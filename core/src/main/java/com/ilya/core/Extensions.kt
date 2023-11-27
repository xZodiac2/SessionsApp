package com.ilya.core

fun <T> List<T>.isFirst(item: T): Boolean {
    return this.first() == item
}

fun <T> List<T>.isLast(item: T): Boolean {
    return this.last() == item
}