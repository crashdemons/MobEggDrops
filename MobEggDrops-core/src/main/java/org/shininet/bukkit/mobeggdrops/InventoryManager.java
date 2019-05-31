/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package org.shininet.bukkit.mobeggdrops;

import com.github.crashdemons.mobeggdrops.EggManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

/**
 * Defines a collection of utility methods for the plugin inventory management, item management
 * @author meiskam
 */
public final class InventoryManager {
    private InventoryManager(){}
    
    /**
     * Adds a head-item to a player's inventory.
     * 
     * The quantity of heads added to the inventory is controlled by Config.defaultStackSize (usually 1).
     * @param player the player receiving the head.
     * @param skullOwner the owner username of the head-item, or the "spawn" string for a supported skull.
     * @param unused unused
     * @param addLore controls whether lore text can be added to the head by the plugin
     * @return true: the head was added successfully. false: there was no empty inventory slot to add the item.
     * @see Config#defaultStackSize
     */
    @SuppressWarnings("unused")
    public static boolean addEgg(Player player, String skullOwner, boolean unused, boolean addLore) {
        return addEgg(player, skullOwner, Config.defaultStackSize, unused, addLore);
    }

    /**
     * Adds a head-item to a player's inventory.
     * @param player the player receiving the head.
     * @param skullOwner the owner username of the head-item, or the "spawn" string for a supported skull.
     * @param quantity the number of this item to add
     * @param unused whether vanilla mobheads are permitted (if relevant to the owner parameter).
     * @param addLore controls whether lore text can be added to the head by the plugin
     * @return true: the head was added successfully. false: there was no empty inventory slot to add the item.
     */
    public static boolean addEgg(Player player, String skullOwner, int quantity, boolean unused,boolean addLore) {
        PlayerInventory inv = player.getInventory();
        int firstEmpty = inv.firstEmpty();
        if (firstEmpty == -1) {
            return false;
        } else {
            inv.setItem(firstEmpty, EggManager.spawnEgg(skullOwner, quantity, true, addLore));
            return true;
        }
    }
}
