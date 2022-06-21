package pl.crystalek.crctools.command;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.crystalek.crcapi.command.impl.Command;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.core.util.ColorUtil;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class LoreCommand extends SingleCommand {

    public LoreCommand(final MessageAPI messageAPI, final Map<Class<? extends Command>, CommandData> commandDataMap) {
        super(messageAPI, commandDataMap);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Player player = (Player) sender;
        final ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            messageAPI.sendMessage("loreCommand.emptyHand", sender);
            return;
        }

        final List<String> newLore = ColorUtil.color(Arrays.asList(StringUtils.split(String.join(" ", args), "||")));

        final ItemMeta itemMeta = itemInHand.getItemMeta();
        itemMeta.setLore(newLore);
        itemInHand.setItemMeta(itemMeta);

        messageAPI.sendMessage("loreCommand.lore", sender, ImmutableMap.of("{LORE}", String.join("\n", newLore)));
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "crc.tools.lore";
    }

    @Override
    public boolean isUseConsole() {
        return false;
    }

    @Override
    public String getCommandUsagePath() {
        return "loreCommand.usage";
    }

    @Override
    public int maxArgumentLength() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int minArgumentLength() {
        return 1;
    }
}
