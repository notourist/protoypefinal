package de.schweinefilet.prototype.client.frame;

import static de.schweinefilet.prototype.client.Client.FRAME_HANDLER;

import de.schweinefilet.prototype.client.frame.paint.PaintStrategy;
import de.schweinefilet.prototype.client.frame.listener.FocusLooseListener;
import de.schweinefilet.prototype.client.frame.listener.KeyboardListener;
import de.schweinefilet.prototype.client.frame.listener.MouseButtonListener;
import de.schweinefilet.prototype.client.frame.listener.MouseMoveListener;
import de.schweinefilet.prototype.common.Vars;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The {@link Frame} the user sees. This class contains the paint logic.
 */
public class GameFrame extends JFrame {

    @SuppressWarnings("WeakerAccess")
    public GameFrame() {
        super();
        //setup the frame
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setResizable(true);
        this.setTitle(
            "Prototype " + Vars.VERSION);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //creates an invisible cursor
        this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(
            16,
            16,
            BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor"));
    }

    /**
     * Paints the game with the selected {@link PaintStrategy}.
     * @param graphics -
     */
    @Override
    public void paint(Graphics graphics) {
        //check if the method can draw
        if (FRAME_HANDLER.getPaintStrategy() != null) {
            switch(FRAME_HANDLER.getPaintStrategy().getId()) {
                //BUFFERED_IMAGE
                case 1:
                    FRAME_HANDLER.getPaintStrategy().paint(graphics);
                    break;
                //PAGE_FLIPPING
                case 2:
                    if (FRAME_HANDLER.getBufferStrategy() != null) {
                        FRAME_HANDLER.getPaintStrategy().paint(getBufferStrategy().getDrawGraphics());
                    }
                    break;
                //VOLATILE_IMAGE
                case 3:
                    FRAME_HANDLER.getPaintStrategy().paint(graphics);
                    break;
            }
        }
    }

    /**
     * Adds all the listeners.
     */
    void addListeners() {
        this.addKeyListener(new KeyboardListener());
        this.addMouseMotionListener(new MouseMoveListener());
        this.addMouseListener(new MouseButtonListener());
        this.addFocusListener(new FocusLooseListener());
    }

    @Override
    protected void finalize() throws Throwable {
        this.dispose();
    }
}
