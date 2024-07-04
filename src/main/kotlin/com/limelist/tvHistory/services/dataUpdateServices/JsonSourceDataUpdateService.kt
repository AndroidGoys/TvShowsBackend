package com.limelist.tvHistory.services.dataUpdateServices

import com.limelist.tvHistory.dataAccess.interfaces.TvChannelsRepository

import com.limelist.tvHistory.dataAccess.interfaces.TvShowsRepository
import com.limelist.tvHistory.services.dataUpdateServices.source.models.DataSet
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.FileNotFoundException

class JsonSourceDataUpdateService(
    private val channels: TvChannelsRepository,
    private val shows: TvShowsRepository,
) : DataUpdateService {

    @OptIn(ExperimentalSerializationApi::class)
    private fun loadDataSet(): DataSet {

        val classloader = Thread.currentThread().contextClassLoader
        val stream = classloader.getResourceAsStream("dataset.json")
            ?: throw FileNotFoundException("dataset.json")
        return Json.decodeFromStream<DataSet>(stream)
    }


    override suspend fun start() {
        val data: DataSet = loadDataSet()


        println(data)
    }

    override suspend fun stop() {
        return;
    }
}