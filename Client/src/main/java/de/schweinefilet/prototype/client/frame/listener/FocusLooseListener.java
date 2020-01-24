package de.schweinefilet.prototype.client.frame.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static de.schweinefilet.prototype.client.Client.ENTITY_HOLDER;

/**
 * This listener checks if the user has tabbed out/minimized the game.
 */
public class FocusLooseListener implements FocusListener {

    @Override
    public void focusGained(FocusEvent e) {
    }

    /**
     * Needed to fix the player not stopping to move after the user
     * minimized/tabbed out of the game.
     *
     * @param e -
     */
    @Override
    public void focusLost(FocusEvent e) {
        ENTITY_HOLDER.getUser().stopMovement();
    }
}
