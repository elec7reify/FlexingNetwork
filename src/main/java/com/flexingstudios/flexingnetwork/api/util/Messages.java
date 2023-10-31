package com.flexingstudios.flexingnetwork.api.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum Messages implements IMessages {
    CHAT_FORMAT("chat.format",
            Component.text("{rank} {player}: {message}")
                    .hoverEvent(HoverEvent.showText(Component.text("тест")))),
    COMMAND_GAMEMODE_USAGE("command.gamemode.usage",
            Component.text("Использование: /{command} <режим>", NamedTextColor.RED)),
    COMMAND_GAMEMODE_SUCCESSFUL("command.gamemode.successful",
            Component.text(">", NamedTextColor.AQUA)
                    .append(Component.text("Ваш режим игры изменён на ", NamedTextColor.WHITE))
                    .append(Component.text("{name}", NamedTextColor.AQUA))),
    COMMAND_GAMEMODE_ERROR_IDENTICAL("command.gamemode.error.identical",
            Component.text("Ваш режим игры уже является {mode}", NamedTextColor.RED))
    ;

    private final String node;
    private final Component message;

    @Override
    public @NotNull Component toComponent() {
        return message;
    }
}
