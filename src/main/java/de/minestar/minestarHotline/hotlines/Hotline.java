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

package de.minestar.minestarHotline.hotlines;

public interface Hotline {

    /**
     * Sending a message to the hotline
     * 
     * @param callerName
     *            The players name using the hotline
     * @param message
     *            The message of the player
     */
    public void sendMessage(String callerName, String message);

    /**
     * Register a supporter to the hotline
     * 
     * @param supportName
     *            The supportes name
     * @param contactInformation
     *            The contact information to the supporter on the hotline
     */
    public void registerSupporter(String supportName, String contactInformation);

    /**
     * Remove a supporter from the hotline
     * 
     * @param supportName
     *            The supportes name
     * @param contactInformation
     *            The contact information to the supporter on the hotline
     */
    public void deregisterSupporter(String suppportName, String contactInformation);

    /**
     * Check whether the supporter is registered to the hotline
     * 
     * @param supportName
     *            The supporters name
     * @return <code>True</code> when the supportes name was registered to the
     *         hotline
     */
    public boolean isSupporterRegistered(String supportName);

    /**
     * Close the hotline and release all resources
     */
    public void closeHotline();

}
