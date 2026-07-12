package com.alonso.dotdash.core.ads

import android.app.Activity
import com.alonso.dotdash.BuildConfig
import com.my.target.ads.Reward
import com.my.target.ads.RewardedAd
import com.my.target.common.models.IAdLoadingError

class RewardedHintsAd(
    private val activity: Activity
) {
    private var ad: RewardedAd? = null
    private var isLoadingOrShowing = false

    fun loadAndShow(
        onReward: () -> Unit,
        onUnavailable: () -> Unit
    ) {
        if (isLoadingOrShowing) return

        val slotId = BuildConfig.VK_REWARDED_SLOT_ID
        if (slotId <= 0) {
            onUnavailable()
            return
        }

        isLoadingOrShowing = true
        val rewardedAd = RewardedAd(slotId, activity)
        ad = rewardedAd

        rewardedAd.setListener(
            object : RewardedAd.RewardedAdListener {
                override fun onLoad(ad: RewardedAd) {
                    ad.show()
                }

                override fun onNoAd(
                    adLoadingError: IAdLoadingError,
                    ad: RewardedAd
                ) {
                    onUnavailable()
                    isLoadingOrShowing = false
                    this@RewardedHintsAd.ad = null
                }

                override fun onClick(ad: RewardedAd) = Unit

                override fun onFailedToShow(ad: RewardedAd) {
                    onUnavailable()
                    isLoadingOrShowing = false
                    this@RewardedHintsAd.ad = null
                }

                override fun onDisplay(ad: RewardedAd) = Unit

                override fun onDismiss(ad: RewardedAd) {
                    isLoadingOrShowing = false
                    this@RewardedHintsAd.ad = null
                }

                override fun onReward(
                    reward: Reward,
                    ad: RewardedAd
                ) {
                    onReward()
                }
            }
        )

        rewardedAd.load()
    }
}
