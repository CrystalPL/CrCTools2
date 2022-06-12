package pl.crystalek.crctools.api.economy;

import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import pl.crystalek.crctools.api.exception.EconomyException;

import java.util.UUID;

public interface Economy {

    double getBalance(final @NonNull OfflinePlayer player) throws EconomyException;

    double getBalance(final @NonNull String playerName) throws EconomyException;

    double getBalance(final @NonNull UUID playerUUID) throws EconomyException;

    boolean hasMoney(final @NonNull OfflinePlayer player, final double money) throws EconomyException;

    boolean hasMoney(final @NonNull String playerName, final double money) throws EconomyException;

    boolean hasMoney(final @NonNull UUID playerUUID, final double money) throws EconomyException;

    boolean takeMoney(final @NonNull OfflinePlayer player, final double money) throws EconomyException;

    boolean takeMoney(final @NonNull String playerName, final double money) throws EconomyException;

    boolean takeMoney(final @NonNull UUID playerUUID, final double money) throws EconomyException;

    boolean giveMoney(final @NonNull OfflinePlayer player, final double money) throws EconomyException;

    boolean giveMoney(final @NonNull String playerName, final double money) throws EconomyException;

    boolean giveMoney(final @NonNull UUID playerUUID, final double money) throws EconomyException;

    void setMoney(final @NonNull OfflinePlayer player, final double money) throws EconomyException;

    void setMoney(final @NonNull String playerName, final double money) throws EconomyException;

    void setMoney(final @NonNull UUID playerUUID, final double money) throws EconomyException;
}
