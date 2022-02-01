package pl.crystalek.crctools.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.UserCache;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PlayerPickupItemListener implements Listener {
    Config config;
    UserCache userCache;

    @EventHandler
    public void onPickup(final PlayerPickupItemEvent event) {
        if (!config.isAntiAfk() || config.isPickupItemsWhileAfk()) {
            return;
        }

        if (userCache.getUser(event.getPlayer()).isAfk()) {
            event.setCancelled(true);
        }
    }
}
