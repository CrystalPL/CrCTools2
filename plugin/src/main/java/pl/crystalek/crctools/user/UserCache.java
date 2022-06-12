package pl.crystalek.crctools.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.user.model.User;
import pl.crystalek.crctools.user.model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class UserCache {
    Map<UUID, User> userMap = new HashMap<>();
    Map<UUID, UserData> userDataByUUIDCache = new HashMap<>();
    Map<String, UserData> userDataByNicknameCache = new HashMap<>();

    public void addUser(final Player player, final User user) {
        userMap.put(player.getUniqueId(), user);
        userDataByUUIDCache.put(player.getUniqueId(), user);
        userDataByNicknameCache.put(player.getName(), user);
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

    public Optional<UserData> getUserData(final UUID playerUUID) {
        final User user = getUser(playerUUID);
        if (user != null) {
            return Optional.of(user);
        }

        return Optional.ofNullable(userDataByUUIDCache.get(playerUUID));
    }

    public Optional<UserData> getUserData(final String playerName) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            final User user = getUser(player);
            if (user != null) {
                return Optional.of(user);
            }
        }


        return Optional.ofNullable(userDataByNicknameCache.get(playerName));
    }
}
