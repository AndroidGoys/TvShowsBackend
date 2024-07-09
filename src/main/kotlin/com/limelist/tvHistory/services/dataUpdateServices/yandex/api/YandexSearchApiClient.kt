package com.limelist.tvHistory.services.dataUpdateServices.yandex.api

import com.limelist.tvHistory.services.dataUpdateServices.yandex.api.imageParams.ImagesSearchParams
import com.limelist.tvHistory.services.dataUpdateServices.yandex.api.limitParams.limitSearchParams
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.InputSource
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Dictionary
import javax.xml.parsers.DocumentBuilderFactory


class YandexSearchApiClient(
    val folderId: String,
    val apiKey: String
) {
    private val baseUrl = "https://yandex.ru"
    private val http = HttpClient(OkHttp);
    private var limits: List<SearchLimit>? = null
    private val rateLimiter = SimpleRateLimiting(100)

    suspend fun init(){
        limits = getApiLimits()
    }

    private suspend inline fun sendGetRequestWithRateLimit(
        route: String,
        params: SearchParams,
        crossinline block: (HttpRequestBuilder) -> Unit = {}
    ): HttpResponse = rateLimiter.withLock {
        val currentLimit = getCurrentApiLimits()
        if (currentLimit.limit != rateLimiter.currentRateLimit)
            rateLimiter.updateRateLimit(currentLimit.limit)

        return@withLock sendGetRequest(route, params, block)
    }


    private suspend inline fun sendGetRequest(
        route: String,
        params: SearchParams,
        block: (HttpRequestBuilder) -> Unit = {}
    ): HttpResponse {
        return http.get(baseUrl + route) {
            url{
                parameters.apply {
                    append("folderid", folderId)
                    append("apikey", apiKey)

                    params.fillBuilder(this)
                }
            }
            block(this)
        }
    }

    suspend fun searchImages(
        params: ImagesSearchParams
    ): List<YandexImage>{
        val response = sendGetRequestWithRateLimit(
            "/images-xml",
            params
        )
        return parseImages(response)
    }

    private suspend fun parseImages(
        response: HttpResponse
    ): List<YandexImage> {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = withContext(Dispatchers.IO) {
            documentBuilder.parse(
                response.bodyAsChannel().toInputStream()
            )
        }

        val urlNodes = document.getElementsByTagName("url")

        val urls = buildList {
            for (i in 0 until urlNodes.length){
                val url = urlNodes?.item(i)
                if (url != null)
                    add(YandexImage(url.textContent))
            }
        }
        return urls
    }

    suspend fun getApiLimits(): List<SearchLimit> {
        val response = sendGetRequest("/search/xml", limitSearchParams())
        return parseLimits(response)
    }

    suspend fun getCurrentApiLimits(): SearchLimit {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val currentHourLimit = limits?.elementAt(now.hour);

        val nowDay = now.dayOfYear
        val lastUpdateDay = currentHourLimit?.from?.dayOfYear ?: -2

        if (nowDay - lastUpdateDay > 1) {
            limits = getApiLimits()
            return getCurrentApiLimits()
        }

        return currentHourLimit!!
    }

    private suspend fun parseLimits(
        response: HttpResponse
    ): List<SearchLimit> {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val strValue = response.bodyAsText()
        val document = withContext(Dispatchers.IO) {
            documentBuilder.parse(
                InputSource(strValue.byteInputStream())
            )
        }

        val timeIntervalNodes = document.getElementsByTagName("time-interval")

        val timeIntervals = ArrayList<SearchLimit>(24)

        for (i in 0 until timeIntervalNodes.length){
            val intervalNode = timeIntervalNodes?.item(i)
                ?: continue

            val attributes = intervalNode.attributes

            val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss xx")
            val from = OffsetDateTime.parse(
                attributes.getNamedItem("from").nodeValue,
                pattern
            )
            val to = OffsetDateTime.parse(
                attributes.getNamedItem("to").nodeValue,
                pattern
            )

            val limit = intervalNode.textContent.toInt()

            timeIntervals.add(
                SearchLimit(
                    from,
                    to,
                    limit
                )
            )

        }
        return timeIntervals.sortedBy { it.from.hour }
    }
}