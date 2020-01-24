package de.schweinefilet.prototype.client.frame.HUD.component;

import static de.schweinefilet.prototype.client.Client.CONTROL_HANDLER;
import static de.schweinefilet.prototype.client.Client.ENTITY_HOLDER;
import static de.schweinefilet.prototype.client.Client.FRAME_HANDLER;

import de.schweinefilet.prototype.client.entity.User;
import java.awt.Color;
import java.awt.Graphics2D;
import lombok.NoArgsConstructor;

/**
 * Shows when the {@link User} can teleport or not.
 */
@NoArgsConstructor
public class ComponentTeleport extends Component {

    @Override
    public void draw(Graphics2D graphics2D) {
        if (ENTITY_HOLDER.getUser().isTeleported()) {
            graphics2D.setColor(Color.GRAY);
        } else {
            graphics2D.setColor(Color.GREEN);
        }
        graphics2D.fillOval(FRAME_HANDLER.getWidth() - 33,
            FRAME_HANDLER.getHeight() - 33,
            30, 30);
    }
}
