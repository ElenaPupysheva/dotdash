package com.alonso.dotdash.core.ads

import android.app.Activity
import com.alonso.dotdash.BuildConfig
import com.my.target.ads.Reward as VkReward
import com.my.target.ads.RewardedAd as VkRewardedAd
import com.my.target.common.models.IAdLoadingError
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader

class RewardedHintsAd(
    private val activity: Activity
) {
    private val yandexLoader = RewardedAdLoader(activity.applicationContext)
    private var yandexAd: RewardedAd? = null
    private var vkAd: VkRewardedAd? = null
    private var isLoadingOrShowing = false

    fun loadAndShow(
        onReward: () -> Unit,
        onUnavailable: () -> Unit
    ) {
        if (isLoadingOrShowing) return

        isLoadingOrShowing = true
        loadVkAd(onReward, onUnavailable)
    }

    private fun loadYandexAd(
        onReward: () -> Unit,
        onUnavailable: () -> Unit
    ) {
        val adUnitId = BuildConfig.YANDEX_REWARDED_AD_UNIT_ID
        if (adUnitId.isBlank()) {
            finishUnavailable(onUnavailable)
            return
        }

        val request = AdRequest.Builder(adUnitId).build()
        yandexLoader.loadAd(
            request,
            object : RewardedAdLoadListener {
                override fun onAdLoaded(ad: RewardedAd) {
                    yandexAd = ad
                    showYandexAd(ad, onReward, onUnavailable)
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    yandexAd = null
                    finishUnavailable(onUnavailable)
                }
            }
        )
    }

    private fun showYandexAd(
        ad: RewardedAd,
        onReward: () -> Unit,
        onUnavailable: () -> Unit
    ) {
        ad.setAdEventListener(
            object : RewardedAdEventListener {
                override fun onAdShown() = Unit

                override fun onAdFailedToShow(error: AdError) {
                    clearYandexAd(ad)
                    finishUnavailable(onUnavailable)
                }

                override fun onAdDismissed() {
                    clearYandexAd(ad)
                    isLoadingOrShowing = false
                }

                override fun onAdClicked() = Unit

                override fun onAdImpression(impressionData: ImpressionData?) = Unit

                override fun onRewarded(reward: Reward) {
                    onReward()
                }
            }
        )
        ad.show(activity)
    }

    private fun clearYandexAd(ad: RewardedAd) {
        ad.setAdEventListener(null)
        if (yandexAd === ad) yandexAd = null
    }

    private fun loadVkAd(
        onReward: () -> Unit,
        onUnavailable: () -> Unit
    ) {
        val slotId = BuildConfig.VK_REWARDED_SLOT_ID
        if (slotId <= 0) {
            loadYandexAd(onReward, onUnavailable)
            return
        }

        val ad = VkRewardedAd(slotId, activity)
        vkAd = ad
        ad.setListener(
            object : VkRewardedAd.RewardedAdListener {
                override fun onLoad(ad: VkRewardedAd) {
                    ad.show()
                }

                override fun onNoAd(
                    error: IAdLoadingError,
                    ad: VkRewardedAd
                ) {
                    clearVkAd(ad)
                    loadYandexAd(onReward, onUnavailable)
                }

                override fun onClick(ad: VkRewardedAd) = Unit

                override fun onFailedToShow(ad: VkRewardedAd) {
                    clearVkAd(ad)
                    loadYandexAd(onReward, onUnavailable)
                }

                override fun onDisplay(ad: VkRewardedAd) = Unit

                override fun onDismiss(ad: VkRewardedAd) {
                    clearVkAd(ad)
                    isLoadingOrShowing = false
                }

                override fun onReward(reward: VkReward, ad: VkRewardedAd) {
                    onReward()
                }
            }
        )
        ad.load()
    }

    private fun clearVkAd(ad: VkRewardedAd) {
        if (vkAd === ad) vkAd = null
    }

    private fun finishUnavailable(onUnavailable: () -> Unit) {
        isLoadingOrShowing = false
        onUnavailable()
    }
}
