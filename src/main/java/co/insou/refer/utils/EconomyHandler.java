package co.insou.refer.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

/**
 * Created by insou on 26/11/2015.
 */
public class EconomyHandler {

    private Economy econ;

    public EconomyHandler(Economy econ) {
        this.econ = econ;
    }

    public boolean isSetup() {
        return econ != null;
    }

    public void addMoney(Player player, double amount) {
        econ.depositPlayer(player.getPlayer(), amount);
    }

    public double getMoney(Player player) {
        return econ.getBalance(player);
    }

}
