/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.mobeggdrops.api;

import com.github.crashdemons.mobeggdrops.EggManager;
import com.github.crashdemons.mobeggdrops.InternalEggType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.shininet.bukkit.mobeggdrops.MobEggDrops;
import com.github.crashdemons.mobeggdrops.MobEggDropsPlugin;
import com.github.crashdemons.mobeggdrops.compatibility.Compatibility;

/**
 * Implements the API by wrapping internal methods
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class ApiProvider implements MobEggDropsAPI {

    private final MobEggDrops plugin;

    public ApiProvider(MobEggDropsPlugin plugin) {
        this.plugin = (MobEggDrops) plugin;
    }

    @Override
    public MobEggDropsPlugin getPlugin() {
        return plugin;
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }




    @Override
    public EggType getEggOf(Entity e) {
        return Compatibility.getProvider().getInternalEggFromEntity(e);
    }

    @Override
    public EggType getEggOf(EntityType t) {

        return Compatibility.getProvider().getInternalEggFromEntityType(t);
    }

    @Override
    public ItemStack getEggItem(EggType h, int num) {
        InternalEggType type = eggFromApiEgg(h);
        if (type == null) {
            return null;
        }

        boolean addLore = plugin.configFile.getBoolean("addlore");
        return EggManager.MobEgg(type, num, true, addLore);
    }

    @Override
    public ItemStack getEggDrop(Entity e) {
        InternalEggType type = Compatibility.getProvider().getInternalEggFromEntity(e);
        if (type == null) {
            return null;
        }
        boolean addLore = plugin.configFile.getBoolean("addlore");
        ItemStack drop = EggManager.MobEgg(type, true, addLore);
        return drop;
    }

    private InternalEggType eggFromApiEgg(EggType h) {
        return (InternalEggType) h;
    }
}
