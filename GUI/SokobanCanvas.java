package org.vikenpedia.sokoban.GUI;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;

import org.vikenpedia.sokoban.LevelChangeListener;
import org.vikenpedia.sokoban.Position;
import org.vikenpedia.sokoban.SokobanEngine;


public class SokobanCanvas extends Canvas implements LevelChangeListener {
    /**
     * 
     */
    private static final long serialVersionUID = -1521765808340132046L;
    private Image blank;
    private Image bot;
    private Image box;
    private Image mov;
    private Image mot;
    private Image tgt;
    private Image wall;
    private Stack<SquareMap> maps;
    private boolean reset = true;
    private SokobanEngine engine;

    public SokobanCanvas() throws IOException {
        super();
        maps = new Stack<SquareMap>();
        File input;
        input = new File("resources/sokoban/blank16x16.png");
        blank = ImageIO.read(input);
        input = new File("resources/sokoban/movable_on_target16x16.png");
        bot = ImageIO.read(input);
        input = new File("resources/sokoban/movable16x16.png");
        box = ImageIO.read(input);
        input = new File("resources/sokoban/mover_on_target16x16.png");
        mot = ImageIO.read(input);
        input = new File("resources/sokoban/mover16x16.png");
        mov = ImageIO.read(input);
        input = new File("resources/sokoban/target16x16.png");
        tgt = ImageIO.read(input);
        input = new File("resources/sokoban/wall16x16.png");
        wall = ImageIO.read(input);

    }

    private void drawASquare(Graphics g, Position p, Image i) {
        g.drawImage(i, p.x * 16, p.y * 16, null);
    }

    @Override
    public void paint(Graphics g) {
        // We'll do our own painting, so leave out
        // a call to the superclass behavior
        SquareMap sm;
        if (reset) {
            g.clearRect(0, 0, 1920, 1080);
            reset = false;
        }
        while (maps.size() > 0) {
            sm = maps.pop();
            switch (sm.type) {
            case ' ':
                drawASquare(g, sm.pos, blank);
                break;
            case '*':
                drawASquare(g, sm.pos, bot);
                break;
            case '@':
                drawASquare(g, sm.pos, mov);
                break;
            case '+':
                drawASquare(g, sm.pos, mot);
                break;
            case '$':
                drawASquare(g, sm.pos, box);
                break;
            case '.':
                drawASquare(g, sm.pos, tgt);
                break;
            case '#':
                drawASquare(g, sm.pos, wall);
                break;
            }
        }
    }

    private void addSquare(SquareMap maping) {
        maps.add(maping);
        repaint();
    }

    public void reset() {
        reset = true;
        maps.clear();
        repaint();
    }

    public void drawEntireMap(String[] map) {
        reset();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length(); x++) {
                addSquare(new SquareMap(map[y].charAt(x), new Position(x, y)));
            }
        }
    }

    public void setEngine(SokobanEngine newEngine) {
        if (engine == null) {
            engine = newEngine;
            engine.addListener(this);
        } else if (engine != newEngine) {
            engine.removeListener(this);
            engine = newEngine;
            engine.addListener(this);
        }

    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void changedTile(SquareMap map) {
        maps.add(map);
        repaint();
    }
}
