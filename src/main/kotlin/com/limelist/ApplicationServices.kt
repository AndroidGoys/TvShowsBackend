package com.limelist

import com.limelist.shared.DbLifeCycle
import com.limelist.shared.HostedService
import com.limelist.tvHistory.TvHistoryServices

class ApplicationServices(
    val tvHistoryServices: TvHistoryServices,
    val backgroundServices: Iterable<HostedService>,
    val databases: Iterable<DbLifeCycle>
)