package com.flexingstudios.flexingnetwork

enum class Debug(
    @get:JvmName("isEnabled")
    var enabled: Boolean = false
) {
    MYSQL;
}