package de.schweinefilet.prototype.client.frame;

import de.schweinefilet.prototype.client.Client;
import de.schweinefilet.prototype.client.frame.paint.PaintStrategy;
import de.schweinefilet.prototype.client.frame.paint.PaintStrategyImage;
import de.schweinefilet.prototype.common.util.LogUtil;

public class ResizeRunnable implements Runnable {

    private int tries = 0;

    private PaintStrategy paintStrategy = Client.FRAME_HANDLER.getPaintStrategy();


    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tryImageFix();
            }
        }
    }

    /**
     * Recursive recreates the image until it is fixed.
     */
    private void tryImageFix() {
        if (paintStrategy != null && paintStrategy.isImagePowered()
            && ((PaintStrategyImage) paintStrategy).hasValidImage()
            && tries < 9) {
            LogUtil.info("Recreated image | Try: " + tries);
            ((PaintStrategyImage) paintStrategy).recreateImage();
            tries++;
            try {
                wait(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tryImageFix();
        }
    }
}