package com.limelist.slices.tvStore.dataAccess.sqlite.repositories

import com.limelist.slices.tvStore.dataAccess.interfaces.TvTagsRepository
import com.limelist.slices.tvStore.dataAccess.models.create.TvTagCreateModel
import com.limelist.slices.tvStore.services.models.tags.TvTagDetails
import com.limelist.slices.tvStore.services.models.tags.TvTagBelong
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

class TvTagsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseSqliteTvRepository(connection, mutex, "tags"),
    TvTagsRepository {

    private val searchByNameStatement = connection.prepareStatement("""
        SELECT * 
            FROM tags
        WHERE lower_name LIKE ?
    """)

    private val insertTagStatement = connection.prepareStatement("""
        INSERT INTO tags
            (name, lower_name, belong) 
        VALUES (?, ?, ?)
    """, Statement.RETURN_GENERATED_KEYS)

    private val getByIdStatement = connection.prepareStatement("""
        SELECT *
            FROM tags
        WHERE id = ?
    """)

    override suspend fun createIfNotExists(
        tvTag: TvTagCreateModel
    ): TvTagDetails = mutex.withLock {
        var set = searchByNameStatement.run {
            setString(1, "%${tvTag.name.lowercase()}%");
            return@run executeQuery()
        }

        if (set.next()) {
            return parseTagDetails(set)
        }

        insertTagStatement.run {
            setString(1, tvTag.name)
            setString(2, tvTag.name.lowercase())
            setInt(3, tvTag.belong)
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

        return parseTagDetails(set)
    }

    private fun parseTagDetails(set: ResultSet) = TvTagDetails(
        set.getInt("id"),
        set.getString("name"),
        TvTagBelong(set.getInt("belong"))
    )

}