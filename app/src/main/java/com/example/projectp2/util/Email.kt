package com.example.projectp2.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun sendEmail(context: Context, emailAddress: String, emailSubject: String, emailText: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        putExtra(Intent.EXTRA_TEXT, emailText)
    }
    context.startActivity(intent)
}
