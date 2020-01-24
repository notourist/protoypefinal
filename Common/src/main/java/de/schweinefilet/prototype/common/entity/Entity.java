package de.schweinefilet.prototype.common.entity;

import de.schweinefilet.prototype.common.frame.draw.DrawableOffset;
import java.awt.Point;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents an object on a map.
 */
@NoArgsConstructor
public abstract class Entity implements Serializable, DrawableOffset {

    /**
     * Used to identify every {@link Entity}.
     */
    @Getter
    protected String uniqueId;

    /**
     * The location is defined as the middle of {@link Entity}.
     */
    @Getter
    @Setter
    protected Point location;

    Entity(@NonNull Point location) {
        uniqueId = UUID.randomUUID().toString();
        this.location = location;
    }

    Entity(@NonNull Point location, @NonNull String uniqueId) {
        this(location);
        this.uniqueId = uniqueId;
    }

    @Override
    public int hashCode() {
        return uniqueId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Entity && obj.hashCode() == this.hashCode();
    }
}
