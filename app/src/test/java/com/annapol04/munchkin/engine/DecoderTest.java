package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.engine.Action;
import com.annapol04.munchkin.engine.DataType;
import com.annapol04.munchkin.engine.Decoder;
import com.annapol04.munchkin.engine.Event;
import com.annapol04.munchkin.engine.Scope;

import junit.framework.Assert;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static junit.framework.Assert.assertEquals;


public class DecoderTest {
    @Test
    public void decodeEventWithEmptyData() {
        byte[] b = new byte[]{
                (byte)2, // player 2 scope
                (byte)0,(byte)0,(byte)0,(byte)1, // action id nothing
                (byte)0,(byte)0,(byte)0,(byte)3, // message id empty
                (byte)0, // data type empty
        };

        Decoder decoder = new Decoder();

        List<Event> events = decoder.decode(b, 0, b.length);

        assertEquals(1, events.size());

        Event event = events.get(0);
        Assert.assertEquals(Scope.PLAYER2, event.getScope());
        Assert.assertEquals(Action.JOIN_PLAYER, event.getAction());
        assertEquals(3, event.getMessageId());
        Assert.assertEquals(DataType.EMPTY, event.getDataType());
    }

    @Test
    public void decodeEventWithIntegerData() {
        byte[] b = new byte[]{
                (byte)2, // player 2 scope
                (byte)0,(byte)0,(byte)0,(byte)1, // action id nothing
                (byte)0,(byte)0,(byte)0,(byte)3, // message id empty
                (byte)1, // data type empty
                (byte)1,(byte)2,(byte)3,(byte)4, // ‭16909060‬
        };

        Decoder decoder = new Decoder();

        List<Event> events = decoder.decode(b, 0, b.length);

        assertEquals(1, events.size());

        Event event = events.get(0);
        assertEquals(Scope.PLAYER2, event.getScope());
        assertEquals(Action.JOIN_PLAYER, event.getAction());
        assertEquals(3, event.getMessageId());
        assertEquals(DataType.INTEGER, event.getDataType());
        assertEquals(16909060, event.getInteger());
    }

    @Test
    public void decodeEventWithStringData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{
                (byte)2, // player 2 scope
                (byte)0,(byte)0,(byte)0,(byte)1, // action id nothing
                (byte)0,(byte)0,(byte)0,(byte)3, // message id empty
                (byte)2, // data type empty
        });
        outputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        outputStream.write(new byte[] { (byte) 0 });
        byte b[] = outputStream.toByteArray();

        Decoder decoder = new Decoder();

        List<Event> events = decoder.decode(b, 0, b.length);

        assertEquals(1, events.size());

        Event event = events.get(0);
        assertEquals(Scope.PLAYER2, event.getScope());
        assertEquals(Action.JOIN_PLAYER, event.getAction());
        assertEquals(3, event.getMessageId());
        assertEquals(DataType.STRING, event.getDataType());
        assertEquals("Hello World!", event.getString());
    }

    @Test
    public void decodeMultipleEvents() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{
                (byte)2, // player 2 scope
                (byte)0,(byte)0,(byte)0,(byte)1, // action id nothing
                (byte)0,(byte)0,(byte)0,(byte)3, // message id empty
                (byte)2, // data type empty
        });
        outputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        outputStream.write(new byte[] { (byte) 0 });
        outputStream.write(new byte[]{
                (byte)2, // player 2 scope
                (byte)0,(byte)0,(byte)0,(byte)1, // action id nothing
                (byte)0,(byte)0,(byte)0,(byte)3, // message id empty
                (byte)2, // data type empty
        });
        outputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        outputStream.write(new byte[] { (byte) 0 });
        byte b[] = outputStream.toByteArray();

        Decoder decoder = new Decoder();

        List<Event> events = decoder.decode(b, 0, b.length);

        assertEquals(2, events.size());
    }
}
