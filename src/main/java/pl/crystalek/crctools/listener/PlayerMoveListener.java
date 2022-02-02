package pl.crystalek.crctools.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.UserCache;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PlayerMoveListener implements Listener {
    Config config;
    UserCache userCache;

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if (!config.isAntiAfk() || !config.isCancelAfkWhenMove() || event.getPlayer().hasPermission("crc.tools.afk.bypass")) {
            return;
        }

        final Location to = event.getTo();
        final Location from = event.getFrom();
        final double xTo = to.getX();
        final double yTo = to.getY();
        final double zTo = to.getZ();
        final double xFrom = from.getX();
        final double yFrom = from.getY();
        final double zFrom = from.getZ();

        if ((xTo == xFrom) && (zTo == zFrom)) {
            if (!config.isCancelAfkWhenMoveY()) {
                return;
            }

            if (yTo == yFrom) {
                return;
            }
        }

        userCache.getUser(event.getPlayer()).resetAfk();
    }
}
