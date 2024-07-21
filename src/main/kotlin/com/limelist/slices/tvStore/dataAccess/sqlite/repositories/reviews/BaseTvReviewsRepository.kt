package com.limelist.slices.tvStore.dataAccess.sqlite.repositories.reviews

import com.limelist.slices.tvStore.dataAccess.interfaces.TvReviewsRepository
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.BaseSqliteTvRepository
import com.limelist.slices.tvStore.services.models.reviews.ReviewsDistribution
import com.limelist.slices.tvStore.services.models.reviews.TvReview
import com.limelist.slices.tvStore.services.models.reviews.TvReviews
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
        WHERE parent_id = ? AND date > ?
        ORDER BY date
        LIMIT ?
    """)

    private val getReviewsCountStatement = connection.prepareStatement("""
        SELECT COUNT(*) 
            FROM $targetTable
    """)

    override suspend fun get(
        parentId: Int,
        limit: Int,
        timeStart: Long
    ): TvReviews = mutex.withLock {
        val set = getReviewsStatement.run {
            setInt(1,  parentId)
            setLong(2, timeStart)
            setInt(3, limit)
            return@run executeQuery()
        }

        val reviews = parseReviews(set)
        return TvReviews(
            getParsedCount(getReviewsCountStatement),
            reviews
        )
    }

    private val getReviewsByAssessmentStatement = connection.prepareStatement("""
        SELECT * 
            FROM $targetTable
        WHERE parent_id = ? AND date > ? AND assessment = ?
        ORDER BY date
        LIMIT ?
    """)

    private val getReviewsCountByAssessmentStatement = connection.prepareStatement("""
        SELECT COUNT(*) 
            FROM $targetTable
        WHERE parent_id = ? AND assessment = ?
    """)


    override suspend fun getByAssessment(
        parentId: Int,
        assessment: Int,
        limit: Int,
        timeStart: Long
    ): TvReviews = mutex.withLock {
        val set = getReviewsByAssessmentStatement.run {
            setInt(1,  parentId)
            setLong(2, timeStart)
            setInt(3, assessment)
            setInt(4, limit)
            return@run executeQuery()
        }

        getReviewsCountByAssessmentStatement.run {
            setInt(1,  parentId)
            setInt(2, assessment)
        }

        val reviews = parseReviews(set)
        return TvReviews(
            getParsedCount(getReviewsCountByAssessmentStatement),
            reviews)
    }

    private val updateReviews = connection.prepareStatement("""
        INSERT INTO $targetTable (
            user_id,
            parent_id,
            date,
            text,
            assessment
        ) VALUES (?, ?, ?, ?, ?)
        ON CONFLICT (user_id, parent_id) DO UPDATE
            SET date = EXCLUDED.date,
                text = EXCLUDED.text,
                assessment = EXCLUDED.assessment
    """)

    override suspend fun update(
        parentId: Int,
        review: TvReview
    ) : Unit = mutex.withLock {
        updateReviews.run{
            setInt(1, review.userId)
            setInt(2, parentId)
            setLong(3, review.date)
            setString(4, review.text)
            setInt(5, review.assessment)
            executeUpdate()
        }

        connection.commit()
    }

    private val getForUserStatement = connection.prepareStatement("""
        SELECT * 
            FROM $targetTable
        WHERE parent_id = ? AND user_id = ?
        LIMIT 1
    """)

    override suspend fun getForUser(
        userId: Int,
        parentId: Int
    ): TvReview? = mutex.withLock {
        val set = getForUserStatement.run {
            setInt(1,  parentId)
            setInt(2, userId)
            return@run executeQuery()
        }

        val reviews = parseReviews(set)

        return@withLock reviews.firstOrNull()
    }

    private val getDistributionStatement = connection.prepareStatement("""
        SELECT assessment, COUNT(*)
            FROM (
                SELECT * FROM $targetTable
                    WHERE parent_id = ?
            ) as reviews 
        GROUP BY assessment
    """)

    override suspend fun getDistribution(
        parentId: Int
    ): ReviewsDistribution = mutex.withLock{
        val set = getForUserStatement.run {
            setInt(1, parentId)
            return@run executeQuery()
        }

        val distribution = buildMap {
            while (set.next()) {
                val assessment = set.getInt(1)
                val count = set.getInt(2)
                set(assessment, count)
            }
        }

        return@withLock ReviewsDistribution(distribution)
    }

    private val deleteStatement = connection.prepareStatement("""
        DELETE FROM $targetTable
            WHERE parent_id = ? AND user_id = ?
    """)

    override suspend fun delete(parentId: Int, userId: Int) {
        deleteStatement.run {
            setInt(1, parentId)
            setInt(2, userId)
            executeUpdate()
        }
        connection.commit()
    }
}