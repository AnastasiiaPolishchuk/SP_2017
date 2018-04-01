package com.annapol04.munchkin.data;

import android.util.Log;

import com.annapol04.munchkin.AppExecutors;
import com.annapol04.munchkin.db.EventDao;
import com.annapol04.munchkin.engine.Decoder;
import com.annapol04.munchkin.engine.Event;
import com.annapol04.munchkin.engine.HashUtil;
import com.annapol04.munchkin.engine.PlayClient;

import java.util.Collection;
import java.util.List;

import javax.inject.Singleton;
import javax.inject.Inject;

@Singleton
public class EventRepository implements PlayClient.OnMessageReceivedListener {
    private static final String TAG = EventRepository.class.getSimpleName();

    private AppExecutors executors;
    private EventDao dao;
    private PlayClient client;
    private Decoder decoder;
    private byte[] topHash = new byte[] {
            (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8,
            (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16
    };

    private byte[] pushTopHash = topHash;

    public interface OnNewEventListener {
        void onNewEvent(Event event);
    }

    private OnNewEventListener listener;

    @Inject
    public EventRepository(AppExecutors executors, EventDao dao, PlayClient client, Decoder decoder) {
        this.executors = executors;
        this.dao = dao;
        this.client = client;
        this.decoder = decoder;

        this.client.setMessageReceivedListener(this);
    }

    public byte[] getTopHash() {
        return topHash;
    }

    public void setTopHash(byte[] topHash) {
        this.topHash = topHash;
    }

    public void push(Collection<Event> events) {
        for (Event event : events) {
            event.setPreviousHash(pushTopHash);
            pushTopHash = event.getHash();

            Log.d(TAG, "pushig event: " + event);

            client.sendToAll(event.getBytes());
        }
    }

    public void push(Event... events) {
        for (Event event : events) {
            event.setPreviousHash(pushTopHash);
            pushTopHash = event.getHash();

            Log.d(TAG, "pushig event: " + event);

            client.sendToAll(event.getBytes());
        }
    }

    public void setNewEventListener(OnNewEventListener listener) {
        this.listener = listener;
    }

    public void newEvent(Event event) {
        if (listener != null)
            listener.onNewEvent(event);
    }

    @Override
    public void onMessageReceived(byte[] data) {
        final List<Event> received = decoder.decode(data, 0, data.length);

        for (Event event : received) {
            executors.diskIO().execute(() -> {
                dao.insert(event);
            });

            newEvent(event);
        }
    }
}
