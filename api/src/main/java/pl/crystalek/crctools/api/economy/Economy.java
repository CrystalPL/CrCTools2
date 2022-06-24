package pl.crystalek.crctools.api.economy;

import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import pl.crystalek.crctools.api.exception.EconomyException;

import java.util.UUID;

/**
 * Economy API
 */
public interface Economy {

    /**
     * Get balance of player
     *
     * @param player to check
     * @return Amount of money
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player is null
     */
    double getBalance(final @NonNull OfflinePlayer player);

    /**
     * Get balance of player
     *
     * @param playerName to check
     * @return Amount of money
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player name is null
     */
    double getBalance(final @NonNull String playerName);


    /**
     * Get balance of player
     *
     * @param playerUUID to check
     * @return Amount of money
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player uuid is null
     */
    double getBalance(final @NonNull UUID playerUUID);

    /**
     * Check if player has money
     *
     * @param player to check
     * @param money  amount of money to check
     * @return True if player has money
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player is null
     */
    boolean hasMoney(final @NonNull OfflinePlayer player, final double money);

    /**
     * Check if player has money
     *
     * @param playerName to check
     * @param money      amount of money to check
     * @return True if player has money
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player name is null
     */
    boolean hasMoney(final @NonNull String playerName, final double money);

    /**
     * Check if player has money
     *
     * @param playerUUID to check
     * @param money      amount of money to check
     * @return True if player has money
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player uuid is null
     */
    boolean hasMoney(final @NonNull UUID playerUUID, final double money);

    /**
     * Take money from player account
     *
     * @param player to take money from account
     * @param money  amount of money to take
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player is null
     */
    void takeMoney(final @NonNull OfflinePlayer player, final double money);

    /**
     * Take money from player account
     *
     * @param playerName to take money from account
     * @param money      amount of money to take
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player name is null
     */
    void takeMoney(final @NonNull String playerName, final double money);

    /**
     * Take money from player account
     *
     * @param playerUUID to take money from account
     * @param money      amount of money to take
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player uuid is null
     */
    void takeMoney(final @NonNull UUID playerUUID, final double money);

    /**
     * Give money to player
     *
     * @param player to give money
     * @param money  amount of money to give
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player is null
     */
    void giveMoney(final @NonNull OfflinePlayer player, final double money);

    /**
     * Give money to player
     *
     * @param playerName to give money
     * @param money      amount of money to give
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player name is null
     */
    void giveMoney(final @NonNull String playerName, final double money);

    /**
     * Give money to player
     *
     * @param playerUUID to give money
     * @param money      amount of money to give
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player uuid is null
     */
    void giveMoney(final @NonNull UUID playerUUID, final double money);

    /**
     * Set money of player
     *
     * @param player to set money
     * @param money  amount of money to set
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player is null
     */
    void setMoney(final @NonNull OfflinePlayer player, final double money);

    /**
     * Set money of player
     *
     * @param playerName to set money
     * @param money      amount of money to set
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player name is null
     */
    void setMoney(final @NonNull String playerName, final double money);

    /**
     * Set money of player
     *
     * @param playerUUID to set money
     * @param money      amount of money to set
     * @throws EconomyException     if there is any problem
     * @throws NullPointerException if player uuid is null
     */
    void setMoney(final @NonNull UUID playerUUID, final double money);
}
