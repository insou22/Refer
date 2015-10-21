package co.insou.refer.api;

import co.insou.refer.utils.ReferPlayer;
import co.insou.refer.utils.Reward;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by insou on 10/10/2015.
 */
public class ReferEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private ReferPlayer referer;
    private ReferPlayer referee;
    private boolean hasReward;
    private Reward reward;

    public ReferEvent (ReferPlayer referer, ReferPlayer referee) {
        this.referer = referer;
        this.referee = referee;
        hasReward = false;
    }

    public ReferEvent (ReferPlayer referer, ReferPlayer referee, Reward reward) {
        this.referer = referer;
        this.referee = referee;
        this.reward = reward;
        hasReward = true;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public ReferPlayer getReferer () {
        return referer;
    }

    public ReferPlayer getReferee () {
        return referee;
    }

    public boolean hasReward () {
        return hasReward;
    }

    public Reward getReward () {
        return reward;
    }

}
