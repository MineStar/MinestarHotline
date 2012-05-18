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

package de.minestar.minestarHotline.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.minestar.minestarHotline.hotlines.Hotline;
import de.minestar.minestarHotline.hotlines.MailHotline;

public class HotlineManager {

    // THE REGISTERED HOTLINES
    private List<Hotline> hotlines = new ArrayList<Hotline>(8);
    private Map<Class<? extends Hotline>, Hotline> hotlineTypes = new HashMap<Class<? extends Hotline>, Hotline>();

    public HotlineManager(File dataFolder) {
        this.registerHotlines(dataFolder);
        this.dontKnowTheNameOfThisFunctionBecauseThisIsVerySimiliarToRegisterHotlines();
    }

    // REGISTER HERE ALL HOTLINES
    private void registerHotlines(File dataFolder) {
        // MAIL HOTLINE
        hotlines.add(new MailHotline(dataFolder));
    }

    private void dontKnowTheNameOfThisFunctionBecauseThisIsVerySimiliarToRegisterHotlines() {
        for (Hotline hotline : hotlines)
            hotlineTypes.put(hotline.getClass(), hotline);

    }

    // SOMEONE USES THE HOTLINE
    public void useHotline(String callerName, String message) {
        for (Hotline hotline : hotlines)
            hotline.sendMessage(callerName, message);
    }

    // CLOSE ALL HOTLINES
    public void closeHotlines() {
        for (Hotline hotline : hotlines)
            hotline.closeHotline();
    }

    // CLOSE ONE SPECIFIC HOTLINE
    public void closeHotline(Class<? extends Hotline> hotlineClass) {
        hotlineTypes.get(hotlineClass).closeHotline();
    }

    // REGISTER ONE SUPPORTER TO A SPECIFIC HOTLINE
    public void registerSupporter(String supporterName, String contactInformation, Class<? extends Hotline> hotlineClass) {
        hotlineTypes.get(hotlineClass).registerSupporter(supporterName, contactInformation);
    }

    // DEREGISTER ONE SUPPORTER TO A SPECIFIC HOTLINE
    public void deregisterSupporter(String supporterName, String contactInformation, Class<? extends Hotline> hotlineClass) {
        hotlineTypes.get(hotlineClass).deregisterSupporter(supporterName, contactInformation);
    }
}
