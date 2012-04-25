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
 * The Class FlatLevel. Simple level with no special encoding.
 */
public class FlatLevel extends LoadableLevel {

    /**
     * Instantiates a new flat-format level from a file path.
     * 
     * @param path
     *            the path to the level file
     */
    public FlatLevel(String path) {
        process(load(path));

    }

    /**
     * Instantiates a new flat-format level from a File resource.
     * 
     * @param file
     *            the file resource of the level file
     */
    public FlatLevel(File file) {
        process(load(file));
    }

    /**
     * Process.
     * 
     * @param s
     *            the level as a single string to be processed.
     */
    protected void process(String s) {
        List<String> lvl = new ArrayList<String>(Arrays.asList(s.split("\\n")));
        title = lvl.get(0);
        lvl.remove(0);
        level = new String[lvl.size()];
        lvl.toArray(level);
        startMap = level.clone();
    }
}
