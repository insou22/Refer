package co.insou.refer.api;

import co.insou.refer.utils.ReferPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class ReferAPI {

    public ReferPlayer getReferPlayer (Player p) {
        return ReferPlayer.getReferPlayer(p);
    }

    public List<ReferPlayer> getReferPlayers () {
        return ReferPlayer.getReferPlayers();
    }

}
