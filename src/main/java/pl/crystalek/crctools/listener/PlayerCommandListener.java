package pl.crystalek.crctools.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.crystalek.crcapi.command.CommandRegistry;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.UserCache;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PlayerCommandListener implements Listener {
    Config config;
    MessageAPI messageAPI;
    UserCache userCache;

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        if (config.isAntiAfk() && config.isCancelAfkWhenUseCommand()) {
            userCache.getUser(event.getPlayer()).resetAfk();
        }

        final String commandName = event.getMessage().toLowerCase().split(" ")[0].substring(1);
        if (!config.getBlockedCommandList().isEmpty() && config.getBlockedCommandList().contains(commandName)) {
            messageAPI.sendMessage("commandNotFound", event.getPlayer());
            event.setCancelled(true);
            return;
        }

        if (!config.isCommandNotFound()) {
            return;
        }

        if (CommandRegistry.getCommandMap().getCommand(commandName) == null) {
            messageAPI.sendMessage("commandNotFound", event.getPlayer());
            event.setCancelled(true);
        }
    }
}
