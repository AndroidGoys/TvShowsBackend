package com.limelist.tvHistory.services.dataUpdateServices

import com.limelist.tvHistory.dataAccess.interfaces.TvChannelsRepository
import com.limelist.tvHistory.dataAccess.interfaces.TvReleasesRepository

import com.limelist.tvHistory.dataAccess.interfaces.TvShowsRepository
import com.limelist.tvHistory.dataAccess.models.TvChannelCreateModel
import com.limelist.tvHistory.dataAccess.models.TvReleaseCreateModel
import com.limelist.tvHistory.dataAccess.models.TvShowCreateModel
import com.limelist.tvHistory.services.dataUpdateServices.source.models.SourceChannel
import com.limelist.tvHistory.services.dataUpdateServices.source.models.SourceDataSet
import com.limelist.tvHistory.services.dataUpdateServices.source.models.SourceRelease
import com.limelist.tvHistory.services.dataUpdateServices.source.models.SourceShow
import com.limelist.tvHistory.services.models.shows.TvShowDetailsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.FileNotFoundException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class JsonSourceDataUpdateService(
    private val channels: TvChannelsRepository,
    private val shows: TvShowsRepository,
    private val releases: TvReleasesRepository,
    private val coroutineContext: CoroutineContext = EmptyCoroutineContext
) : DataUpdateService {
    private val loadLoopScope = CoroutineScope(coroutineContext)

    @OptIn(ExperimentalSerializationApi::class)
    private fun loadDataSet(): SourceDataSet {

        val classloader = Thread.currentThread().contextClassLoader
        val stream = classloader.getResourceAsStream("dataset.json")
            ?: throw FileNotFoundException("dataset.json")
        return Json.decodeFromStream<SourceDataSet>(stream)
    }

    private suspend fun findTvShowDetails(show: SourceShow): TvShowCreateModel{
        return  TvShowCreateModel(
            show.id,
            show.name,
            0,
            "https://mur-mur.top/cat2/uploads/posts/2024-01/1706006784_mur-mur-top-p-kobaladze-tamara-davidovna-den-rozhdeniya-43.jpg",
            ""
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
            id = -1,
            release.channelId,
            release.showId,
            release.description,
            release.timestart,
            release.timestop
        )
    }

    override suspend fun start() {
        loadLoopScope.launch {
            val data: SourceDataSet = loadDataSet()
            //data.shows
            val channelsData = data.channels.map { convertToTvChannel(it) }
            val releasesData = data.releases.map { convertToTvRelease(it) }
            val showsData = data.shows.map { findTvShowDetails(it) }

            channels.updateMany(channelsData);
            shows.updateMany(showsData)
            releases.updateMany(releasesData);
        }
    }

    override suspend fun stop() {
        loadLoopScope.cancel();
    }
}