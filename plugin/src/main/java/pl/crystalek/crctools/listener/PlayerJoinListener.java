package pl.crystalek.crctools.listener;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.UserCache;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PlayerJoinListener implements Listener {
    Config config;
    UserCache userCache;
    MessageAPI messageAPI;
    JavaPlugin plugin;

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (config.isJoinMessage()) {
            event.setJoinMessage("");
            messageAPI.broadcast("joinMessage", ImmutableMap.of("{PLAYER_NAME}", event.getPlayer().getName()));
        }

        if (config.isMotdMessage()) {
            messageAPI.sendMessage("motdMessage", event.getPlayer(), ImmutableMap.of("{PLAYER_NAME}", event.getPlayer().getName()));
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> userCache.createUser(event.getPlayer()));
    }
}
