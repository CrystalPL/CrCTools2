package pl.crystalek.crctools.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    long afkStartTime;
    long lastCommandUsedTime;
    boolean afk;

    public void resetAfk() {
        this.afkStartTime = System.currentTimeMillis();
        this.afk = false;
    }
}
