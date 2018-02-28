package com.annapol04.munchkin.engine;

import android.app.Application;
import android.support.annotation.StringRes;

import com.annapol04.munchkin.R;

import javax.inject.Inject;

public class MessageBook {

    private final Application application;

    @Inject
    public MessageBook(Application application) {
        this.application = application;
    }

    public String find(int id) {
        return application.getString(id);
    }

    public String build(Event event) {
        return build(event, null, false);
    }

    public String build(Event event, Player player, boolean anonymized) {
        int id = event.getMessageId();

        if (id == 0)
            return "";

        return replace(find(id), event, player, anonymized);
    }

    private String replace(String message, Event event, Player player, boolean anonymized) {
        if (message.contains("%scope%"))
            message = message.replace("%scope%", event.getScope().toString());

        if (player != null && message.contains("%player%"))
            message = message.replace("%player%", player.getName());

        if (message.contains("%card%")) {
            if (anonymized)
                message = message.replace("%card%", find(R.string.anonymized_card));
            else
                message = message.replace("%card%", find(Card.fromId(event.getInteger()).getName()));
        }

        if (message.contains("%integer%"))
            message = message.replace("%integer%", Integer.toString(event.getInteger()));

        if (message.contains("%string%"))
            message = message.replace("%string%", event.getString());

        if (message.contains("%turn-phase%"))
            message = message.replace("%turn-phase%", find(TurnPhase.fromId(event.getInteger()).getStringId()));

        return message;
    }
}
