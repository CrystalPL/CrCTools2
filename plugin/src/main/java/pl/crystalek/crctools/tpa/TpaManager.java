package pl.crystalek.crctools.tpa;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.hook.VaultHook;
import pl.crystalek.crctools.user.model.User;

import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class TpaManager {
    Config config;
    JavaPlugin plugin;
    MessageAPI messageAPI;

    public void sendRequest(final User userSendingRequest, final User userReceivingRequest) {
        final Player playerSendingRequest = userSendingRequest.getPlayer();
        final Player playerReceivingRequest = userReceivingRequest.getPlayer();
        final TpaRequest tpaRequest;
        if (userReceivingRequest.isTpaAuto()) {
            messageAPI.sendMessage("tpAuto.teleportAutoAccept", playerReceivingRequest, ImmutableMap.of("{PLAYER_NAME}", userSendingRequest.getNickname()));
            messageAPI.sendMessage("teleport.accept", playerSendingRequest, ImmutableMap.of("{PLAYER_NAME}", userReceivingRequest.getNickname()));

            if (config.isInstantTeleport()) {
                playerSendingRequest.teleport(playerReceivingRequest.getLocation());
                messageAPI.sendMessage("teleport.teleport", playerSendingRequest, ImmutableMap.of("{PLAYER_NAME}", userReceivingRequest.getNickname()));
                return;
            }

            //start teleport timer
            return;
        }

        long teleportTime = System.currentTimeMillis();
        if (VaultHook.isEnableVault()) {
            final Map<String, Long> teleportRankTime = config.getTeleportRankTime();

            final String primaryGroup = VaultHook.getPermission().getPrimaryGroup(playerSendingRequest);
            teleportTime += teleportRankTime.containsKey(primaryGroup) ? teleportRankTime.get(primaryGroup) : config.getDefaultTeleportTime();
        } else {
            teleportTime += config.getDefaultTeleportTime();
        }
        //start expired time timer

        final Location teleportLocation = config.isTeleportToCurrentLocation() ? null : playerReceivingRequest.getLocation();
    }

    public void cancelTeleport(final TpaRequest tpaRequest) {
        final User userSendingRequest = tpaRequest.getUserSendingRequest();
        final User userReceivingRequest = tpaRequest.getUserReceivingRequest();


    }

    public void startTeleportTimer(final TpaRequest tpaRequest) {

    }
}
