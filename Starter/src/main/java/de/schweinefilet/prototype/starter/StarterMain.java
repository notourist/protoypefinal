package de.schweinefilet.prototype.starter;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

class StarterMain {
    public static void main(String... args) {
        JFrame frame = new JFrame("Starter");
        frame.setContentPane(new GUI().panel);
        frame.setSize(260, 220);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
