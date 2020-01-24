package de.schweinefilet.prototype.client.frame.HUD;

import de.schweinefilet.prototype.client.frame.HUD.component.Component;
import de.schweinefilet.prototype.client.frame.HUD.component.ComponentTeleport;
import de.schweinefilet.prototype.client.frame.HUD.component.ComponentWins;
import java.awt.Graphics2D;
import java.util.HashSet;

/**
 * Contains every {@link Component}.
 */
public class HUDHolder {

    /**
     * Every {@link Component} stored in a {@link HashSet}.
     */
    private HashSet<Component> components;

    public HUDHolder() {
        components = new HashSet<>();
        components.add(new ComponentWins());
        components.add(new ComponentTeleport());
    }

    /**
     * Calls {@link Component#draw(Graphics2D)} of every {@link Component} in {@link HUDHolder#components}.
     * @param graphics2D -
     */
    public void drawAll(Graphics2D graphics2D) {
        components.forEach(component -> component.draw(graphics2D));
    }
}
