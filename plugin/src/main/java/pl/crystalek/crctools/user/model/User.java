package pl.crystalek.crctools.user.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.crystalek.crctools.tpa.TpaRequest;

import java.lang.ref.WeakReference;
import java.util.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends UserData {
    final List<UUID> tpaBlockPlayerList = new ArrayList<>();
    final Map<UUID, Long> teleportCooldownMap;
    final Map<UUID, TpaRequest> sentRequestMap = new HashMap<>();
    final Map<UUID, TpaRequest> receivedRequestMap = new HashMap<>();
    long afkStartTime;
    long lastCommandUsedTime;
    boolean afk;
    boolean tpaAuto;
    boolean tptoggle;
    WeakReference<Player> playerReference;

    public User(final Player player, final double money) {
        super(player.getUniqueId(), player.getName(), money);

        this.playerReference = new WeakReference<>(player);
        this.teleportCooldownMap =;
    }

    public void resetAfk() {
        this.afkStartTime = System.currentTimeMillis();
        this.afk = false;
    }

    public long getTeleportCooldown(final UUID playerUUID) {
        if (!teleportCooldownMap.containsKey(playerUUID)) {
            return 0;
        }

        return teleportCooldownMap.get(playerUUID);
    }

    public Player getPlayer() {
        Player player = playerReference.get();
        if (player == null) {
            player = Bukkit.getPlayer(uuid);
            playerReference = new WeakReference<>(player);
        }

        return player;
    }
}
