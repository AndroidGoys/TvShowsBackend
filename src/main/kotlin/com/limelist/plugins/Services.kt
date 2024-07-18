package com.limelist.plugins

import com.limelist.ApplicationConfig
import java.sql.DriverManager;
import io.ktor.server.application.*
import kotlinx.coroutines.sync.Mutex

import com.limelist.ApplicationServices
import com.limelist.slices.auth.AuthConfig
import com.limelist.slices.auth.AuthServices
import com.limelist.slices.auth.dataAccess.sqlite.repositories.AuthSqliteRepository
import com.limelist.slices.auth.services.KtorTokenIssuanceService
import com.limelist.slices.auth.services.PBKDF2Hasher
import com.limelist.slices.tvStore.TvStoreConfig
import com.limelist.slices.tvStore.TvStoreServices
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.*
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.reviews.TvChannelReviewsSqliteRepository
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.reviews.TvShowReviewsSqliteRepository
import com.limelist.slices.tvStore.services.tvChannelServices.TvChannelsService;
import com.limelist.slices.tvStore.services.tvShowServices.TvShowsService;
import com.limelist.slices.tvStore.services.dataUpdateServices.JsonSourceDataUpdateService
import com.limelist.slices.users.UsersServices
import com.limelist.slices.users.dataAccess.sqlite.UsersSqliteRepository
import com.limelist.slices.users.services.internal.DefaultUsersRegistrationInternalService
import com.limelist.slices.users.services.internal.UsersRegistrationInternalService
import com.limelist.slices.users.services.userDataServices.UsersDataService
import kotlin.coroutines.CoroutineContext


fun Application.configureServices(
    coroutineContext: CoroutineContext,
    config: ApplicationConfig
) : ApplicationServices {

    val userServices = configureUserServices()

    val tvHistoryServices = configureTvHistoryServices(
        coroutineContext,
        config.tvStoreConfig
    )

    val authServices = configureAuthServices(
        config.authConfig,
        userServices.usersRegistrationService
    )
    return ApplicationServices(
        tvHistoryServices,
        authServices,
        userServices,
        tvHistoryServices.backgroundServices,
        tvHistoryServices.databases
    )
}

fun Application.configureTvHistoryServices(
    coroutineContext: CoroutineContext,
    config: TvStoreConfig
): TvStoreServices {
    val conn = DriverManager.getConnection("jdbc:sqlite:tvStore.sql")
    val mutex = Mutex()

    val dbLifeCycle = TvSqliteDbLifeCycle(conn, mutex)

    val channelReviews = TvChannelReviewsSqliteRepository(conn, mutex)
    val showReviews = TvShowReviewsSqliteRepository(conn, mutex)

    val channels = TvChannelsSqliteRepository(conn, mutex)
    val shows = TvShowsSqliteRepository(conn, mutex)
    val releases = TvReleasesSqliteRepository(conn, mutex);
    val tags = TvTagsSqliteRepository(conn, mutex);

    val tvChannelsService = TvChannelsService(channels, channelReviews);
    val tvShowsService = TvShowsService(shows, showReviews);

    val dataUpdateService = JsonSourceDataUpdateService(
        channels,
        shows,
        releases,
        tags,
        config.dataUpdateConfig,
        coroutineContext
    );

    return TvStoreServices(
        tvChannelsService,
        tvShowsService,
        listOf(dataUpdateService),
        listOf(dbLifeCycle)
    )
}


fun Application.configureAuthServices(
    config: AuthConfig,
    users: UsersRegistrationInternalService
) : AuthServices {
    val conn = DriverManager.getConnection("jdbc:sqlite:authIdent.sql")
    val mutex = Mutex()

    val authRepository = AuthSqliteRepository(conn, mutex)
    val passwordHasher = PBKDF2Hasher()

    val tokenIssuanceService = KtorTokenIssuanceService(
        users,
        authRepository,
        passwordHasher,
        config
    )

    return AuthServices(
        tokenIssuanceService
    )
}

fun Application.configureUserServices() : UsersServices {
    val conn = DriverManager.getConnection("jdbc:sqlite:users.sql")
    val mutex = Mutex()

    val users = UsersSqliteRepository(conn, mutex)
    val usersRegistrationService = DefaultUsersRegistrationInternalService(
        users
    )
    val usersDataService = UsersDataService(users)

    return UsersServices(
        usersRegistrationService,
        usersDataService
    )
}