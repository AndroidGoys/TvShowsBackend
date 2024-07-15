package com.limelist.slices.auth.dataAccess.sqlite.repositories

import com.limelist.slices.auth.dataAccess.interfaces.AuthRepository
import com.limelist.slices.auth.services.models.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection
import java.sql.ResultSet

class AuthSqliteRepository(
    val connection: Connection,
    val mutex: Mutex
) : AuthRepository {
    val table = "idents"

    init {
        connection.autoCommit = false
        val statement = connection.createStatement();

        statement.execute("""
            CREATE TABLE IF NOT EXISTS $table(
                user_id INTEGER PRIMARY KEY,
                login VARCHAR(50) UNIQUE,
                password_hash VARCHAR(50),
                last_update REAL
            )
        """)

        connection.commit()
    }

    val addIdentStatement = connection.prepareStatement("""
        INSERT INTO $table (
            user_id, login, password_hash, last_update
        ) VALUES (?, ?, ?, ?)
    """)
    override suspend fun add(
        data: IdentificationData
    ): Int = mutex.withLock {
        val result = addIdentStatement.run {
            setInt(1, data.userId)
            setString(2, data.login)
            setString(3, data.hashedPassword)
            setLong(4, data.lastUpdate)
            return@run executeUpdate()
        }

        connection.commit()

        return@withLock result
    }

    val updateIdentStatement = connection.prepareStatement("""
        INSERT INTO $table (
            user_id, login, password_hash, last_update
        ) VALUES (?, ?, ?, ?)
        ON CONFLICT(user_id) DO UPDATE SET 
            login = EXCLUDED.login,
            password_hash = EXCLUDED.password_hash,
            last_update = EXCLUDED.last_update
    """)

    override suspend fun update(
        data: IdentificationData
    ) = mutex.withLock {
        val result = updateIdentStatement.run {
            setInt(1, data.userId)
            setString(2, data.login)
            setString(3, data.hashedPassword)
            setLong(4, data.lastUpdate)
            return@withLock executeUpdate()
        }

        connection.commit()

        return@withLock result
    }

    val findByUserIdStatement = connection.prepareStatement("""
        SELECT *
            FROM $table
        WHERE user_id = ?
        
        LIMIT 1
    """)
    override suspend fun findByUserId(
        userId: Int
    ) = mutex.withLock {
        val set = findByUserIdStatement.run {
            setInt(1, userId)
            return@run executeQuery()
        }

        if (!set.next())
            return@withLock null

        return@withLock parseIdentificationData(set)
    }


    val findByLoginStatement = connection.prepareStatement("""
        SELECT *
            FROM $table
        WHERE login = ?
        
        LIMIT 1
    """)

    override suspend fun findByLogin(
        login: String
    ) = mutex.withLock {
        val set = findByLoginStatement.run {
            setString(1, login)
            return@run executeQuery()
        }

        if (!set.next())
            return@withLock null

        return@withLock parseIdentificationData(set)
    }

    private fun parseIdentificationData(
        set: ResultSet
    ) = IdentificationData(
        set.getInt("user_id"),
        set.getString("login"),
        set.getString("password_hash"),
        set.getLong("last_update")
    )

}