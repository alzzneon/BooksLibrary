package com.project.books_library.extension

import android.icu.text.SimpleDateFormat
import android.widget.TextView
import java.util.Date
import java.util.Locale

fun TextView.setDate(timestamp: Long?) {
    timestamp?.let {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val date = Date(it)
        this.text = "Ditambahkan pada: ${sdf.format(date)}"
    }
}
