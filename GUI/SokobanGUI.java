package org.vikenpedia.sokoban.GUI;

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SokobanGUI {
    public static void main(String[] args) {
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty(
                    "com.apple.mrj.application.apple.menu.about.name", "Test");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e.getMessage());
        } catch (InstantiationException e) {
            System.out.println("InstantiationException: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException: " + e.getMessage());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("UnsupportedLookAndFeelException: "
                    + e.getMessage());
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                sokoWin window;
                try {
                    window = new sokoWin();
                    window.frame.setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
