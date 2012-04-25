/*
 * File: Sokoban.java
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
package org.vikenpedia.sokoban;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.vikenpedia.sokoban.SokobanLevels.Level;

/**
 * The Class SokobanEngine. A game-rule engine for sokoban games. Written to be
 * somewhat versatile
 */
public class SokobanEngine {

    /** The board. */
    private String[] board;

    /** The player pos. */
    private Position playerPos;

    /** The remaining. */
    private int remaining;

    /** The moves. */
    private Stack<Character> moves;

    /** The dirty blocs. */
    private Queue<Position> dirtyBlocs;

    /** The level. */
    public Level level;

    /** Redo Moves *. */
    public Stack<Character> redos;

    /** The listeners. */
    private List<LevelChangeListener> listeners;

    /**
     * Move without clearing the redo cache.
     * 
     * @param dir
     *            the direction to move
     * @return the number of remaining targets
     */

    public int redo(char dir) {
        if (iMove(dir)) {
            calcRemain();
        }
        popDirty();
        return remaining;
    }

    /**
     * Move, and clean out the redo list.
     * 
     * @param dir
     *            the direction to move
     * @return the number of remaining targets
     */
    public int move(char dir) {
        if (iMove(dir)) {
            redos.clear();
            calcRemain();
        }
        popDirty();
        return remaining;
    }

    /**
     * Undo.
     * 
     * @return the number of remaining targets
     */
    public synchronized int undo() {
        if (moves.size() == 0)
            return remaining;
        boolean move;
        char c = moves.pop().charValue();
        switch (c) {
        case 'U':
            move = undoUp(true);
            break;
        case 'u':
            move = undoUp(false);
            break;
        case 'D':
            move = undoDown(true);
            break;
        case 'd':
            move = undoDown(false);
            break;
        case 'R':
            move = undoRight(true);
            break;
        case 'r':
            move = undoRight(false);
            break;
        case 'L':
            move = undoLeft(true);
            break;
        case 'l':
            move = undoLeft(false);
            break;
        default:
            move = false;
            moves.add(new Character(c));
        }
        if (move) {
            redos.add(new Character(c));
            calcRemain();
        }
        popDirty();
        return remaining;
    }

    /**
     * Gets the number of remaining targets.
     * 
     * @return the remaining
     */
    public int getRemaining() {
        return remaining;
    }

    /**
     * Instantiates a new sokoban engine.
     * 
     * @param level
     *            the level
     */
    public SokobanEngine(Level level) {
        this.level = level;
        board = level.getLines();
        dirtyBlocs = new LinkedList<Position>();
        moves = new Stack<Character>();
        redos = new Stack<Character>();
        listeners = new ArrayList<LevelChangeListener>();
        findPlayer();
        calcRemain();
    }

    /**
     * Internal move method.
     * 
     * @param dir
     *            the direction to move
     * @return true, if successful
     */
    private synchronized boolean iMove(char dir) {
        boolean move;
        switch (dir) {
        case 'U':
            move = moveUp();
            break;
        case 'D':
            move = moveDown();
            break;
        case 'R':
            move = moveRight();
            break;
        case 'L':
            move = moveLeft();
            break;
        default:
            move = false;
        }
        return move;
    }

    /**
     * Find player.
     */
    private void findPlayer() {
        for (int i = 0; i < board.length; i++) {
            if (board[i].contains("@")) {
                playerPos = new Position(board[i].indexOf('@'), i);
                return;
            } else if (board[i].contains("+")) {
                playerPos = new Position(board[i].indexOf('+'), i);
                return;
            }
        }
        throw new RuntimeException("Oh noes");
    }

    /**
     * Move up.
     * 
     * @return true, if successful
     */
    private boolean moveUp() {
        int x = playerPos.x;
        Position a = new Position(x, playerPos.y);
        Position b;
        Position c;
        if (a.y == 0) {
            b = new Position(x, board.length - 1);
            c = new Position(x, board.length - 2);
        } else if (a.y == 1) {
            b = new Position(x, 0);
            c = new Position(x, board.length - 1);
        } else {
            b = new Position(x, a.y - 1);
            c = new Position(x, a.y - 2);
        }
        return doMove(a, b, c, new Character('u'));
    }

    /**
     * Move down.
     * 
     * @return true, if successful
     */
    private boolean moveDown() {
        int x = playerPos.x;
        Position a = new Position(x, playerPos.y);
        Position b;
        Position c;
        if (a.y == board.length - 1) {
            b = new Position(x, 0);
            c = new Position(x, 1);
        } else if (a.y == board.length - 2) {
            b = new Position(x, a.y + 1);
            c = new Position(x, 0);
        } else {
            b = new Position(x, a.y + 1);
            c = new Position(x, a.y + 2);
        }
        return doMove(a, b, c, new Character('d'));
    }

