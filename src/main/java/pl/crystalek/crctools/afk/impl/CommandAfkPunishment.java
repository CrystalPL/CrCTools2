package pl.crystalek.crctools.afk.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.afk.IAfkPunishment;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class CommandAfkPunishment implements IAfkPunishment {
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    List<String> commandList;

    @Override
    public void execute(final Player player) {
        commandList.forEach(command -> Bukkit.dispatchCommand(console, command.replace("{PLAYER_NAME}", player.getName())));
    }
}
