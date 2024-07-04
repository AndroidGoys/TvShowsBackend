package com.limelist.tvHistory.services.dataUpdateServices

import com.limelist.tvHistory.dataAccess.interfaces.TvChannelsRepository
import com.limelist.tvHistory.dataAccess.interfaces.TvReleasesRepository

import com.limelist.tvHistory.dataAccess.interfaces.TvShowsRepository
import com.limelist.tvHistory.dataAccess.models.TvReleaseDataModel
import com.limelist.tvHistory.services.dataUpdateServices.source.models.SourceChannel
import com.limelist.tvHistory.services.dataUpdateServices.source.models.SourceDataSet
import com.limelist.tvHistory.services.dataUpdateServices.source.models.SourceRelease
import com.limelist.tvHistory.services.dataUpdateServices.source.models.SourceShow
import com.limelist.tvHistory.services.models.channels.TvChannelDetailsModel
import com.limelist.tvHistory.services.models.shows.TvShowDetailsModel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.FileNotFoundException

class JsonSourceDataUpdateService(
    private val channels: TvChannelsRepository,
    private val shows: TvShowsRepository,
    private val releases: TvReleasesRepository
) : DataUpdateService {

    @OptIn(ExperimentalSerializationApi::class)
    private fun loadDataSet(): SourceDataSet {

        val classloader = Thread.currentThread().contextClassLoader
        val stream = classloader.getResourceAsStream("dataset.json")
            ?: throw FileNotFoundException("dataset.json")
        return Json.decodeFromStream<SourceDataSet>(stream)
    }

    private suspend fun findTvShowDetails(show: SourceShow): TvShowDetailsModel{
        TODO("Not implemented")
    }

    private fun convertToTvChannel(
        channel: SourceChannel
    ): TvChannelDetailsModel{
        return TvChannelDetailsModel(
            channel.id,
            channel.name,
            channel.image,
            channel.description,
            listOf("https://limehd.tv/channel/${channel.address}")
        )
    }

    private fun convertToTvRelease(
        release: SourceRelease
    ):TvReleaseDataModel {
        return TvReleaseDataModel(
            id = -1,
            release.channelId,
            release.showId,
            release.description,
            release.timestart,
            release.timestop
        )
    }

    override suspend fun start() {
        val data: SourceDataSet = loadDataSet()
        //data.shows


        println(data.shows)
    }

    override suspend fun stop() {
        return;
    }
}