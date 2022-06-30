package pl.crystalek.crctools.economy;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crctools.api.economy.Economy;
import pl.crystalek.crctools.api.exception.EconomyException;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.user.UserCache;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
class VaultEconomyImpl implements net.milkbowl.vault.economy.Economy {
    Config config;
    UserCache userCache;
    JavaPlugin plugin;
    Economy economy;

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return "CrCTools";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(final double amount) {
        return config.getNumberFormatter().format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return config.getCurrencySymbol();
    }

    @Override
    public String currencyNameSingular() {
        return config.getCurrencySymbol();
    }

    @Override
    public boolean hasAccount(final String playerName) {
        return userCache.getUserData(playerName).isPresent();
    }

    @Override
    public boolean hasAccount(@NonNull final OfflinePlayer player) {
        return userCache.getUserData(player.getUniqueId()).isPresent();
    }

    @Override
    public boolean hasAccount(final String playerName, final String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(final OfflinePlayer player, final String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(final String playerName) {
        try {
            return economy.getBalance(playerName);
        } catch (final EconomyException exception) {
            return 0;
        }
    }

    @Override
    public double getBalance(final OfflinePlayer player) {
        try {
            return economy.getBalance(player);
        } catch (final EconomyException exception) {
            return 0;
        }
    }

    @Override
    public double getBalance(final String playerName, final String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(final OfflinePlayer player, final String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(final String playerName, final double amount) {
        try {
            return economy.hasMoney(playerName, amount);
        } catch (final EconomyException exception) {
            return false;
        }
    }

    @Override
    public boolean has(final OfflinePlayer player, final double amount) {
        try {
            return economy.hasMoney(player, amount);
        } catch (final EconomyException exception) {
            return false;
        }
    }

    @Override
    public boolean has(final String playerName, final String worldName, final double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(final OfflinePlayer player, final String worldName, final double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(final String playerName, final double amount) {
        EconomyResponse.ResponseType economyResponse;
        String errorMessage;
        try {
            economy.takeMoney(playerName, amount);
            economyResponse = EconomyResponse.ResponseType.SUCCESS;
            errorMessage = null;
        } catch (final EconomyException exception) {
            economyResponse = EconomyResponse.ResponseType.FAILURE;
            errorMessage = exception.getMessage();
        }

        return new EconomyResponse(0, 0, economyResponse, errorMessage);
    }

    @Override
    public EconomyResponse withdrawPlayer(final OfflinePlayer player, final double amount) {
        EconomyResponse.ResponseType economyResponse;
        String errorMessage;
        try {
            economy.takeMoney(player, amount);
            economyResponse = EconomyResponse.ResponseType.SUCCESS;
            errorMessage = null;
        } catch (final EconomyException exception) {
            economyResponse = EconomyResponse.ResponseType.FAILURE;
            errorMessage = exception.getMessage();
        }

        return new EconomyResponse(0, 0, economyResponse, errorMessage);
    }

    @Override
    public EconomyResponse withdrawPlayer(final String playerName, final String worldName, final double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(final OfflinePlayer player, final String worldName, final double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(final String playerName, final double amount) {
        EconomyResponse.ResponseType economyResponse;
        String errorMessage;
        try {
            economy.giveMoney(playerName, amount);
            economyResponse = EconomyResponse.ResponseType.SUCCESS;
            errorMessage = null;
        } catch (final EconomyException exception) {
            economyResponse = EconomyResponse.ResponseType.FAILURE;
            errorMessage = exception.getMessage();
        }

        return new EconomyResponse(0, 0, economyResponse, errorMessage);
    }

    @Override
    public EconomyResponse depositPlayer(final OfflinePlayer player, final double amount) {
        EconomyResponse.ResponseType economyResponse;
        String errorMessage;
        try {
            economy.giveMoney(player, amount);
            economyResponse = EconomyResponse.ResponseType.SUCCESS;
            errorMessage = null;
        } catch (final EconomyException exception) {
            economyResponse = EconomyResponse.ResponseType.FAILURE;
            errorMessage = exception.getMessage();
        }

        return new EconomyResponse(0, 0, economyResponse, errorMessage);
    }

    @Override
    public EconomyResponse depositPlayer(final String playerName, final String worldName, final double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(final OfflinePlayer player, final String worldName, final double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(final String name, final String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CrCTools does not support bank accounts!");
    }

    @Override
    public EconomyResponse createBank(final String name, final OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CrCTools does not support bank accounts!");
    }

    @Override
    public EconomyResponse deleteBank(final String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CrCTools does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankBalance(final String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CrCTools does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankHas(final String name, final double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CrCTools does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankWithdraw(final String name, final double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CrCTools does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankDeposit(final String name, final double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CrCTools does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(final String name, final String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CrCTools does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(final String name, final OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CrCTools does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(final String name, final String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CrCTools does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(final String name, final OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "CrCTools does not support bank accounts!");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    //plugin does not support
    @Override
    public boolean createPlayerAccount(final String playerName) {
        return false;
    }

    //plugin does not support
    @Override
    public boolean createPlayerAccount(final OfflinePlayer player) {
        return false;
    }

    //plugin does not support
    @Override
    public boolean createPlayerAccount(final String playerName, final String worldName) {
        return false;
    }

    //plugin does not support
    @Override
    public boolean createPlayerAccount(final OfflinePlayer player, final String worldName) {
        return false;
    }
}
