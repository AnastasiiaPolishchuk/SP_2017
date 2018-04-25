package com.annapol04.munchkin.engine

open class MatchResult { }
class MatchFinishedResult(val name: String, val level: Int) : MatchResult()
class MatchAbortedResult(val reason: String): MatchResult()