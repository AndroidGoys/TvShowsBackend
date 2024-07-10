package com.limelist

import com.limelist.shared.DbLifeCycle
import com.limelist.shared.HostedService
import com.limelist.tvStore.TvStoreServices

class ApplicationServices(
    val tvStoreServices: TvStoreServices,
    val backgroundServices: Iterable<HostedService>,
    val databases: Iterable<DbLifeCycle>
)