/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.mobeggdrops.compatibility.common;

import com.github.crashdemons.mobeggdrops.InternalEggType;
import com.github.crashdemons.mobeggdrops.compatibility.CompatibilityProvider;
import com.github.crashdemons.mobeggdrops.compatibility.RuntimeReferences;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Tameable;

/**
 * CompatibilityProvider Implementation for 1.8-1.12.2 support.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
@SuppressWarnings("deprecation")
public abstract class Provider_common implements CompatibilityProvider {

    @Override
    public String getCompatibleNameFromEntity(Entity e) {
        if (isLegacyCat(e)) {
            return "CAT";
        }
        return e.getType().name().toUpperCase();
    }

    @Override
    public OfflinePlayer getOfflinePlayerByName(String username) {
        return Bukkit.getOfflinePlayer(username);
    }

    protected boolean isLegacyCat(Entity e) {
        if (e instanceof Ocelot && e instanceof Tameable) {
            return ((Tameable) e).isTamed();
        }

        return false;
    }
    
    @Override
    public Material getEffectiveCatEgg(){
        Material mat = RuntimeReferences.getMaterialByName("CAT_SPAWN_EGG");
        if(mat==null) mat = RuntimeReferences.getMaterialByName("OCELOT_SPAWN_EGG");
        return mat;
    }
    
    @Override
    public Material getEggFromEntityType(EntityType type){
        //System.out.println("getegg "+type.name());
        switch(type){
            case PIG_ZOMBIE:
                return Material.ZOMBIE_PIGMAN_SPAWN_EGG;
            case MUSHROOM_COW:
                return Material.MOOSHROOM_SPAWN_EGG;
        }
        String eggname = type.name()+"_SPAWN_EGG";
        Material mat = RuntimeReferences.getMaterialByName(eggname);
        //if(mat==null && plugin!=null) plugin.getLogger().info("Missing spawn egg for: "+type.name());
        return mat;
    }
    
    @Override
    public Material getEggFromEntity(Entity e){
        if(isLegacyCat(e)) return getEffectiveCatEgg();
        return getEggFromEntityType(e.getType());
    }
    
    @Override
    public InternalEggType getInternalEggFromEntityType(EntityType t){
        try{
            return InternalEggType.valueOf(t.name().toUpperCase());
        }catch(Exception ex){
            return null;
        }
    }
    @Override
    public InternalEggType getInternalEggFromEntity(Entity e){
        if(isLegacyCat(e)) return InternalEggType.CAT;
        return getInternalEggFromEntityType(e.getType());
    }
}
