/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.mobeggdrops.compatibility;

import com.github.crashdemons.mobeggdrops.InternalEggType;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * An interface specifying all of the methods we need for our plugin that require differing Bukkit-specific implementations which we wish to abstract from our plugin code.
 * @author crashdemons (crashenator at gmail.com)
 */
public interface CompatibilityProvider {
    /**
     * Retrieve the server type the provider implements code for.
     * @return the server type string
     */
    public String getType();
    
    /**
     * Retrieve the version string associated with the specific Compatibility Provider implementation.
     * @return the version string
     */
    public String getVersion();
  
    /**
     * Gets the itemstack in the [main] hand of a player
     * @param p the player to check
     * @return The ItemStack if found, or null
     */
    public ItemStack getItemInMainHand(Player p);
    /**
     * Sets the itemstack in the [main] hand of a player
     * @param p the player to change
     * @param s the itemstack to set in their hand
     */
    public void setItemInMainHand(Player p,ItemStack s);

    /**
     * Gets a forward-portable name of an entity.
     * 
     * This gets the name of the entity as it appears in the EntityType enum of newer bukkit versions, even if the mob is a variant in the current version.
     * This value can be used to correspond entities to their heads.
     * @param e the entity to check
     * @return the portable name of the string.
     * @see org.bukkit.entity.EntityType
     */
    public String getCompatibleNameFromEntity(Entity e);//determine forward-portable name of entity even if they are variants.

    /**
     * Gets a player by their username
     * @param username the username of the player
     * @return the offline-player
     */
    public OfflinePlayer getOfflinePlayerByName(String username);
    
    
    public Material getEffectiveCatEgg();
    public Material getEggFromEntityType(EntityType t);
    public Material getEggFromEntity(Entity e);
    public InternalEggType getInternalEggFromEntityType(EntityType t);
    public InternalEggType getInternalEggFromEntity(Entity e);
    
    //----------- 5.0 providers -----------//
    public ItemStack getItemInMainHand(LivingEntity p);
}
