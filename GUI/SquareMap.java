package org.vikenpedia.sokoban.GUI;

import org.vikenpedia.sokoban.Position;

public class SquareMap {
    public final char type;
    public final Position pos;

    public SquareMap(char type, Position pos) {
        this.type = type;
        this.pos = pos;
    }
}
