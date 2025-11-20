package com.android.utils

const val DEFAULT_FLOAT = 0F
const val DEFAULT_DOUBLE = 0.0
const val DEFAULT_INT = 0
const val DEFAULT_LONG = 0L
const val DEFAULT_BOOLEAN = false
const val DEFAULT_BYTE = 0.toByte()
const val DEFAULT_SHORT = 0.toShort()
const val DEFAULT_CHAR = '\u0000'
const val EMPTY_STRING = ""

fun Float?.orDefault() = this ?: DEFAULT_FLOAT
fun Double?.orDefault() = this ?: DEFAULT_DOUBLE
fun Int?.orDefault() = this ?: DEFAULT_INT
fun Long?.orDefault() = this ?: DEFAULT_LONG
fun Boolean?.orDefault() = this ?: DEFAULT_BOOLEAN
fun Byte?.orDefault() = this ?: DEFAULT_BOOLEAN
fun Short?.orDefault() = this ?: DEFAULT_SHORT
fun Char?.orDefault() = this ?: DEFAULT_CHAR
