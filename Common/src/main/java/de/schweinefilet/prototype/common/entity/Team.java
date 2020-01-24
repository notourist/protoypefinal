package de.schweinefilet.prototype.common.entity;

import de.schweinefilet.prototype.common.Vars;
import java.awt.Color;
import lombok.Getter;

/**
 * Both teams have to play against each other. They have to capture the
 * {@link Flag} a in {@link Vars#MAX_WINS} defined times.
 */
public enum Team {
    BLUE(Color.BLUE, Color.CYAN),
    RED(Color.RED, Color.ORANGE);

    @Getter
    Color color;

    @Getter
    Color spawnColor;

    Team(Color color, Color spawnColor) {
        this.color = color;
        this.spawnColor = spawnColor;
    }
}
