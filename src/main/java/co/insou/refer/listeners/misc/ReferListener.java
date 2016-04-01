package co.insou.refer.listeners.misc;

import co.insou.refer.Refer;
import co.insou.refer.api.ReferEvent;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.Reward;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Created by insou on 25/10/2015.
 */
public class ReferListener implements Listener {

    private final Refer plugin;

    public ReferListener(Refer plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(ReferEvent e) {
        ReferPlayer referee = e.getReferee();
        ReferPlayer referer = e.getReferer();
        Reward reward = e.getReward();
        referer.sendFormattedMessage("&aYou have referred " + referee.getPlayer().getName() + "!");
        referee.sendFormattedMessage("&aYou have been referred by " + referer.getPlayer().getName() + "!");
        if (e.hasReward()) {
            reward.rewardPlayer(referee);
        }
        plugin.getReferDatabase().referPlayer(referer.getPlayer(), referee.getPlayer());
    }

}
