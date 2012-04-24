/*
 * File: FlatLevel.java
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
package org.vikenpedia.sokoban.SokobanLevels;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Class FlatLevel.
 */
public class FlatLevelWithMoves extends LoadableLevel implements SavableLevel {

    /**
     * Instantiates a new flat level.
     * 
     * @param path
     *            the path
     */
    public FlatLevelWithMoves(String path) {
        process(load(path));
    }

    public FlatLevelWithMoves(Level lev) {
        this.title = new String(lev.getTitle());
        this.level = lev.getLines().clone();
        this.startMap = lev.getOrig().clone();
    }

    public FlatLevelWithMoves(File file) {
        process(load(file));
    }

    protected void process(String s) {
        List<String> lvl = new ArrayList<String>(Arrays.asList(s.split("\\n")));
        title = lvl.get(0);
        lvl.remove(0);
        int i = lvl.size() - 1;
        initialMoves = lvl.get(i);
        lvl.remove(i);
        level = new String[lvl.size()];
        lvl.toArray(level);
        startMap = level.clone();
    }

    @Override
    public void save(String moves, File file) {
        StringBuffer buf = new StringBuffer();
        buf.append(title);
        buf.append("\n");
        for (String s : startMap) {
            buf.append(s);
            buf.append("\n");
        }
        buf.append(moves);
        store(file, buf.toString());
    }
}
