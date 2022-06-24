package pl.crystalek.crctools.economy;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crctools.api.economy.Economy;
import pl.crystalek.crctools.api.economy.EconomyResult;
import pl.crystalek.crctools.api.exception.EconomyException;
import pl.crystalek.crctools.config.Config;
import pl.crystalek.crctools.storage.Provider;
import pl.crystalek.crctools.user.UserCache;
import pl.crystalek.crctools.user.model.UserData;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EconomyImpl implements Economy {
    Config config;
    UserCache userCache;
    JavaPlugin plugin;
    Provider provider;

    private double getBalance(final Optional<UserData> userDataOptional) {
        if (!userDataOptional.isPresent()) {
            throw new EconomyException("user not found", EconomyResult.USER_NOT_FOUND);
        }

        return userDataOptional.get().getMoney();
    }

    private boolean hasMoney(final Optional<UserData> userDataOptional, final double money) {
        if (!userDataOptional.isPresent()) {
            throw new EconomyException("user not found", EconomyResult.USER_NOT_FOUND);
        }

        if (money < 0) {
            throw new EconomyException("cannot check negative money", EconomyResult.NEGATIVE_AMOUNT);
        }

        return userDataOptional.get().getMoney() >= money;
    }

    private void takeMoney(final Optional<UserData> userDataOptional, final double money) {
        if (!userDataOptional.isPresent()) {
            throw new EconomyException("user not found", EconomyResult.USER_NOT_FOUND);
        }

        if (money <= 0) {
            throw new EconomyException("cannot take negative money", EconomyResult.TAKE_NEGATIVE_AMOUNT);
        }

        if (money < config.getMinTransferBalance()) {
            throw new EconomyException("minimum transfer balance is: " + config.getMinTransferBalance(), EconomyResult.MIN_TRANSFER_BALANCE);
        }

        if (money > config.getMaxTransferBalance()) {
            throw new EconomyException("maximum transfer balance is: " + config.getMaxTransferBalance(), EconomyResult.MAX_TRANSFER_BALANCE);
        }

        final UserData userData = userDataOptional.get();
        final double moneyAfterTake = userData.getMoney() - money;
        if (moneyAfterTake < 0 && !config.isMinusBalance()) {
            throw new EconomyException("user cannot have negative balance", EconomyResult.NEGATIVE_BALANCE);
        }

        if (moneyAfterTake < -config.getMaxMinusBalance()) {
            throw new EconomyException("maximum minus balance is: " + config.getMaxMinusBalance(), EconomyResult.MAX_MINUS_BALANCE);
        }

        userData.setMoney(moneyAfterTake);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> provider.setMoney(userData.getUuid(), moneyAfterTake));
    }

    private void giveMoney(final Optional<UserData> userDataOptional, final double money) {
        if (!userDataOptional.isPresent()) {
            throw new EconomyException("user not found", EconomyResult.USER_NOT_FOUND);
        }

        if (money <= 0) {
            throw new EconomyException("cannot give negative money", EconomyResult.GIVE_NEGATIVE_AMOUNT);
        }

        if (money < config.getMinTransferBalance()) {
            throw new EconomyException("minimum transfer balance is: " + config.getMinTransferBalance(), EconomyResult.MIN_TRANSFER_BALANCE);
        }

        if (money > config.getMaxTransferBalance()) {
            throw new EconomyException("maximum transfer balance is: " + config.getMaxTransferBalance(), EconomyResult.MAX_TRANSFER_BALANCE);
        }

        final UserData userData = userDataOptional.get();
        final double moneyAfterGive = userData.getMoney() + money;
        if (moneyAfterGive > config.getMaxBalance()) {
            throw new EconomyException("maximum balance is: " + config.getMaxBalance(), EconomyResult.MAX_BALANCE);
        }

        userData.setMoney(moneyAfterGive);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> provider.setMoney(userData.getUuid(), moneyAfterGive));
    }

    private void setMoney(final Optional<UserData> userDataOptional, final double money) {
        if (!userDataOptional.isPresent()) {
            throw new EconomyException("user not found", EconomyResult.USER_NOT_FOUND);
        }

        if (money < 0 && !config.isMinusBalance()) {
            throw new EconomyException("user cannot have negative balance", EconomyResult.NEGATIVE_BALANCE);
        }

        if (money < -config.getMaxMinusBalance()) {
            throw new EconomyException("maximum minus balance is: " + config.getMaxMinusBalance(), EconomyResult.MAX_MINUS_BALANCE);
        }

        if (money > config.getMaxBalance()) {
            throw new EconomyException("maximum balance is: " + config.getMaxBalance(), EconomyResult.MAX_BALANCE);
        }

        final UserData userData = userDataOptional.get();
        userData.setMoney(money);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> provider.setMoney(userData.getUuid(), money));
    }

    @Override
    public double getBalance(final @NonNull OfflinePlayer player) throws EconomyException {
        return getBalance(userCache.getUserData(player.getUniqueId()));
    }

    @Override
    public double getBalance(final @NonNull String playerName) throws EconomyException {
        return getBalance(userCache.getUserData(playerName));
    }

    @Override
    public double getBalance(final @NonNull UUID playerUUID) throws EconomyException {
        return getBalance(userCache.getUserData(playerUUID));
    }

    @Override
    public boolean hasMoney(final @NonNull OfflinePlayer player, final double money) throws EconomyException {
        return hasMoney(userCache.getUserData(player.getUniqueId()), money);
    }

    @Override
    public boolean hasMoney(final @NonNull String playerName, final double money) throws EconomyException {
        return hasMoney(userCache.getUserData(playerName), money);
    }

    @Override
    public boolean hasMoney(final @NonNull UUID playerUUID, final double money) throws EconomyException {
        return hasMoney(userCache.getUserData(playerUUID), money);
    }

    @Override
    public void takeMoney(final @NonNull OfflinePlayer player, final double money) throws EconomyException {
        takeMoney(userCache.getUserData(player.getUniqueId()), money);
    }

    @Override
    public void takeMoney(final @NonNull String playerName, final double money) throws EconomyException {
        takeMoney(userCache.getUserData(playerName), money);
    }

    @Override
    public void takeMoney(final @NonNull UUID playerUUID, final double money) throws EconomyException {
        takeMoney(userCache.getUserData(playerUUID), money);
    }

    @Override
    public void giveMoney(final @NonNull OfflinePlayer player, final double money) throws EconomyException {
        giveMoney(userCache.getUserData(player.getUniqueId()), money);
    }

    @Override
    public void giveMoney(final @NonNull String playerName, final double money) throws EconomyException {
        giveMoney(userCache.getUserData(playerName), money);
    }

    @Override
    public void giveMoney(final @NonNull UUID playerUUID, final double money) throws EconomyException {
        giveMoney(userCache.getUserData(playerUUID), money);
    }

    @Override
    public void setMoney(final @NonNull OfflinePlayer player, final double money) throws EconomyException {
        setMoney(userCache.getUserData(player.getUniqueId()), money);
    }

    @Override
    public void setMoney(final @NonNull String playerName, final double money) throws EconomyException {
        setMoney(userCache.getUserData(playerName), money);
    }

    @Override
    public void setMoney(final @NonNull UUID playerUUID, final double money) throws EconomyException {
        setMoney(userCache.getUserData(playerUUID), money);
    }

    public void applyVault() {
        final Plugin vaultPlugin = Bukkit.getPluginManager().getPlugin("Vault");
        if (vaultPlugin == null) {
            plugin.getLogger().severe("Nie odnaleziono pluginu Vault, obsługa ekonomii przez niego nie jest możliwa!");
            return;
        }

        if (!vaultPlugin.isEnabled()) {
            plugin.getLogger().severe("Odnaleziono plugin Vault, lecz jest wyłączony. Obsługa ekonomii przez niego nie jest możliwa!");
            return;
        }

        plugin.getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new VaultEconomyImpl(config, userCache, plugin, this), plugin, ServicePriority.Highest);
        plugin.getLogger().info("Odnaleziono plugin Vault, ekonomia została wpięta.");
    }
}
