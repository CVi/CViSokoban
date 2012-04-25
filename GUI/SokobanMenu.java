/*
 * File: SokobanMenu.java
 * 
 * @author CVi
 * 
 * Copyright (c) 2012, CVi
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package org.vikenpedia.sokoban.GUI;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * The Menu.
 */
public class SokobanMenu extends JMenuBar {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -9068903045801749765L;

    /** The paste. */
    public JMenuItem paste;

    /**
     * Instantiates a new sokoban menu.
     * 
     * @param window
     *            the window
     */
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
