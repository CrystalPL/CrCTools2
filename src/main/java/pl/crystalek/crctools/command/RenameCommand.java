package pl.crystalek.crctools.command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.crystalek.crcapi.command.impl.SingleCommand;
import pl.crystalek.crcapi.command.model.CommandData;
import pl.crystalek.crcapi.core.util.ColorUtil;
import pl.crystalek.crcapi.message.api.MessageAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class RenameCommand extends SingleCommand {

    public RenameCommand(final MessageAPI messageAPI, final Map<Class<? extends SingleCommand>, CommandData> commandDataMap) {
        super(messageAPI, commandDataMap);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Player player = (Player) sender;
        final ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR) {
            messageAPI.sendMessage("renameCommand.emptyHand", sender);
            return;
        }

        final String newItemName = String.join(" ", args);

        final ItemMeta itemMeta = itemInHand.getItemMeta();
        itemMeta.setDisplayName(ColorUtil.color(newItemName));
        itemInHand.setItemMeta(itemMeta);

        messageAPI.sendMessage("renameCommand.usage", sender);
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "crc.tools.rename";
    }

    @Override
    public boolean isUseConsole() {
        return false;
    }

    @Override
    public String getCommandUsagePath() {
        return "renameCommand.usage";
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
