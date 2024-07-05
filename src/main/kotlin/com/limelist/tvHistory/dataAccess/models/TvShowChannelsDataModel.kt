package com.limelist.tvHistory.dataAccess.models

import kotlinx.serialization.Serializable
import java.sql.ResultSet

@Serializable
data class TvShowChannelsDataModel (
    val channelId: Int,
    val channelName: String,
    val channelAssessment: Float,
    val channelImageUrl: String,
    val releaseId: Int,
    val releaseDescription: String,
    val releaseStart: Long,
    val releaseStop: Long
) {
    constructor(set: ResultSet) : this(
        set.getInt("channel_id"),
        set.getString("channel_name"),
        set.getFloat("channel_assessment"),
        set.getString("channel_image_url"),
        set.getInt("release_id"),
        set.getString("release_description"),
        set.getLong("release_start"),
        set.getLong("release_stop")
    )
}

//channels.id as channel_id,
//channels.name as channel_name,
//channels.assessment as channel_assessment,
//channels.image_url as channel_image_url,
//releases.id as release_id,
//releases.description as releases_description,
//releases.time_start as release_start,
//releases.time_stop as release_stop
