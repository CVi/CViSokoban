package org.vikenpedia.sokoban.GUI;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.vikenpedia.sokoban.SokobanEngine;
import org.vikenpedia.sokoban.SokobanLevels.FlatLevel;
import org.vikenpedia.sokoban.SokobanLevels.FlatLevelNoTitle;
import org.vikenpedia.sokoban.SokobanLevels.FlatLevelWithMoves;
import org.vikenpedia.sokoban.SokobanLevels.Level;


public class sokoWin implements KeyListener {
    JFrame frame;
    private SokobanEngine gameengine;
    private SokobanCanvas canvas;
    private SokobanMenu menuBar;
    private boolean isPasteLocked = false;
    private String[] emptymap = { " #### #   # ### ", " #    ##  # #  #",
            " ###  # # # #  #", " #    #  ## #  #", " #### #   # ### " };

    /**
     * Create the application.
     * 
     * @throws IOException
     */
    public sokoWin() throws IOException {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     * 
     * @throws IOException
     */
    private void initEngine(Level lev) {
        this.gameengine = new SokobanEngine(lev);
        canvas.setEngine(this.gameengine);
        canvas.drawEntireMap(lev.getLines());
    }

    private void initialize() throws IOException {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(ImageIO.read(new File("resources/sokoban/Icon.png")));
        menuBar = new SokobanMenu(this);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
        canvas = new SokobanCanvas();
        frame.getContentPane().add(canvas);
        canvas.drawEntireMap(emptymap);
        canvas.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int remain = 1;
        switch (event.getKeyCode()) {
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_D:
            if (gameengine != null) {
                remain = gameengine.move('R');
            }
            break;
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_A:
            if (gameengine != null) {
                remain = gameengine.move('L');
            }
            break;
        case KeyEvent.VK_UP:
        case KeyEvent.VK_W:
            if (gameengine != null) {
                remain = gameengine.move('U');
            }
            break;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_S:
            if (gameengine != null) {
                remain = gameengine.move('D');
            }
            break;
        case KeyEvent.VK_O:
            // openLevel();
            break;
        case KeyEvent.VK_BACK_SPACE:
            // gameengine.undo();
            break;
        case KeyEvent.VK_U:
            // printMoves();
        }
        if (remain == 0) {
            gameengine = null;
            canvas.reset();
            canvas.drawEntireMap(emptymap);
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    public void open() {
        final JFileChooser fc = new JFileChooser();

        FileNameExtensionFilter flatNtFilter = new FileNameExtensionFilter(
                "Sokoban Flat Levels, no title", "txt");
        fc.addChoosableFileFilter(flatNtFilter);

        FileNameExtensionFilter flatFilter = new FileNameExtensionFilter(
                "Sokoban Flat Levels (With title)", "txt");
        fc.addChoosableFileFilter(flatFilter);

        FileNameExtensionFilter flatMovesFilter = new FileNameExtensionFilter(
                "Sokoban Flat Levels (with title and moves)", "txt");
        fc.addChoosableFileFilter(flatMovesFilter);

        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(flatNtFilter);
        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            gameengine = null;
            canvas.reset();
            Level lev = null;
            if (fc.getFileFilter() == flatNtFilter) {
                lev = new FlatLevelNoTitle(fc.getSelectedFile());
            } else if (fc.getFileFilter() == flatFilter) {
                lev = new FlatLevel(fc.getSelectedFile());
            } else if (fc.getFileFilter() == flatMovesFilter) {
                lev = new FlatLevelWithMoves(fc.getSelectedFile());
            }
            if (lev != null) {
                canvas.drawEntireMap(emptymap);
                initEngine(lev);
                (new PasteThread(lev.getInitialMoves(), this)).start();
            }
        }
    }

    public void save() {
        final JFileChooser fc = new JFileChooser();

        FileNameExtensionFilter flatMovesFilter = new FileNameExtensionFilter(
                "Sokoban Flat Levels (with title and moves)", "txt");
        fc.addChoosableFileFilter(flatMovesFilter);

        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(flatMovesFilter);
        int returnVal = fc.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (fc.getFileFilter() == flatMovesFilter) {
                (new FlatLevelWithMoves(gameengine.level)).save(
                        gameengine.getMoves(), fc.getSelectedFile());
            }
        }
    }

    public void undo() {
        gameengine.undo();
    }

    public void redo() {
        if (this.gameengine.redos.size() > 0) {
            (new PasteThread((this.gameengine.redos.pop().toString()), this))
                    .start();
        }
    }

    public void copy() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(gameengine.getMoves());
        clipboard.setContents(transferable, null);
    }

    public void paste() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipData = clipboard.getContents(clipboard);
        try {
            if (clipData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String s = (String) (clipData
                        .getTransferData(DataFlavor.stringFlavor));
                (new PasteThread(s, this)).start();
            }
        } catch (Exception ufe) {
        }
    }

    public void replay() {
        canvas.removeKeyListener(this);
        String moves = gameengine.getMoves();
        Level lev = gameengine.level;
        lev.reset();
        initEngine(lev);
        (new PasteThread(moves, this)).start();
    }

    private class PasteThread extends Thread {
        private String s;
        private sokoWin window;

        public PasteThread(String s, sokoWin win) {
            super();
            this.s = s;
            window = win;
        }

        public void run() {
            while (isPasteLocked) {
                try {
                    synchronized (menuBar.paste) {
                        menuBar.paste.wait();
                    }
                } catch (InterruptedException e) {
                }
            }
            isPasteLocked = true;
            canvas.removeKeyListener(window);
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (gameengine.redo(Character.toUpperCase(c)) == 0) {
                    gameengine = null;
                    canvas.reset();
                    canvas.drawEntireMap(emptymap);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            canvas.addKeyListener(window);
            isPasteLocked = false;
            synchronized (menuBar.paste) {
                menuBar.paste.notify();
            }
        }
    }
}
