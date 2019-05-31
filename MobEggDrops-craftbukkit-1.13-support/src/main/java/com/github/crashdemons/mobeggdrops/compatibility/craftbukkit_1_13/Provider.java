/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.mobeggdrops.compatibility.craftbukkit_1_13;

import com.github.crashdemons.mobeggdrops.compatibility.CompatibilityProvider;
import com.github.crashdemons.mobeggdrops.compatibility.common.Provider_common;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * CompatibilityProvider Implementation for 1.13+ support
 * @author crashdemons (crashenator at gmail.com)
 */
@SuppressWarnings( "deprecation" )
public class Provider extends Provider_common implements CompatibilityProvider {
    public Provider(){}
    @Override public String getType(){ return "craftbukkit"; }
    @Override public String getVersion(){ return "1.13"; }

    //@Override public boolean setOwner(SkullMeta skullItemMeta, String owner){ return skullItemMeta.setOwner(owner); }
    //@Override public boolean setOwner(Skull skullBlockState, String owner){ return skullBlockState.setOwner(owner); }
    @Override public ItemStack getItemInMainHand(LivingEntity p){ 
        EntityEquipment equipment = p.getEquipment();
        if(equipment == null) return null;
        return equipment.getItemInMainHand(); 
    }
    @Override public ItemStack getItemInMainHand(Player p){ return p.getEquipment().getItemInMainHand(); }
    @Override public void setItemInMainHand(Player p,ItemStack s){ p.getEquipment().setItemInMainHand(s); }
 
}