    /**
     * Move right.
     * 
     * @return true, if successful
     */
    private boolean moveRight() {
        int y = playerPos.y;
        Position a = new Position(playerPos.x, y);
        Position b;
        Position c;
        int lineLength = board[y].length() - 1;
        if (a.x == lineLength) {
            b = new Position(0, y);
            c = new Position(1, y);
        } else if (a.x == lineLength - 1) {
            b = new Position(lineLength, y);
            c = new Position(0, y);
        } else {
            b = new Position(a.x + 1, y);
            c = new Position(a.x + 2, y);
        }
        return doMove(a, b, c, new Character('r'));
    }

    /**
     * Move left.
     * 
     * @return true, if successful
     */
    private boolean moveLeft() {
        int y = playerPos.y;
        Position a = new Position(playerPos.x, y);
        Position b;
        Position c;
        int lineLength = board[y].length() - 1;
        if (a.x == 0) {
            b = new Position(lineLength, y);
            c = new Position(lineLength - 1, y);
        } else if (a.x == 1) {
            b = new Position(0, y);
            c = new Position(lineLength, y);
        } else {
            b = new Position(a.x - 1, y);
            c = new Position(a.x - 2, y);
        }
        return doMove(a, b, c, new Character('l'));
    }

    /**
     * Undo up.
     * 
     * @param push
     *            the push
     * @return true, if successful
     */
    private boolean undoUp(boolean push) {
        Position a;
        Position b = new Position(playerPos.x, playerPos.y);
        Position c;
        if (b.y == board.length) {
            a = new Position(b.x, board.length - 1);
            c = new Position(b.x, 0);
        } else if (b.y == 0) {
            a = new Position(b.x, board.length);
            c = new Position(b.x, 1);
        } else {
            a = new Position(b.x, b.y + 1);
            c = new Position(b.x, b.y - 1);
        }
        return doUndo(a, b, c, push);
    }

    /**
     * Undo down.
     * 
     * @param push
     *            the push
     * @return true, if successful
     */
    private boolean undoDown(boolean push) {
        Position a;
        Position b = new Position(playerPos.x, playerPos.y);
        Position c;
        if (b.y == board.length) {
            c = new Position(b.x, board.length - 1);
            a = new Position(b.x, 0);
        } else if (b.y == 0) {
            c = new Position(b.x, board.length);
            a = new Position(b.x, 1);
        } else {
            c = new Position(b.x, b.y + 1);
            a = new Position(b.x, b.y - 1);
        }
        return doUndo(a, b, c, push);
    }

    /**
     * Undo right.
     * 
     * @param push
     *            the push
     * @return true, if successful
     */
    private boolean undoRight(boolean push) {
        Position a;
        Position b = new Position(playerPos.x, playerPos.y);
        Position c;
        int xl = board[b.y].length();
        if (b.x == xl) {
            a = new Position(xl - 1, b.y);
            c = new Position(0, b.y);
        } else if (b.x == 0) {
            a = new Position(xl, b.y);
            c = new Position(1, b.y);
        } else {
            a = new Position(b.x - 1, b.y);
            c = new Position(b.x + 1, b.y);
        }
        return doUndo(a, b, c, push);
    }

    /**
     * Undo left.
     * 
     * @param push
     *            the push
     * @return true, if successful
     */
    private boolean undoLeft(boolean push) {
        Position a;
        Position b = new Position(playerPos.x, playerPos.y);
        Position c;
        int xl = board[b.y].length();
        if (b.x == xl) {
            c = new Position(xl - 1, b.y);
            a = new Position(0, b.y);
        } else if (b.x == 0) {
            c = new Position(xl, b.y);
            a = new Position(1, b.y);
        } else {
            c = new Position(b.x - 1, b.y);
            a = new Position(b.x + 1, b.y);
        }
        return doUndo(a, b, c, push);
    }

    /**
     * Do move.
     * 
     * @param a
     *            the first coordinate
     * @param b
     *            the second coordinate
     * @param c
     *            the third coordinate
     * @param dir
     *            the direction, to add to log.
     * @return true, if successful
     */
    private boolean doMove(Position a, Position b, Position c, Character dir) {
        StringBuffer blocks = new StringBuffer();
        blocks.append(getChar(a));
        blocks.append(getChar(b));
        blocks.append(getChar(c));

        String sBlocks = blocks.toString();
        String result = sBlocks;

        switch (blocks.charAt(1)) {
        case ' ':
        case '.':
            result = moveToEmpty(sBlocks);
            dir = Character.toLowerCase(dir);
            break;
        case '$':
        case '*':
            result = moveToBox(sBlocks);
            dir = Character.toUpperCase(dir);
            break;
        }

        if (!sBlocks.equals(result)) {
            editLine(a, result.charAt(0));
            editLine(b, result.charAt(1));
            editLine(c, result.charAt(2));
            playerPos = b;
            moves.add(dir);
            dirtyBlocs.add(a);
            dirtyBlocs.add(b);
            dirtyBlocs.add(c);
            return true;
        } else
            return false;
    }

