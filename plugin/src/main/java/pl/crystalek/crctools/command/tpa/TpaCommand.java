package pl.crystalek.crctools.command.tpa;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.command.impl.Command;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.core.time.TimeUtil;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.tpa.TpaRequest;
import pl.crystalek.crctools.user.UserCache;
import pl.crystalek.crctools.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class TpaCommand extends SingleCommand {
    UserCache userCache;
    Config config;

    public TpaCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap, final UserCache userCache, final Config config) {
        super(messageAPI, commandDataMap);

        this.userCache = userCache;
        this.config = config;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            messageAPI.sendMessage("playerNotFound", sender);
            return;
        }

        final User targetUser = userCache.getUser(targetPlayer);
        if (!targetUser.isTptoggle()) {
            messageAPI.sendMessage("tpa.tptoggle", sender, ImmutableMap.of("{PLAYER_NAME}", args[0]));
            return;
        }

        final Player playerSender = (Player) sender;
        if (targetUser.getTpaBlockPlayerList().contains(playerSender.getUniqueId())) {
            messageAPI.sendMessage("tpa.block", sender, ImmutableMap.of("{PLAYER_NAME}", args[0]));
            return;
        }

        final User senderUser = userCache.getUser(playerSender);
        if (senderUser.getSentRequestMap().containsKey(targetPlayer.getUniqueId())) {
            messageAPI.sendMessage("tpa.alreadySentRequest", sender);
            return;
        }

        final long teleportCooldown = senderUser.getTeleportCooldown(targetPlayer.getUniqueId());
        if (teleportCooldown > System.currentTimeMillis()) {
            final Map<String, Object> replacements = ImmutableMap.of(
                    "{PLAYER_NAME}", args[0],
                    "{TIME}", TimeUtil.getDateInString(System.currentTimeMillis() - teleportCooldown, config.getTimeDelimiter(), config.isShortFormTime())
            );

            messageAPI.sendMessage("tpa.cooldown", sender, replacements);
            return;
        }

        final boolean teleportToCurrentLocation = config.isTeleportToCurrentLocation();
        new TpaRequest(senderUser, targetUser, config.isTeleportToCurrentLocation() ? null : targetPlayer.getLocation(), )
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(player -> StringUtils.startsWithIgnoreCase(player, args[0])).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "crc.tools.tpa";
    }

    @Override
    public boolean isUseConsole() {
        return false;
    }

    @Override
    public String getCommandUsagePath() {
        return "tpa.usage";
    }

    @Override
    public int maxArgumentLength() {
        return 1;
    }

    @Override
    public int minArgumentLength() {
        return 1;
    }
}
