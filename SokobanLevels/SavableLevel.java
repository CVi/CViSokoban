package org.vikenpedia.sokoban.SokobanLevels;

import java.io.File;

public interface SavableLevel extends Level {
    public void save(String moves, File file);
}
