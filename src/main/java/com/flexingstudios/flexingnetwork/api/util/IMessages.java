package com.flexingstudios.flexingnetwork.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public interface IMessages {
    /**
     * Returns the message with replaced placeholders.
     *
     * @param message      The message string to replace placeholders in
     * @param placeholders The map of placeholders and their corresponding values
     * @return The message with replaced placeholders
     */
    default String toMessage(@NotNull String message, @NotNull Map<String, Object> placeholders) {
        String baseMessage = message;

        for (Map.Entry<String, Object> placeholder : placeholders.entrySet()) {
            final String toReplace = Matcher.quoteReplacement(placeholder.getKey());

            String parsedPlaceholder;

            if (placeholder.getValue() instanceof Component) {
                parsedPlaceholder = LegacyComponentSerializer.legacySection().serialize((Component) placeholder.getValue());
            } else {
                parsedPlaceholder = placeholder.getValue().toString();
            }
            baseMessage = baseMessage.replaceAll("\\{" + toReplace + "}", parsedPlaceholder);
        }

        return baseMessage;
    }

    /**
     * Returns the message with replaced placeholders.
     *
     * @param placeholders The map of placeholders and their corresponding values
     * @return The message with replaced placeholders
     */
    default String toMessage(@NotNull Map<String, Object> placeholders) {
        return toMessage(LegacyComponentSerializer.legacySection().serialize(toComponent()), placeholders);
    }

    /**
     * Returns this legacy Component content.
     *
     * @return The Legacy string
     */
    default String toMessage() {
        return toMessage(new HashMap<>());
    }

    default Component toComponent(@NotNull String message, @NotNull Map<String, Object> placeholders) {
        return LegacyComponentSerializer.legacySection().deserialize(toMessage(message, placeholders));
    }

    default Component toComponent(@NotNull Map<String, Object> placeholders) {
        return toComponent(LegacyComponentSerializer.legacySection().serialize(toComponent()), placeholders);
    }

    /**
     * Returns this Component without placeholders.
     *
     * @return The Component
     */
    @NotNull Component toComponent();
}
