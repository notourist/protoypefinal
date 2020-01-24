package de.schweinefilet.prototype.client.frame;

import static de.schweinefilet.prototype.client.frame.paint.PaintStrategy.PAGE_FLIPPING;

import de.schweinefilet.prototype.client.Client;
import de.schweinefilet.prototype.client.frame.paint.PaintStrategy;
import de.schweinefilet.prototype.client.frame.paint.PaintStrategyBufferedImage;
import de.schweinefilet.prototype.client.frame.paint.PaintStrategyPageFlipping;
import de.schweinefilet.prototype.client.frame.paint.PaintStrategyVolatileImage;
import de.schweinefilet.prototype.client.util.UILog;
import de.schweinefilet.prototype.common.ExecutableHandler;
import de.schweinefilet.prototype.common.util.LogUtil;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.BufferCapabilities.FlipContents;
import java.awt.ImageCapabilities;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import lombok.Getter;

/**
 * This class controls the PaintLoop and the GameFrame.
 */
public class FrameHandler implements ExecutableHandler {

    /**
     * The used {@link PaintLoop}.
     */
    private PaintLoop loop;

    /**
     * The used {@link GameFrame}.
     */
    private final GameFrame gameFrame;

    /**
     * Stores the paint strategy ID.
     */
    private int strategy;

    /**
     * The used {@link PaintStrategy}.
     */
    @Getter
    private PaintStrategy paintStrategy;

    public FrameHandler(int strategy) {
        this.strategy = strategy;
        LogUtil.info("Init: Frame");

        gameFrame = new GameFrame();
    }

    /**
     * Shows the frame, creates the {@link PaintStrategy} starts the PaintLoop.
     */
    @Override
    public void start() {
        LogUtil.info("Start: Frame");
        //show frame
        gameFrame.setVisible(true);
        LogUtil.info("Resolution: " + gameFrame.getWidth() + "/" + gameFrame.getHeight());

        //used to prevent a ISE
        gameFrame.addNotify();

        BufferCapabilities current = gameFrame.getGraphicsConfiguration().getBufferCapabilities();
        FlipContents flipContents = FlipContents.COPIED;

        //noinspection ConstantConditions
        if (strategy == PAGE_FLIPPING) {
            if (current.isPageFlipping()) {
                if (current.isMultiBufferAvailable()) {
                    flipContents = FlipContents.PRIOR;
                } else {
                    LogUtil.warn("Multi buffering is NOT available! This is NOT adviced!");
                    UILog.showWarn("Multibuffering ist nicht verfügbar! Dies wird NICHT empfohlen!"
                        + "\nBitte benutze Image Buffered.");
                }
            } else {
                LogUtil.error("The PaintStrategy is page flipping, but the device doesn't support it!");
                UILog.showBlockingError("Page Flipping wird von diesem Gerät nicht unterstützt!");
                Client.dirtyStop();
            }

            try {
                gameFrame.createBufferStrategy(2, new BufferCapabilities(new ImageCapabilities(true),
                    new ImageCapabilities(true), flipContents));
            } catch (AWTException e) {
                LogUtil.error("Can't create BufferStrategy with 2 Buffers.");
                e.printStackTrace();
                UILog.showBlockingError("Konnte BufferStrategy mit 2 Buffern nicht erstellen!");
            }
        }

        switch(strategy) {
            case 1:
                paintStrategy = new PaintStrategyBufferedImage();
                break;
            case 2:
                paintStrategy = new PaintStrategyPageFlipping();
                break;
            case 3:
                paintStrategy = new PaintStrategyVolatileImage();
                break;
            default:
                paintStrategy = null;
                LogUtil.error("Can't create PaintStrategy");
                UILog.showBlockingError("Kann PaintStrategy nicht erstellen!");
                Client.dirtyStop();
                break;
        }
        LogUtil.info("Found " + paintStrategy.getName());

        loop = new PaintLoop(gameFrame);
        loop.setRunning(true);
        loop.start();
    }

    /**
     * Stops the PaintLoop.
     */
    @Override
    public void stop() {
        LogUtil.info("Stop: Frame");
        loop.setRunning(false);
    }

    /**
     * Wrapper function, returns the {@link GameFrame}'s width.
     *
     * @return the frame's width.
     */
    public int getWidth() {
        return gameFrame.getWidth();
    }

    /**
     * Wrapper function, returns the {@link GameFrame}'s height.
     *
     * @return the frame's height.
     */
    public int getHeight() {
        return gameFrame.getHeight();
    }

    /**
     * Wrapper function, returns the frame's {@link BufferStrategy}.
     *
     * @return the {@link BufferStrategy}
     */
    public BufferStrategy getBufferStrategy() {
        return gameFrame.getBufferStrategy();
    }

    /**
     * Wrapper function, returns {@link javax.swing.JFrame#createVolatileImage(int, int)}.
     *
     * @return {@link javax.swing.JFrame#createVolatileImage(int, int)}
     */
    public VolatileImage createVolatileImage() {
        return gameFrame.createVolatileImage(gameFrame.getWidth(), gameFrame.getHeight());
    }
}
