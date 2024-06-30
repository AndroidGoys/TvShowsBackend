package com.limelist

import com.limelist.shared.IHostedService
import com.limelist.tvHistory.TvHistoryServices

class ApplicationServices(
    val tvHistoryServices: TvHistoryServices,
    val backgroundServices: Iterable<IHostedService>
)