package com.limelist.slices.tvStore.dataAccess.sqlite.repositories

import com.limelist.slices.tvStore.dataAccess.interfaces.TvTagsRepository
import com.limelist.slices.tvStore.dataAccess.models.create.TvTagCreateModel
import com.limelist.slices.tvStore.services.models.tags.TvTag
import com.limelist.slices.tvStore.services.models.tags.TvTagBelong
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

class TvTagsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseSqliteTvRepository(connection, mutex, tagsTableName),
    TvTagsRepository {

    private val searchByNameStatement = connection.prepareStatement("""
        SELECT * 
            FROM tags
        WHERE name LIKE ?
    """)

    private val insertTagStatement = connection.prepareStatement("""
        INSERT INTO tags
            (name, belong) 
        VALUES (?, ?)
    """, Statement.RETURN_GENERATED_KEYS)

    private val getByIdStatement = connection.prepareStatement("""
        SELECT *
            FROM tags
        WHERE id = ?
    """)

    override suspend fun createIfNotExists(
        tvTag: TvTagCreateModel
    ): TvTag = mutex.withLock {
        var set = searchByNameStatement.run {
            setString(1, "%${tvTag.name}%");
            return@run executeQuery()
        }

        if (set.next()) {
            return parseTag(set)
        }

        insertTagStatement.run {
            setString(1, tvTag.name)
            setInt(2, tvTag.belong)
            return@run executeUpdate()
        }
        connection.commit()

        set = insertTagStatement.generatedKeys
        if (!set.next())
            throw UnknownError()

        val id =  set.getInt(1)

        set = getByIdStatement.run {
            setInt(1, id)
            return@run executeQuery()
        }

        if (!set.next())
            throw UnknownError()

        return parseTag(set)
    }

    private fun parseTag(set: ResultSet) = TvTag(
        set.getInt("id"),
        set.getString("name"),
        TvTagBelong(set.getInt("belong"))
    )
}