package de.schweinefilet.prototype.client.match;

import static de.schweinefilet.prototype.client.Client.ENTITY_HOLDER;

import de.schweinefilet.prototype.client.Client;
import de.schweinefilet.prototype.client.util.UILog;
import de.schweinefilet.prototype.common.entity.Team;
import de.schweinefilet.prototype.common.match.MatchController;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ClientMatchController extends MatchController {

    @Override
    public void onWin(Team team) {
        if (team == ENTITY_HOLDER.getUser().getTeam()) {
            UILog.showBlockingInfo("YOU WON\nYOU WON\nYOU WON\nYOU WON");
            Client.stop();
        } else {
            UILog.showBlockingInfo("YOU LOST\nYOU LOST\nYOU LOST\nYOU LOST");
            Client.stop();
        }
    }

    /**
     * Sets the new amount of wins.
     * @param redWins the new amount of red wins
     * @param blueWins the new amount of blue wins
     */
    public void update(int redWins, int blueWins) {
        this.blueWins = blueWins;
        this.redWins = redWins;
    }
}
