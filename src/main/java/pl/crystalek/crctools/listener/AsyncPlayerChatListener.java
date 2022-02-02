package pl.crystalek.crctools.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.UserCache;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class AsyncPlayerChatListener implements Listener {
    Config config;
    UserCache userCache;

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        if (!config.isAntiAfk() || !config.isCancelAfkWhenSendMessage() || event.getPlayer().hasPermission("crc.tools.afk.bypass")) {
            return;
        }

        userCache.getUser(event.getPlayer()).resetAfk();
    }
}
