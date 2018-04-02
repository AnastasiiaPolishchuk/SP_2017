package com.annapol04.munchkin.engine

@Throws(IllegalEngineStateException::class)
fun test(expr: Boolean, message: String) {
    if (!expr)
        throw IllegalEngineStateException(message)
}
