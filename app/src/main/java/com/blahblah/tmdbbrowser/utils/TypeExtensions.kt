package com.blahblah.tmdbbrowser.utils

/**
 * Description: Type extensions
 * Created by shmuel on 6.3.19.
 */

fun IntRange.contains(other: IntRange): Boolean = other.first in this && other.last in this

/**
 * Concatenate a [prefix] string if is not empty
 *
 * @param prefix prefix string
 *
 * @return concatenated string or an empty one
 */
fun String.addToIfNotEmpty(prefix: String): String = if (prefix.isNotEmpty()) {
    prefix + this
} else {
    ""
}


/**
 * Clip float to int range
 *
 * @param intRange range to clip
 *
 * @return clipped float range.min <= x <= rangeMax
 */
fun Float.clipTo(min: Int, max: Int): Float = when {
    this < min -> min.toFloat()
    this <= max -> this
    else -> max.toFloat()
}

