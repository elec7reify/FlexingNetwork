package com.flexingstudios.flexingnetwork.api.util;

import net.md_5.bungee.api.chat.*;
import org.jetbrains.annotations.NotNull;

public class ChatUtil {

    public ChatUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static BaseComponent createMessage(@NotNull String message, ClickEvent clickEvent, HoverEvent hoverEvent, String append, ComponentBuilder.FormatRetention format) {
        TextComponent element = new TextComponent(new ComponentBuilder(message)
                .event(clickEvent)
                .event(hoverEvent)
                .append(append, format)
                .create());

        return element;
    }

    public static BaseComponent createMessage(@NotNull String message) {
        return new TextComponent(new ComponentBuilder(message).create());
    }

    public static BaseComponent createMessage(@NotNull String message, ClickEvent clickEvent, HoverEvent hoverEvent) {
        TextComponent element = new TextComponent(new ComponentBuilder(message)
                .event(clickEvent)
                .event(hoverEvent)
                .create());

        return element;
    }

    public static BaseComponent createMessage(@NotNull String message, HoverEvent hoverEvent) {
        TextComponent element = new TextComponent(new ComponentBuilder(message)
                .event(hoverEvent)
                .create());

        return element;
    }
}
