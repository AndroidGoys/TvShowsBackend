package com.limelist

import com.limelist.slices.shared.DbLifeCycle
import com.limelist.slices.shared.HostedService
import com.limelist.slices.tvStore.TvStoreServices

class ApplicationServices(
    val tvStoreServices: TvStoreServices,
    val backgroundServices: Iterable<HostedService>,
    val databases: Iterable<DbLifeCycle>
)