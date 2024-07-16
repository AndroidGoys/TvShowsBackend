package com.limelist

import com.limelist.slices.auth.AuthServices
import com.limelist.slices.shared.DbLifeCycle
import com.limelist.slices.shared.HostedService
import com.limelist.slices.tvStore.TvStoreServices
import com.limelist.slices.users.UsersServices

class ApplicationServices(
    val tvStoreServices: TvStoreServices,
    val authServices: AuthServices,
    val userServices: UsersServices,
    val backgroundServices: Iterable<HostedService>,
    val databases: Iterable<DbLifeCycle>
)