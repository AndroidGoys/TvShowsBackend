package com.limelist.slices.users.dataAccess.sqlite

import com.limelist.slices.shared.getCurrentUnixUtc0TimeSeconds
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.BaseSqliteTvRepository.Companion.channelsTabelName
import com.limelist.slices.users.dataAccess.interfaces.UsersRepository
import com.limelist.slices.users.services.models.UserData
import com.limelist.slices.users.services.models.UserPermissions
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Long.getLong
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import javax.swing.UIManager.getString

class UsersSqliteRepository(
    private val connection: Connection,
    private val mutex: Mutex
) : UsersRepository {

    val table = "users"

    init {
        connection.autoCommit = false
        val statement = connection.createStatement();

        statement.execute("""
            PRAGMA case_sensitive_like=OFF;
        """.trimIndent())


//        val id: Int,
//        val email: String,
//        val username: String,
//        val avatarUrl: String?,
//        val registrationDateSeconds: Long,
//        val permissions: UserPermissions
//
        statement.execute("""
            CREATE TABLE IF NOT EXISTS $table (
                id INTEGER PRIMARY KEY,
                email VARCHAR(255) NOT NULL UNIQUE,
                username VARCHAR(50) NOT NULL,
                avatar_url TEXT,
                registration_date REAL,
                permissions REAL
            );
        """)

        connection.commit()
    }

    private val addStatement = connection.prepareStatement("""
        INSERT INTO $table (
            email, 
            username, 
            avatar_url, 
            registration_date, 
            permissions
        ) VALUES (?, ?, ?, ?, ?)
    """, Statement.RETURN_GENERATED_KEYS)

    override suspend fun add(
        data: UserData
    ): UserData = mutex.withLock {
        addStatement.run {
            setString(1, data.email)
            setString(2, data.username)
            setString(3, data.avatarUrl)
            setLong(4, data.registrationDateSeconds)
            setLong(5, data.permissions.flag)

            return@run executeUpdate()
        }

        val keys = addStatement.generatedKeys
        if (!keys.next())
            throw UnknownError()

        val id =  keys.getInt(1)

        return@withLock UserData(
            id,
            data.email,
            data.username,
            data.avatarUrl,
            data.registrationDateSeconds,
            data.permissions
        )
    }

    private val findByEmailStatement = connection.prepareStatement("""
        SELECT *
            FROM users
        WHERE email = ?
        LIMIT 1
    """)

    override suspend fun findByEmail(
        email: String
    ): UserData? = mutex.withLock {
        val set = findByEmailStatement.run {
            setString(1, email)
            return@run executeQuery()
        }

        if (!set.next())
            return@withLock null

        return@withLock parseUserData(set)
    }

    private fun parseUserData(
        set: ResultSet
    ) = UserData(
        set.getInt("id"),
        set.getString("email"),
        set.getString("username"),
        set.getString("avatar_url"),
        set.getLong("registration_date"),
        UserPermissions(set.getLong("permisssions"))
    )
}