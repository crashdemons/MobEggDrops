/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.mobeggdrops;

import com.github.crashdemons.mobeggdrops.compatibility.Compatibility;
import com.github.crashdemons.mobeggdrops.compatibility.RuntimeReferences;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.shininet.bukkit.mobeggdrops.Formatter;
import org.shininet.bukkit.mobeggdrops.Lang;
import com.github.crashdemons.mobeggdrops.api.EggType;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public enum InternalEggType implements EggType  {
    ELDER_GUARDIAN,
    WITHER_SKELETON,
    STRAY,
    HUSK,
    ZOMBIE_VILLAGER,
    SKELETON_HORSE,
    ZOMBIE_HORSE,
    ARMOR_STAND,
    DONKEY,
    MULE,
    EVOKER,
    VEX,
    VINDICATOR,
    ILLUSIONER,
    CREEPER,
    SKELETON,
    SPIDER,
    GIANT,
    ZOMBIE,
    SLIME,
    GHAST,
    PIG_ZOMBIE,
    ENDERMAN,
    CAVE_SPIDER,
    SILVERFISH,
    BLAZE,
    MAGMA_CUBE,
    ENDER_DRAGON,
    WITHER,
    BAT,
    WITCH,
    ENDERMITE,
    GUARDIAN,
    SHULKER,
    PIG,
    SHEEP,
    COW,
    CHICKEN,
    SQUID,
    WOLF,
    MUSHROOM_COW,
    SNOWMAN,
    OCELOT,
    IRON_GOLEM,
    HORSE,
    RABBIT,
    POLAR_BEAR,
    LLAMA,
    PARROT,
    VILLAGER,
    TURTLE,
    PHANTOM,
    COD,
    SALMON,
    PUFFERFISH,
    TROPICAL_FISH,
    DROWNED,
    DOLPHIN,
    CAT,
    PANDA,
    PILLAGER,
    RAVAGER,
    TRADER_LLAMA,
    WANDERING_TRADER,
    FOX;
    
    /**
     * Finds the skulltype that has the provided Spawn-Name associated with it
     *
     * @param spawnname The spawn-name to find the skulltype for.
     * @return if found: a TexturedSkullType, otherwise: null.
     */
    public static InternalEggType getBySpawnName(String spawnname) {
        if (spawnname.isEmpty()) {
            return null;
        }
        for (InternalEggType type : InternalEggType.values()) {
            if (type.getSpawnName().equalsIgnoreCase(spawnname)) {
                return type;
            }
        }
        return null;
    }
    /**
     * Get the droprate config name (key) for the given skulltype.
     *
     * @return A string containing the config entry name (key) for the skulltype
     */
    public String getConfigName() {
        return name().replace("_", "").toLowerCase() + "droprate";
    }
    /**
     * Gets the item displayname for the associated skulltype, as defined in the
     * "lang" file.
     *
     * @return A string containing the skulltype's displayname
     */
    @Override
    public String getDisplayName() {
        return Formatter.format(Lang.getString("EGG_" + name()));
    }
    /**
     * Get the "spawn" name for the associated skulltype, as defined in the
     * "lang" file.
     * <p>
     * This string is used to spawn-in the skull in external commands.
     *
     * @return A string containing the spawnname.
     */
    public String getSpawnName() {
        return Lang.getString("EGG_SPAWN_" + name());
    }
    
    @Override
    public Material getMaterial(){
        if(this.name().equals("CAT")) return Compatibility.getProvider().getEffectiveCatEgg();
        EntityType type = RuntimeReferences.getEntityTypeByName(this.name());
        return Compatibility.getProvider().getEggFromEntityType(type);
    }
    
    /**
     * Implement API requirement that can't see this enum's type.
     * @return 
     */
    @Override
    public Enum toEnum() {
        return this;
    }
}
