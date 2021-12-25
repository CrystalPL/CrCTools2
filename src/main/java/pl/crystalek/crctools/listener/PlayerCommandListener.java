package pl.crystalek.crctools.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.crystalek.crcapi.command.CommandRegistry;
import pl.crystalek.crcapi.message.MessageAPI;
import pl.crystalek.crctools.config.Config;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PlayerCommandListener implements Listener {
    Config config;
    MessageAPI messageAPI;

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final String commandName = event.getMessage().toLowerCase().split(" ")[0].substring(1);
        if (config.getBlockedCommandList().contains(commandName) || CommandRegistry.getCommandMap().getCommand(commandName) == null) {
            messageAPI.sendMessage("commandNotFound", event.getPlayer());
            event.setCancelled(true);
        }
    }
}
