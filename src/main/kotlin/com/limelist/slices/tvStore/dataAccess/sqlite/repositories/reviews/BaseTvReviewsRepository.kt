package com.limelist.slices.tvStore.dataAccess.sqlite.repositories.reviews

import com.limelist.slices.tvStore.dataAccess.interfaces.TvReviewsRepository
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.BaseSqliteTvRepository
import com.limelist.slices.tvStore.services.models.comments.TvReview
import com.limelist.slices.tvStore.services.models.comments.TvReviews
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection

abstract class BaseTvReviewsRepository(
    connection: Connection,
    mutex: Mutex,
    val parentTable: String,
    val targetTable: String
): BaseSqliteTvRepository(connection, mutex, targetTable),
    TvReviewsRepository {

    private val getReviewsStatement = connection.prepareStatement("""
        SELECT * 
            FROM $targetTable
        WHERE parent_id = ?;
        LIMIT ?
        OFFSET ?
    """)

    override suspend fun get(
        parentId: Int,
        limit: Int,
        offset: Int
    ): TvReviews = mutex.withLock {
        val set = getReviewsStatement.run {
            setInt(1,  parentId)
            setInt(2, limit)
            setInt(3, offset)
            return@run executeQuery()
        }

        val reviews = parseReviews(set)
        return TvReviews(-1, reviews)
    }


    private val addReviews = connection.prepareStatement("""
        INSERT OR IGNORE INTO $targetTable (
            user_id,
            parent_id,
            date,
            text,
            assessment
        ) VALUES (?, ?, ?, ?, ?)
    """)

    override suspend fun add(
        parentId: Int,
        comment: TvReview
    ) : Unit = mutex.withLock {
        addReviews.run{
            setInt(1, comment.userId)
            setInt(2, parentId)
            setLong(3, comment.date)
            setString(4, comment.text)
            setInt(5, comment.assessment)
            executeUpdate()
        }
    }
}