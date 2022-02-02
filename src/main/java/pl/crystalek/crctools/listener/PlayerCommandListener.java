package pl.crystalek.crctools.listener;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.crystalek.crcapi.command.CommandRegistry;
import pl.crystalek.crcapi.core.time.TimeUtil;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.User;
import pl.crystalek.crctools.user.UserCache;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PlayerCommandListener implements Listener {
    Config config;
    MessageAPI messageAPI;
    UserCache userCache;

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();

        //blocking commands
        final String commandName = event.getMessage().toLowerCase().split(" ")[0].substring(1);
        if (!config.getBlockedCommandList().isEmpty() && config.getBlockedCommandList().contains(commandName)) {
            messageAPI.sendMessage("commandNotFound", player);
            event.setCancelled(true);
            return;
        }

        //check if command exists
        if (CommandRegistry.getCommandMap().getCommand(commandName) == null) {
            if (config.isCommandNotFoundMessage()) {
                messageAPI.sendMessage("commandNotFound", player);
            }

            event.setCancelled(true);
            return;
        }

        //check antiafk
        final User user = userCache.getUser(event.getPlayer());
        if (config.isAntiAfk() && config.isCancelAfkWhenUseCommand() && !player.hasPermission("crc.tools.afk.bypass")) {
            user.resetAfk();
        }

        //check command cooldown
        if (config.isCommandCoolDown()) {
            if (player.hasPermission("crc.tools.cmdcooldown.bypass")) {
                return;
            }

            if (System.currentTimeMillis() - user.getLastCommandUsedTime() >= config.getCommandCoolDownTime()) {
                user.setLastCommandUsedTime(System.currentTimeMillis());
                return;
            }

            final String time = TimeUtil.getDateInString(
                    userCache.getUser(event.getPlayer()).getLastCommandUsedTime() + config.getCommandCoolDownTime() - System.currentTimeMillis(),
                    config.getTimeDelimiter(),
                    config.isShortFormTime()
            );

            messageAPI.sendMessage("commandCoolDown", event.getPlayer(), ImmutableMap.of("{TIME}", time));
            event.setCancelled(true);
        }
    }
}
