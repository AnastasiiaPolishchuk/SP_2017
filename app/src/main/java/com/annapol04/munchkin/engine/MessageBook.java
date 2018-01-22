package com.annapol04.munchkin.engine;

public abstract class MessageBook {
    public abstract String find(int id);

    public String build(Event event) {
        int id = event.getMessageId();

        if (id == 0)
            return "";

        return replace(find(id), event);
    }

    private String replace(String message, Event event) {
        if (message.contains("%scope%"))
            message = message.replace("%scope%", event.getScope().toString());

        if (message.contains("%player%"))
            message = message.replace("%player%", event.getScope().toString());

        if (message.contains("%card%"))
            message = message.replace("%card%", Card.fromId(event.getInteger()).getName());

        if (message.contains("%integer%"))
            message = message.replace("%integer%", Integer.toString(event.getInteger()));

        if (message.contains("%string%"))
            message = message.replace("%string%", event.getString());

        return message;
    }
}
