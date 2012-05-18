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
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import de.minestar.minestarHotline.hotlines.Hotline;
import de.minestar.minestarHotline.hotlines.MailHotline;
import de.minestar.minestarlibrary.data.Entry;

public class HotlineManager implements Runnable {

    // THE REGISTERED HOTLINES
    private List<Hotline> hotlines = new ArrayList<Hotline>(8);
    private Map<Class<? extends Hotline>, Hotline> hotlineTypes = new HashMap<Class<? extends Hotline>, Hotline>();

    private Queue<Entry<String, String>> bufferedMessages = new LinkedBlockingQueue<Entry<String, String>>();

    public HotlineManager(File dataFolder) {
        this.registerHotlines(dataFolder);
        this.dontKnowTheNameOfThisFunctionBecauseThisIsVerySimiliarToRegisterHotlines();
    }

    // REGISTER HERE ALL HOTLINES
    private void registerHotlines(File dataFolder) {
        // MAIL HOTLINE
        hotlines.add(new MailHotline(dataFolder));
    }

    // METHOD TO PUT THE REGISTERD HOTLINES TO A MAP
    private void dontKnowTheNameOfThisFunctionBecauseThisIsVerySimiliarToRegisterHotlines() {
        for (Hotline hotline : hotlines)
            hotlineTypes.put(hotline.getClass(), hotline);
    }

    // SOMEONE USES THE HOTLINE
    public void useHotline(String callerName, String message) {
        // BUFFER THE MESSAGES
        bufferedMessages.add(new Entry<String, String>(callerName, message));
    }

    // CLOSE ALL HOTLINES
    public synchronized void closeHotlines() {
        run();
        for (Hotline hotline : hotlines)
            hotline.closeHotline();
    }

    // CLOSE ONE SPECIFIC HOTLINE
    public synchronized void closeHotline(Class<? extends Hotline> hotlineClass) {
        run();
        hotlineTypes.get(hotlineClass).closeHotline();

    }

    // REGISTER ONE SUPPORTER TO A SPECIFIC HOTLINE
    public synchronized void registerSupporter(String supporterName, String contactInformation, Class<? extends Hotline> hotlineClass) {
        hotlineTypes.get(hotlineClass).registerSupporter(supporterName, contactInformation);
    }

    // DEREGISTER ONE SUPPORTER TO A SPECIFIC HOTLINE
    public synchronized void deregisterSupporter(String supporterName, String contactInformation, Class<? extends Hotline> hotlineClass) {
        hotlineTypes.get(hotlineClass).deregisterSupporter(supporterName, contactInformation);
    }

    @Override
    public void run() {
        if (bufferedMessages.isEmpty())
            return;

        // HANDLE ALL BUFFERED MESSAGES
        for (Entry<String, String> entry : bufferedMessages) {
            for (Hotline hotline : hotlines) {
                hotline.sendMessage(entry.getKey(), entry.getValue());
            }
        }
        bufferedMessages.clear();
    }
}
