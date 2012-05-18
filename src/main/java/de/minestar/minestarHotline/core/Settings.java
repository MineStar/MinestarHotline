/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of MinestarHotline.
 * 
 * MinestarHotline is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MinestarHotline is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MinestarHotline.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.minestarHotline.core;

import java.io.File;

import de.minestar.minestarlibrary.config.MinestarConfig;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class Settings {

    // INTERVALL CHECKING THE QUEUE SIZE - SECONDS
    public static long QUEUE_INTERVALL = 15L;

    private Settings() {
    }

    public static boolean init(File dataFolder) {
        File configFile = new File(dataFolder, "config.yml");
        try {
            if (configFile.exists())
                loadValues(new MinestarConfig(configFile));
            else
                loadValues(MinestarConfig.copyDefault(Settings.class.getResourceAsStream("/config.yml"), configFile));
            return true;
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't load the settings from " + configFile);
            return false;
        }
    }

    private static void loadValues(MinestarConfig config) throws Exception {
        // Value * 20 to get seconds
        QUEUE_INTERVALL = config.getLong("HotlineThread.Intervall", QUEUE_INTERVALL);
    }
}
