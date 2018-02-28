package com.annapol04.munchkin.engine;

public class EngineTest {
    public static void test(boolean expr, String message) throws IllegalEngineStateException {
        if (!expr)
            throw new IllegalEngineStateException(message);
    }
}
