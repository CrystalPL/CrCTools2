package pl.crystalek.crctools.listener;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.UserCache;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PlayerQuitListener implements Listener {
    Config config;
    UserCache userCache;
    MessageAPI messageAPI;

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        if (config.isQuitMessage()) {
            event.setQuitMessage("");
            messageAPI.broadcast("quitMessage", ImmutableMap.of("{PLAYER_NAME}", event.getPlayer().getName()));
        }

        userCache.removeUser(event.getPlayer().getUniqueId());
    }
}
