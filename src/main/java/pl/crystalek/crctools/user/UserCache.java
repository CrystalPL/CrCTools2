package pl.crystalek.crctools.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class UserCache {
    Map<UUID, User> userMap = new HashMap<>();

    public void addUser(final UUID playerUUID, final User user) {
        userMap.put(playerUUID, user);
    }

    public void removeUser(final UUID playerUUID) {
        userMap.remove(playerUUID);
    }

    public User getUser(final Player player) {
        return userMap.get(player.getUniqueId());
    }

    public User getUser(final UUID playerUUID) {
        return userMap.get(playerUUID);
    }
}
