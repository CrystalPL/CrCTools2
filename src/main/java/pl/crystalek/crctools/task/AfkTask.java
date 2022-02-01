package pl.crystalek.crctools.task;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crctools.afk.IAfkPunishment;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.User;
import pl.crystalek.crctools.user.UserCache;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class AfkTask implements Runnable {
    UserCache userCache;
    Config config;
    JavaPlugin plugin;

    @Override
    public void run() {
        for (final Map.Entry<UUID, User> userEntry : userCache.getUserMap().entrySet()) {
            final User user = userEntry.getValue();
            if (!user.isAfk()) {
                if (System.currentTimeMillis() - user.getAfkStartTime() < config.getAfkTime()) {
                    continue;
                }

                user.setAfk(true);
            }

            final long afkTime = System.currentTimeMillis() - user.getAfkStartTime();
            for (final Map.Entry<Long, IAfkPunishment> entry : config.getAfkActionTypeMap().entrySet()) {
                final long time = afkTime - entry.getKey();
                if (time >= 0 && time <= 1000) {
                    Bukkit.getScheduler().runTask(plugin, () -> entry.getValue().execute(Bukkit.getPlayer(userEntry.getKey())));
                    break;
                }
            }
        }
    }
}
