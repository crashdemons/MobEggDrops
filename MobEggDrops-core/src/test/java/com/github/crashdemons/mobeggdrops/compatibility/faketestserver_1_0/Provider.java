/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.mobeggdrops.compatibility.faketestserver_1_0;

import com.github.crashdemons.mobeggdrops.compatibility.CompatibilityProvider;
import com.github.crashdemons.mobeggdrops.compatibility.common.Provider_common;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * CompatibilityProvider Implementation for 1.13+ support
 * @author crashdemons (crashenator at gmail.com)
 */
@SuppressWarnings( "deprecation" )
public class Provider extends Provider_common implements CompatibilityProvider  {
    public Provider(){}
    @Override public String getType(){ return "faketestserver"; }
    @Override public String getVersion(){ return "1.0"; }

    @Override public ItemStack getItemInMainHand(Player p){ return null; }
    @Override public ItemStack getItemInMainHand(LivingEntity p){ return null; }
    @Override public void setItemInMainHand(Player p,ItemStack s){  }

    @Override public String getCompatibleNameFromEntity(Entity e){ return e.getType().name().toUpperCase(); }
    @Override public OfflinePlayer getOfflinePlayerByName(String username){ return null; }
 
    
}
