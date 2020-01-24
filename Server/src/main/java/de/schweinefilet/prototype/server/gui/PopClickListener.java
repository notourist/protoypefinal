package de.schweinefilet.prototype.server.gui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Opens the {@link PopUpMenu}.
 */
public class PopClickListener extends MouseAdapter {

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            showMenu(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void showMenu(Component component, int x, int y) {
        new PopUpMenu().show(component, x, y);
    }
}
