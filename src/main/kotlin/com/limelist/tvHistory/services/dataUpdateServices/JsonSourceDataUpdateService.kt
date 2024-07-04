package com.limelist.tvHistory.services.dataUpdateServices

import com.limelist.tvHistory.dataAccess.interfaces.TvChannelsRepository
import com.limelist.tvHistory.dataAccess.interfaces.TvReleasesRepository

import com.limelist.tvHistory.dataAccess.interfaces.TvShowsRepository
import com.limelist.tvHistory.services.dataUpdateServices.source.models.DataSet
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.InputStream

class JsonSourceDataUpdateService(
    private val channels: TvChannelsRepository,
    private val shows: TvShowsRepository,
    private val releases: TvReleasesRepository,
) : DataUpdateService {

    private fun loadDataSet(): DataSet {
        val stream = File("dataset.json").inputStream()
        return Json.decodeFromStream<DataSet>(stream)
    }


    override suspend fun start() {
        val data: DataSet = loadDataSet()


    }

    override suspend fun stop() {
        return;
    }
}