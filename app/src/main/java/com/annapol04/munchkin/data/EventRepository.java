package com.annapol04.munchkin.data;

import android.util.Log;

import com.annapol04.munchkin.AppExecutors;
import com.annapol04.munchkin.db.EventDao;
import com.annapol04.munchkin.engine.Decoder;
import com.annapol04.munchkin.engine.Event;
import com.annapol04.munchkin.engine.HashUtil;
import com.annapol04.munchkin.engine.PlayClient;

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
    private String topHash = HashUtil.applySha256("");

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

    public String getTopHash() {
        return topHash;
    }

    public void setTopHash(String topHash) {
        this.topHash = topHash;
    }

    public void push(Event... events) {
        String top = topHash;

        for (Event event : events) {
            event.setPreviousHash(top);
            top = event.getHash();

            Log.d(TAG, "pushig event: " + event);

            executors.diskIO().execute(() -> {
                dao.insert(event);
            });
            client.sendToAll(event.getBytes());
            newEvent(event);
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

        for (Event event : received)
            newEvent(event);
    }
}
