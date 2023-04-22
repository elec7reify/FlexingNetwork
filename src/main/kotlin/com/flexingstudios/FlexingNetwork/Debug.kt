package com.flexingstudios.FlexingNetwork

enum class Debug(private var enabled: Boolean = false) {
    MYSQL;

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    fun isEnabled(): Boolean {
        return enabled
    }
}