package com.limelist.slices.users.services.avatars

import com.limelist.slices.shared.RequestError
import com.limelist.slices.shared.RequestError.ErrorCode.Companion.NotFound
import com.limelist.slices.shared.RequestError.ErrorCode.Companion.UnableToCreateFile
import com.limelist.slices.shared.RequestError.ErrorCode.Companion.UnsupportedFileFormat
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.users.dataAccess.interfaces.UsersRepository
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class AvatarsSharingService(
    val users: UsersRepository
) {

    private val pathToFiles = "./userFiles/"
    private val unableToCreateFile = RequestResult.FailureResult(
        RequestError(
            UnableToCreateFile,
        "unable to create a file"
        ),
        HttpStatusCode.Conflict
    )

    private val unsupportedMediaType = RequestResult.FailureResult(
        RequestError(
            UnsupportedFileFormat,
            "unsupported file format"
        ),
        HttpStatusCode.Conflict
    )

    private val fileNotFound = RequestResult.FailureResult(
        RequestError(
            NotFound,
            "file not found"
        ),
        HttpStatusCode.NotFound
    )


    private suspend fun saveFile(
        userId: Int,
        fileExtension: String,
        stream: InputStream
    ): String {
        val fileRelativeName = pathToFiles + "$userId/avatars/avatar.$fileExtension"
        return withContext(Dispatchers.IO){
            var file = File(fileRelativeName)

            var userAvatarsDirectory = file.getParentFile()
            userAvatarsDirectory.deleteOnExit()
            userAvatarsDirectory.mkdirs()

            file.createNewFile()

            val fileOutputStream = FileOutputStream(file, false)
            fileOutputStream.write(stream.readBytes())
            fileOutputStream.close()
            return@withContext fileRelativeName
        }
    }


    suspend fun loadAvatar(
        userId: Int,
        contentType: String?,
        stream: InputStream
    ): RequestResult<Unit> {
        var fileFullName: String

        val fileExtension = fileExtensionFromContentType(contentType)
            ?: return unsupportedMediaType

        if (fileExtension !in setOf("png", "jpeg", "jpg"))
            return unsupportedMediaType

        try {
            fileFullName = saveFile(userId, fileExtension, stream)
        } catch (e: IOException){
            return unableToCreateFile
        }

        users.setAvatarRoute(userId, "users/$userId/avatar")

        return RequestResult.SuccessResult(
            Unit,
            HttpStatusCode.Created
        )
    }

    private fun fileExtensionFromContentType(contentType: String?): String? {
        if (contentType == null)
            return null

        return contentType
            .split(";")[0]
            .split("/")[1]
    }

    suspend fun getAvatarFile(userId: Int) : RequestResult<File> {
        val dir = File(pathToFiles + "$userId/avatars/")
        val file = dir.listFiles()?.firstOrNull()

        if (file == null)
            return fileNotFound

        return RequestResult.SuccessResult(file)
    }
}