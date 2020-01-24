package de.schweinefilet.prototype.server.match;

import de.schweinefilet.prototype.common.Vars.SERVER;
import de.schweinefilet.prototype.common.entity.Team;
import de.schweinefilet.prototype.common.match.MatchController;
import de.schweinefilet.prototype.common.util.LogUtil;
import de.schweinefilet.prototype.server.Server;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServerMatchController extends MatchController {

    /**
     * Quits the server on a win or restarts the server if {@link SERVER#AUTO_RESTART} is true.
     * @param team the winning {@link Team}
     */
    @Override
    public void onWin(Team team) {
        LogUtil.info("Team " + team + " won.\nQuitting...");

        if (SERVER.AUTO_RESTART) {
            Server.restart();
        }
        System.exit(0);
    }
}
