package de.schweinefilet.prototype.client.frame.HUD.component;

import static de.schweinefilet.prototype.client.Client.FRAME_HANDLER;
import static de.schweinefilet.prototype.client.Client.MATCH_CONTROLLER;
import static de.schweinefilet.prototype.common.entity.Team.BLUE;
import static de.schweinefilet.prototype.common.entity.Team.RED;

import de.schweinefilet.prototype.common.Vars;
import de.schweinefilet.prototype.common.entity.Team;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Shows the number of wins.
 */
public class ComponentWins extends Component {

    public ComponentWins() {
        super(0, 0);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        for (int i = 0; i < Vars.MAX_WINS; i++) {
            graphics2D.setColor(Color.GRAY);
            int x = 10 + (i * 40);
            graphics2D.fillRect(x, 5, 30, 30);
            graphics2D.fillRect(FRAME_HANDLER.getWidth() - 30 - x, 5, 30, 30);
        }

        drawPoints(RED, graphics2D);
        drawPoints(BLUE, graphics2D);
    }

    private void drawPoints(Team team, Graphics2D graphics2D) {
        int wins;
        switch(team) {
            case BLUE:
                wins = MATCH_CONTROLLER.getBlueWins();
                graphics2D.setColor(BLUE.getColor());
                for (int i = 0; i < wins; i++) {
                    int x = FRAME_HANDLER.getWidth() - 40 - (i * 40);
                    graphics2D.fillRect(x, 5, 30, 30);
                }
                break;
            case RED:
                wins = MATCH_CONTROLLER.getRedWins();
                graphics2D.setColor(RED.getColor());
                for (int i = 0; i < wins; i++) {
                    int x =  10 + (i * 40);
                    graphics2D.fillRect(x, 5, 30, 30);
                }
                break;
        }
    }
}
