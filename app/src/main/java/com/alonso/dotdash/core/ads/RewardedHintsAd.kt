package com.alonso.dotdash.core.ads

import android.app.Activity

class RewardedHintsAd(
    activity: Activity
) {
    private val delegate = VkRewardedHintsAd(activity)

    fun loadAndShow(
        onReward: () -> Unit,
        onUnavailable: () -> Unit
    ) {
        delegate.loadAndShow(
            Runnable { onReward() },
            Runnable { onUnavailable() }
        )
    }
}
