package co.insou.refer.api;

import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.Reward;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReferEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private ReferPlayer referer;
    private ReferPlayer referee;
    private boolean hasReward;
    private Reward reward;

    private boolean cancelled = false;


    public ReferEvent(ReferPlayer referer, ReferPlayer referee) {
        this.referer = referer;
        this.referee = referee;
        hasReward = false;
    }

    public ReferEvent(ReferPlayer referer, ReferPlayer referee, Reward reward) {
        this.referer = referer;
        this.referee = referee;
        this.reward = reward;
        hasReward = true;
    }

    public ReferPlayer getReferer() {
        return referer;
    }

    public ReferPlayer getReferee() {
        return referee;
    }

    public boolean hasReward() {
        return hasReward;
    }

    public Reward getReward() {
        return reward;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
