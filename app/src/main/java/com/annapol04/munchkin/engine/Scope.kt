package com.annapol04.munchkin.engine

enum class Scope {
    GAME,
    PLAYER1,
    PLAYER2,
    PLAYER3,
    PLAYER4,
    PLAYER5,
    PLAYER6;


    companion object {
        private val lookup = arrayOf(GAME, PLAYER1, PLAYER2, PLAYER3, PLAYER4, PLAYER5, PLAYER6)

        fun fromId(id: Int): Scope {
            if (id >= lookup.size)
                throw IllegalArgumentException("Invalid event scope id $id")

            return lookup[id]
        }
    }
}
