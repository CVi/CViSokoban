/*
 * File: LoadableLevel.java
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The Class LoadableLevel.
 */
public abstract class LoadableLevel implements Level {

    /** The title. */
    protected String title = "";

    protected String initialMoves = "";

    public String getTitle() {
        return title;
    }

    public String getInitialMoves() {
        return initialMoves;
    }

    /** The level. */
    protected String[] level;

    protected String[] startMap;

    public String[] getLines() {
        return level;
    }

    public String[] getOrig() {
        return startMap;
    }

    /**
     * Load.
     * 
     * @param path
     *            the path
     * @return the string loaded from the file
     */
    protected String load(String path) {
        URL url;
        StringBuffer level = new StringBuffer();
        try {
            url = new URL(path);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            String line;
            while ((line = input.readLine()) != null) {
                level.append(line);
                level.append("\n");
            }

            input.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return level.toString();
    }

    protected String load(File file) {
        StringBuffer level = new StringBuffer();
        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            String line;
            while ((line = input.readLine()) != null) {
                level.append(line);
                level.append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return level.toString();
    }

    public void reset() {
        level = startMap.clone();
    }

    protected void store(File file, String content) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(content);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
