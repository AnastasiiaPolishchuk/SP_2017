package com.annapol04.munchkin.engine

enum class DataType {
    EMPTY,
    INTEGER,
    STRING;

    companion object {
        private val lookup = arrayOf(EMPTY, INTEGER, STRING)

        fun fromId(id: Int): DataType {
            if (id >= lookup.size)
                throw IllegalArgumentException("Invalid event data type id $id")

            return lookup[id]
        }
    }
}