package org.vikenpedia.sokoban.GUI;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class SokobanMenu extends JMenuBar {
    /**
     * 
     */
    private static final long serialVersionUID = -9068903045801749765L;
    public JMenuItem paste;

    public SokobanMenu(final sokoWin window) {
        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                window.open();
            }
        });

        JMenuItem save = new JMenuItem("Save");
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                window.save();
            }
        });

        JMenuItem replay = new JMenuItem("Replay");
        replay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                window.replay();
            }
        });

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(replay);

        add(fileMenu);

        JMenu editMenu = new JMenu("Edit");

        JMenuItem copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                window.copy();
            }
        });

        paste = new JMenuItem("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                window.paste();
            }
        });

        JMenuItem undo = new JMenuItem("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                window.undo();
            }
        });

        JMenuItem redo = new JMenuItem("Redo");
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
        redo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                window.redo();
            }
        });

        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(undo);
        editMenu.add(redo);
        add(editMenu);

    }
}
