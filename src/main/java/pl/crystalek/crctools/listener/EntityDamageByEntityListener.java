package pl.crystalek.crctools.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.User;
import pl.crystalek.crctools.user.UserCache;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class EntityDamageByEntityListener implements Listener {
    Config config;
    UserCache userCache;

    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (!config.isAntiAfk() || config.isAttackEntityWhileAfk()) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        final User user = userCache.getUser(event.getDamager().getUniqueId());
        if (user.isAfk()) {
            event.setCancelled(true);
        }
    }
}
