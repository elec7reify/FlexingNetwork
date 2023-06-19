package com.flexingstudios.flexingnetwork.api.command

import com.flexingstudios.common.player.Rank

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class SubCommand(
    val name: String,
    val aliases: Array<String> = [],
    val rank: Rank = Rank.PLAYER,
    val ranks: Array<Rank> = [],
    val hidden: Boolean = false
)
