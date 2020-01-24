package de.schweinefilet.prototype.common.match;

import de.schweinefilet.prototype.common.Vars;
import de.schweinefilet.prototype.common.entity.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public abstract class MatchController {

    /**
     * The number of wins {@link Team#RED} has.
     */
    @Getter
    @Setter
    protected int redWins;

    /**
     * The number of wins {@link Team#BLUE} has.
     */
    @Getter
    @Setter
    protected int blueWins;

    /**
     * Adds one win to the given {@link Team}. When one {@link Team} scores the maximum amount
     * of wins defined by {@link Vars#MAX_WINS} {@link MatchController#onWin(Team)} is called.
     * @param team the team who gets plus one win
     */
    public void win(Team team) {
        switch(team) {

            case BLUE:
                blueWins++;
                break;
            case RED:
                redWins++;
                break;
        }

        if (redWins == Vars.MAX_WINS || blueWins == Vars.MAX_WINS) {
            onWin(team);
        }
    }

    /**
     * Is called when a {@link Team} wins.
     * @param team the winning {@link Team}
     */
    public abstract void onWin(Team team);
}
