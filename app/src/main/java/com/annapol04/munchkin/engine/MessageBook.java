package com.annapol04.munchkin.engine;

public abstract class MessageBook {
    public abstract String find(int id);

    public String build(Event event) {
        return build(event, null);
    }

    public String build(Event event, Player player) {
        int id = event.getMessageId();

        if (id == 0)
            return "";

        return replace(find(id), event, player);
    }

    private String replace(String message, Event event, Player player) {
        if (message.contains("%scope%"))
            message = message.replace("%scope%", event.getScope().toString());

        if (player != null && message.contains("%player%"))
            message = message.replace("%player%", player.getName());

        if (message.contains("%card%"))
            message = message.replace("%card%", find(Card.fromId(event.getInteger()).getName()));

        if (message.contains("%integer%"))
            message = message.replace("%integer%", Integer.toString(event.getInteger()));

        if (message.contains("%string%"))
            message = message.replace("%string%", event.getString());

        if (message.contains("%turn-phase%"))
            message = message.replace("%turn-phase%", find(event.getInteger()));

        return message;
    }
}
