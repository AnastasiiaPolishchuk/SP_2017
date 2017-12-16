package com.annapol04.munchkin.engine;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static junit.framework.Assert.assertTrue;

public class EventTest {
    @Test
    public void serializeEventWithEmptyData() {
        Event event = new Event(Scope.PLAYER2, Action.NOTHING, 3);

        byte[] b = new byte[]{
                (byte)2, // player 2 scope
                (byte)0,(byte)0,(byte)0,(byte)1, // action id JOING_PLAYER
                (byte)0,(byte)0,(byte)0,(byte)3, // message id 3
                (byte)0, // data type empty
        };

        assertTrue(Arrays.equals(b, event.getBytes()));
    }

    @Test
    public void serializeEventWithStringData() throws IOException {
        Event event = new Event(Scope.PLAYER2, Action.JOIN_PLAYER, 3, "Hello World!");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{
                (byte)2, // player 2 scope
                (byte)0,(byte)0,(byte)0,(byte)1, // action id JOING_PLAYER
                (byte)0,(byte)0,(byte)0,(byte)3, // message id 3
                (byte)2, // data type STRING
        });
        outputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        outputStream.write(new byte[] { (byte) 0 });
        byte b[] = outputStream.toByteArray();

        assertTrue(Arrays.equals(b, event.getBytes()));
    }

    @Test
    public void serializeEventWithIntegerData() {
        Event event = new Event(Scope.PLAYER2, Action.JOIN_PLAYER, 3, 16909060);

        byte[] b = new byte[]{
                (byte)2, // player 2 scope
                (byte)0,(byte)0,(byte)0,(byte)1, // action id JOIN_PLAYER
                (byte)0,(byte)0,(byte)0,(byte)3, // message id 3
                (byte)1, // data type INTEGER
                (byte)1,(byte)2,(byte)3,(byte)4, // ‭16909060‬
        };

        assertTrue(Arrays.equals(b, event.getBytes()));
    }
}
