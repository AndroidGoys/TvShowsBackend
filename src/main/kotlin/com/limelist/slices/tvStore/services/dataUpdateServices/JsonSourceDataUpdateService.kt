package com.limelist.slices.tvStore.services.dataUpdateServices

import com.limelist.slices.tvStore.dataAccess.interfaces.*
import com.limelist.slices.tvStore.dataAccess.models.*
import com.limelist.slices.tvStore.services.dataUpdateServices.source.models.*
import com.limelist.slices.tvStore.services.dataUpdateServices.yandex.api.imageParams.ImagesSearchParams
import com.limelist.slices.tvStore.services.dataUpdateServices.yandex.api.YandexSearchApiClient

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class JsonSourceDataUpdateService(
    private val channels: TvChannelsRepository,
    private val shows: TvShowsRepository,
    private val releases: TvReleasesRepository,
    private val config: JsonSourceDataUpdateServiceConfig,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
) : DataUpdateService {
    private val loadLoopScope = CoroutineScope(coroutineContext)

    private val yandex =
        if (config.findShowImages)
            YandexSearchApiClient(
                config.folderId ?: throw IllegalArgumentException("folderId is missing"),
                config.apiKey ?: throw IllegalArgumentException("apiKey is missing")
            )
        else
            null

    @OptIn(ExperimentalSerializationApi::class)
    private fun loadDataSet(): SourceDataSet {
        if (config.pathToDataSet == null)
            throw NullPointerException("config.pathToDataSet == null")

        val file = File(config.pathToDataSet)
        return Json.decodeFromStream<SourceDataSet>(file.inputStream())
    }

    private fun convertToTvShow(
        show: SourceShow,
        releases: List<SourceRelease>
    ): TvShowCreateModel{
        val description = releases.find {
            release -> release.showId == show.id
        }?.description ?: ""

        return TvShowCreateModel(
            show.id,
            show.name,
            0,
            null,
            null,
            description
        )
    }

    private fun convertToTvChannel(
        channel: SourceChannel
    ): TvChannelCreateModel {
        return TvChannelCreateModel(
            channel.id,
            channel.name,
            channel.image,
            channel.description,
            listOf("https://limehd.tv/channel/${channel.address}")
        )
    }

    private fun convertToTvRelease(
        release: SourceRelease
    ):TvReleaseCreateModel {
        return TvReleaseCreateModel(
            release.channelId,
            release.showId,
            release.description,
            release.timestart,
            release.timestop
        )
    }

    private suspend fun findImagesForShows(){
        yandex?: throw NullPointerException("Yandex should not be null")
        yandex.init()
        val limit = 1000;
        val withoutImageShows = shows.getWithoutImageShows(limit, 0)

        withoutImageShows.shows.forEach { show ->
            val searchParams = ImagesSearchParams("Фильм сериал ${show.name}")
            val images = yandex.searchImages(searchParams)

            if (images.isEmpty())
                return@forEach

            shows.updateMany(listOf(
                TvShowCreateModel(
                    show.id,
                    show.name,
                    show.ageLimit.flag,
                    images.first().url,
                    images.subList(1, images.size),
                    show.description,
                )
            ))
        }
    }

    override suspend fun start() {
        loadLoopScope.launch {
            //data.shows
            if (config.reloadDataSet) {
                val data: SourceDataSet = loadDataSet()
                val channelsData = data.channels.map { convertToTvChannel(it) }
                channels.updateMany(channelsData)

                val showsData = data.shows.map { convertToTvShow(it, data.releases) }
                shows.updateMany(showsData)

                val releasesData = data.releases.map { convertToTvRelease(it) }
                releases.updateMany(releasesData)
            }

            if (config.findShowImages && yandex != null){
                findImagesForShows()
            }
        }
    }


    override suspend fun stop() {
        loadLoopScope.cancel();
    }
}