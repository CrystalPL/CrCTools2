package pl.crystalek.crctools.user.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends UserData {
    long afkStartTime;
    long lastCommandUsedTime;
    boolean afk;

    public User(final Player player, final double money) {
        super(player.getName(), player.getUniqueId(), money);
    }

    public void resetAfk() {
        this.afkStartTime = System.currentTimeMillis();
        this.afk = false;
    }
}
