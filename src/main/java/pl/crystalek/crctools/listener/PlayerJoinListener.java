package pl.crystalek.crctools.listener;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.User;
import pl.crystalek.crctools.user.UserCache;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PlayerJoinListener implements Listener {
    Config config;
    UserCache userCache;
    MessageAPI messageAPI;

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (config.isJoinMessage()) {
            event.setJoinMessage("");
            messageAPI.broadcast("joinMessage", ImmutableMap.of("{PLAYER_NAME}", event.getPlayer().getName()));
        }

        if (config.isMotdMessage()) {
            messageAPI.sendMessage("motdMessage", event.getPlayer(), ImmutableMap.of("{PLAYER_NAME}", event.getPlayer().getName()));
        }

        userCache.addUser(event.getPlayer().getUniqueId(), new User());
    }
}
