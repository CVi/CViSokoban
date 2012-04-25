/*
 * File: SokobanCanvas.java
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
import org.vikenpedia.sokoban.SquareMap;

/**
 * The Class SokobanCanvas.
 */
public class SokobanCanvas extends Canvas implements LevelChangeListener {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1521765808340132046L;

    /** The Image for blank. */
    private Image blank;

    /** The Image for box on target. */
    private Image bot;

    /** The Image for box. */
    private Image box;

    /** The Image for mover. */
    private Image mov;

    /** The Image for mover on target. */
    private Image mot;

    /** The Image for target. */
    private Image tgt;

    /** The Image for wall. */
    private Image wall;

    /** The need-to-draw-list with squaremaps. */
    private Stack<SquareMap> maps;

    /** The whether the canvas needs full redrawing */
    private boolean reset = true;

    /** The game engine. Only used for LevelChangeListener */
    private SokobanEngine engine;

    /**
     * Instantiates a new sokoban canvas.
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
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

    /**
     * Draw a square.
     * 
     * @param g
     *            the Graphics object to draw to
     * @param p
     *            the Position to draw at
     * @param i
     *            the Image to draw
     */
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

    /**
     * Adds the square.
     * 
     * @param maping
     *            the maping
     */
    private void addSquare(SquareMap maping) {
        maps.add(maping);
        repaint();
    }

    /**
     * Reset.
     */
    public void reset() {
        reset = true;
        maps.clear();
        repaint();
    }

    /**
     * Draw entire map.
     * 
     * @param map
     *            the map
     */
    public void drawEntireMap(String[] map) {
        reset();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length(); x++) {
                addSquare(new SquareMap(map[y].charAt(x), new Position(x, y)));
            }
        }
    }

    /**
     * Sets the engine.
     * 
     * @param newEngine
     *            the new engine
     */
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
