package de.schweinefilet.prototype.client.frame.HUD.component;

import de.schweinefilet.prototype.common.frame.draw.Drawable;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A drawable HUD component.
 */
@NoArgsConstructor
public abstract class Component implements Drawable {

    /**
     * The x- and y-coordinate of the {@link Component}.
     */
    @Getter
    protected int x, y;

    Component(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
