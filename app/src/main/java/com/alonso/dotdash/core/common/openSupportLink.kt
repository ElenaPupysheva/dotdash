package com.alonso.dotdash.core.common

import android.content.Context
import android.content.Intent
import android.net.Uri

private const val SUPPORT_URL = "https://www.donationalerts.com/r/alonsonya"
fun openSupportLink(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(SUPPORT_URL))
    context.startActivity(intent)
}
