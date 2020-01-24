package de.schweinefilet.prototype.starter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("Convert2Lambda")
class GUI {

    public JPanel panel;
    private JComboBox<String> comboBox_team;
    private JComboBox<String> comboBox_strategy;
    private JTextField txtField_Name;
    private JTextField txtField_IP;
    private JButton btn_Start;

    private final ProcessBuilder builder;

    GUI() {
        createUIComponents();
        builder = new ProcessBuilder();
        builder.directory(new File(Paths.get("").toAbsolutePath().toString()));
    }

    private void createUIComponents() {
        comboBox_team.addItem("Red");
        comboBox_team.addItem("Blue");
        comboBox_strategy.addItem("Page Flipping");
        comboBox_strategy.addItem("Buffered Image");
        comboBox_strategy.addItem("Volatile Image");

        //ideauidesigner kommt nicht mit Lambdas klar
        btn_Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txtField_IP.getText().equals("")
                    && !txtField_Name.getText().equals("")) {
                    if (new File("client.jar").exists()) {
                        try {
                            String strategy;
                            //BufferedImage
                            if (comboBox_strategy.getSelectedItem().equals("Buffered Image")) {
                                strategy = "1";
                            //Page Flipping
                            } else if (comboBox_strategy.getSelectedItem().equals("Page Flipping")) {
                                strategy = "2";
                            //Volatile Image
                            } else {
                                strategy = "3";
                            }
                            builder.command("java",
                                "-jar",
                                "client.jar",
                                "-ip",
                                txtField_IP.getText(),
                                "-name",
                                txtField_Name.getText(),
                                "-team",
                                ((String) comboBox_team.getSelectedItem()).toUpperCase(),
                                "-strategy",
                                strategy)
                                .start();
                            System.out.println("Starting Client with arguments " + builder.command());
                        } catch (IOException exc) {
                            noStartWarning(exc);
                        }
                    } else {
                        noFileWarning();
                    }
                } else {
                    fieldNullWarning();
                }
            }
        });
    }

    private void fieldNullWarning() {
        Object[] options = {"OK"};
        JOptionPane.showOptionDialog(null,
            "Alle Felder müssen ausgefüllt sein!",
            "Fehler",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
    }

    private void noStartWarning(IOException exc) {
        JOptionPane.showConfirmDialog(null,
            "Kann Client nicht starten!\n" + exc.getMessage(),
            "Fehler",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null);
    }

    private void noFileWarning() {
        JOptionPane.showConfirmDialog(null,
            "Kann die Datei \"client.jar\" in" + builder.directory() + " nicht finden!",
            "Fehler",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null);
    }
}