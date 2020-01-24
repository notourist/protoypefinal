package de.schweinefilet.prototype.common.entity;

import java.awt.Point;
import java.awt.Rectangle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Represents an {@link Entity} with bounds.
 */
@NoArgsConstructor
abstract class BoundedEntity extends Entity {

    /**
     * The {@link BoundedEntity}'s bounds.
     */
    @Getter
    protected Rectangle bounds;

    BoundedEntity(@NonNull Point location, int width, int height) {
        super(location);
        this.bounds = new Rectangle(location.x, location.y, width, height);
    }

    BoundedEntity(@NonNull Point location, @NonNull String uniqueId,  int width, int height) {
        super(location, uniqueId);
        this.bounds = new Rectangle(location.x, location.y, width, height);
    }

    /**
     * Sets the {@link BoundedEntity#location}, aligned by -9 to represent whats drawn, as the new bounds location.
     */
    void updateBounds() {
        bounds.setLocation(location.x - 9, location.y - 9);
    }

    /**
     * Sets the new {@link BoundedEntity#location} and calls {@link BoundedEntity#updateBounds()}.
     * @param location the new location
     */
    @Override
    public void setLocation(Point location) {
        super.setLocation(location);
        bounds.setLocation(location);
    }
}