    /**
     * Do undo.
     * 
     * @param a
     *            the first coordinate
     * @param b
     *            the second coordinate
     * @param c
     *            the third coordinate
     * @param push
     *            Is this a push?
     * @return true, if successful
     */
    private boolean doUndo(Position a, Position b, Position c, Boolean push) {
        StringBuffer blocks = new StringBuffer();
        blocks.append(getChar(a));
        blocks.append(getChar(b));
        blocks.append(getChar(c));

        String sBlocks = blocks.toString();
        String result = sBlocks;
        if (!push) {
            result = undoEmpty(sBlocks);
        } else {
            result = undoBox(sBlocks);
        }
        if (!sBlocks.equals(result)) {
            editLine(a, result.charAt(0));
            editLine(b, result.charAt(1));
            editLine(c, result.charAt(2));
            playerPos = a;
            dirtyBlocs.add(a);
            dirtyBlocs.add(b);
            dirtyBlocs.add(c);
            return true;
        } else
            return false;
    }

    /**
     * Move to empty.
     * 
     * @param blocks
     *            the blocks
     * @return the modified blocks
     */
    private String moveToEmpty(String blocks) {
        StringBuffer buf = new StringBuffer(blocks);
        buf.setCharAt(0, (buf.charAt(0) == '+' ? '.' : ' '));
        buf.setCharAt(1, (buf.charAt(1) == '.' ? '+' : '@'));
        return buf.toString();
    }

    /**
     * Push box.
     * 
     * @param blocks
     *            the blocks
     * @return the modified blocks
     */
    private String moveToBox(String blocks) {
        char n2 = blocks.charAt(2);
        if (n2 == ' ' || n2 == '.') {
            StringBuffer buf = new StringBuffer(blocks);
            buf.setCharAt(0, (buf.charAt(0) == '+' ? '.' : ' '));
            buf.setCharAt(1, (buf.charAt(1) == '*' ? '+' : '@'));
            buf.setCharAt(2, (buf.charAt(2) == '.' ? '*' : '$'));
            return buf.toString();

        } else
            return blocks.toString();
    }

    /**
     * Undo empty.
     * 
     * @param blocks
     *            the blocks
     * @return the modified blocks
     */
    private String undoEmpty(String blocks) {
        StringBuffer buf = new StringBuffer(blocks);
        if (buf.charAt(0) == '.' || buf.charAt(0) == ' ') {
            buf.setCharAt(1, (buf.charAt(1) == '+' ? '.' : ' '));
            buf.setCharAt(0, (buf.charAt(0) == '.' ? '+' : '@'));
            return buf.toString();
        } else
            return blocks;
    }

    /**
     * Undo boxpush.
     * 
     * @param blocks
     *            the blocks
     * @return the modified blocks
     */
    private String undoBox(String blocks) {
        StringBuffer buf = new StringBuffer(blocks);
        if (buf.charAt(0) == '.' || buf.charAt(0) == ' ') {
            buf.setCharAt(0, (buf.charAt(0) == '.' ? '+' : '@'));
            buf.setCharAt(1, (buf.charAt(1) == '+' ? '*' : '$'));
            buf.setCharAt(2, (buf.charAt(2) == '*' ? '.' : ' '));
            return buf.toString();
        } else
            return blocks;
    }

    /**
     * Calc remain.
     */
    private void calcRemain() {
        remaining = 0;
        for (int i = 0; i < board.length; i++) {
            int l = board[i].length();
            for (int j = 0; j < l; j++) {
                if (board[i].charAt(j) == '.' || board[i].charAt(j) == '+')
                    remaining++;
            }
        }
    }

    /**
     * Edits the line.
     * 
     * @param pos
     *            the position to replace
     * @param replace
     *            the character to replace with
     */
    private void editLine(Position pos, char replace) {
        StringBuffer buf = new StringBuffer(board[pos.y]);
        buf.setCharAt(pos.x, replace);
        board[pos.y] = buf.toString();
    }

    /**
     * Pop dirty.
     * 
     * @return the position
     */
    private void popDirty() {
        Position pos;
        while ((pos = dirtyBlocs.poll()) != null) {
            for (LevelChangeListener lnr : listeners) {
                lnr.changedTile(new SquareMap(getChar(pos), pos));
            }
        }
    }

    /**
     * Gets the moves.
     * 
     * @return the moves
     */
    public String getMoves() {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < moves.size(); i++) {
            buff.append(moves.get(i));
        }
        return buff.toString();
    }

    /**
     * Gets the char for the provided position.
     * 
     * @param pos
     *            the position to get
     * @return the char at position
     */
    public synchronized char getChar(Position pos) {
        return board[pos.y].charAt(pos.x);
    }

    /**
     * Adds the listener.
     * 
     * @param listen
     *            the listener
     */
    public void addListener(LevelChangeListener listen) {
        if (!listeners.contains(listen)) {
            listeners.add(listen);
        }
    }

    /**
     * Removes the listener.
     * 
     * @param listen
     *            the listen
     */
    public void removeListener(LevelChangeListener listen) {
        listeners.remove(listen);
    }

}
// file:///Users/CVi/Documents/eclipse-py/TDT4100-ovinger/src/sokoban2/soko1.txt