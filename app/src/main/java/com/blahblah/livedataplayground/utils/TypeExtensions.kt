package com.blahblah.livedataplayground.utils

/**
 * Description: Type extensions
 * Created by shmuel on 6.3.19.
 */
fun IntRange.contains(other: IntRange): Boolean = other.first in this && other.last in this

fun String.addToIfNotEmpty(prefix: String): String = if (prefix.isNotEmpty()) {
    prefix + this
} else {
    ""
}

