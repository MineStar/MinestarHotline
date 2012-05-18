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

import de.minestar.minestarHotline.manager.HotlineManager;
import de.minestar.minestarlibrary.AbstractCore;

public class Core extends AbstractCore {

    public static final String NAME = "MinestarHotline";

    private static HotlineManager hotlineManager;

    public Core() {
        super(NAME);
    }

    @Override
    protected boolean createManager() {
        hotlineManager = new HotlineManager(getDataFolder());
        return true;
    }

    @Override
    protected boolean commonDisable() {
        hotlineManager.closeHotlines();
        return true;
    }

    public static HotlineManager getManager() {
        return hotlineManager;
    }

}
