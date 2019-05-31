/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.shininet.bukkit.mobeggdrops;

import com.github.crashdemons.mobeggdrops.InternalEggType;
import com.github.crashdemons.mobeggdrops.compatibility.Compatibility;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import com.github.crashdemons.mobeggdrops.events.MobDropEggEvent;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;
import com.github.crashdemons.mobeggdrops.events.EggRollEvent;
import org.bukkit.entity.EntityType;

/**
 * Defines a listener for mobeggdrops events.
 * <p>
 * <i>Note:</i> This documentation was inferred after the fact and may be
 * inaccurate.
 *
 * @author meiskam
 */
class MobEggDropsListener implements Listener {

    private final Random prng = new Random();
    private final MobEggDrops plugin;

    public void registerAll() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void unregisterAll() {
        EntityDeathEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerJoinEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
    }


    protected MobEggDropsListener(MobEggDrops plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        org.bukkit.configuration.file.FileConfiguration cfg = plugin.configFile; //just to make the following more readable
    }
    
    private Entity getEntityOwningEntity(EntityDamageByEntityEvent event){
        Entity entity = event.getDamager();
        if(entity instanceof Projectile){
            //System.out.println("   damager entity projectile");
            Projectile projectile = (Projectile) entity;
            ProjectileSource shooter = projectile.getShooter();
            if(shooter instanceof Entity){
                entity=(Entity) shooter;
                //if(entity!=null) System.out.println("   arrow shooter: "+entity.getType().name()+" "+entity.getName());
            }
        }else if(entity instanceof Tameable && plugin.configFile.getBoolean("considertameowner")){
            //System.out.println("   damager entity wolf");
            Tameable animal = (Tameable) entity;
            if(animal.isTamed()){
                AnimalTamer tamer = animal.getOwner();
                if(tamer instanceof Entity){
                    entity=(Entity) tamer;
                    //if(entity!=null) System.out.println("   wolf tamer: "+entity.getType().name()+" "+entity.getName());
                }
            }
        }
        return entity;
    }
    
    private LivingEntity getKillerEntity(EntityDeathEvent event){
        LivingEntity victim = event.getEntity();
        //if(victim!=null) System.out.println("victim: "+victim.getType().name()+" "+victim.getName());
        LivingEntity killer = victim.getKiller();
        //if(killer!=null) System.out.println("original killer: "+killer.getType().name()+" "+killer.getName());
        
        if(killer==null && plugin.configFile.getBoolean("considermobkillers")){
            EntityDamageEvent dmgEvent = event.getEntity().getLastDamageCause();
            if(dmgEvent instanceof EntityDamageByEntityEvent){
                Entity killerEntity = getEntityOwningEntity((EntityDamageByEntityEvent)dmgEvent);
                //if(killerEntity!=null) System.out.println(" parent killer: "+killerEntity.getType().name()+" "+killerEntity.getName());
                if(killerEntity instanceof LivingEntity) killer=(LivingEntity)killerEntity;
                //what if the entity isn't living (eg: arrow?)
            }
        }
        //if(killer!=null) System.out.println(" final killer: "+killer.getType().name()+" "+killer.getName());
        return killer;
    }
    

    /**
     * Event handler for entity deaths.
     * <p>
     * Used to determine when eggs should be dropped.
     *
     * @param event the event received
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntityType()==EntityType.PLAYER) return;
        LivingEntity killer = getKillerEntity(event);

        double lootingrate = 1;

        if (killer != null) {
            ItemStack weapon = Compatibility.getProvider().getItemInMainHand(killer);//killer.getEquipment().getItemInMainHand();
            if(weapon!=null){
                if(plugin.configFile.getBoolean("requireitem")){
                    String weaponType = weapon.getType().name().toLowerCase();
                    if(!plugin.configFile.getStringList("requireditems").contains(weaponType)) return;
                }
                lootingrate = 1 + (plugin.configFile.getDouble("lootingrate") * weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
            }
        }

        InternalEggType eggType = Compatibility.getProvider().getInternalEggFromEntity(event.getEntity());
                
        if (eggType == null) {
            return;//entity type is one we don't support - don't attempt to handle eggs for it.
        }
        String mobDropConfig = eggType.getConfigName();
        Double droprate = plugin.configFile.getDouble(mobDropConfig);
        switch (eggType) {
            case WITHER_SKELETON:
                if (droprate < 0) {
                    return;//if droprate is <0, don't modify drops
                }
                MobDeathHelper(event, eggType, droprate, lootingrate);
                break;
            default:
                MobDeathHelper(event, eggType, droprate, lootingrate);
                break;
        }
    }

    private void MobDeathHelper(EntityDeathEvent event, InternalEggType type, Double droprateOriginal, Double lootingModifier) {
        Double droprate = droprateOriginal * lootingModifier;
        Double dropchanceRand = prng.nextDouble();
        Double dropchance = dropchanceRand;
        Entity entity = event.getEntity();
        Player killer = event.getEntity().getKiller();

        boolean killerAlwaysBeeggs = false;
        if (killer != null) {//mob was PK'd
            if (!killer.hasPermission("mobeggdrops.canberewarded")) {
                return;//killer does not have permission to beegg mobs in any case
            }
            killerAlwaysBeeggs = killer.hasPermission("mobeggdrops.alwaysrewarded");
            if (killerAlwaysBeeggs) {
                dropchance = 0.0;//alwaysbeegg should only modify drop chances
            }
        } else {//mob was killed by mob
            if (plugin.configFile.getBoolean("mobpkonly")) {
                return;//mobs must only be beegged by players
            }
        }
        boolean eggDropSuccess = dropchance < droprate;

        EggRollEvent rollEvent = new EggRollEvent(killer, event.getEntity(), killerAlwaysBeeggs, lootingModifier, dropchanceRand, dropchance, droprateOriginal, droprate, eggDropSuccess);
        plugin.getServer().getPluginManager().callEvent(rollEvent);
        if (!rollEvent.succeeded()) {
            return;//allow plugins a chance to modify the success
        }

        ItemStack drop = plugin.api.getEggDrop(entity);

        MobDropEggEvent dropEggEvent = new MobDropEggEvent(event.getEntity(), drop);
        plugin.getServer().getPluginManager().callEvent(dropEggEvent);

        if (dropEggEvent.isCancelled()) {
            return;
        }

        if (plugin.configFile.getBoolean("antideathchest")) {
            Location location = event.getEntity().getLocation();
            location.getWorld().dropItemNaturally(location, drop);
        } else {
            event.getDrops().add(drop);
        }
        //broadcast message about the beegging.
        if (plugin.configFile.getBoolean("broadcastmob") && killer!=null) { //mob-on-mob broadcasts would be extremely annoying!
            String entityName = entity.getCustomName​();
            if (entityName==null) entityName = entity.getName(); //notnull
            
            
            String message = Formatter.format(Lang.BEHEAD_OTHER, entityName + ChatColor.RESET, killer.getDisplayName() + ChatColor.RESET);

            int broadcastRange = plugin.configFile.getInt("broadcastmobrange");
            if (broadcastRange > 0) {
                broadcastRange *= broadcastRange;
                Location location = entity.getLocation();
                List<Player> players = entity.getWorld().getPlayers();

                for (Player loopPlayer : players) {
                    try{
                        if (location.distanceSquared(loopPlayer.getLocation()) <= broadcastRange) {
                            loopPlayer.sendMessage(message);
                        }
                    }catch(IllegalArgumentException e){
                        //entities are in different worlds
                    }
                }
            } else {
                plugin.getServer().broadcastMessage(message);
            }
        }
    }




    //private ItemStack createConvertedMobegg(InternalEggType eggType, boolean unused, boolean addLore, int quantity) {
    //    return SkullManager.MobSkull(eggType, quantity, true, addLore);
    //}

    

    /**
     * Event handler for player server join events
     * <p>
     * Used to send updater information to appropriate players on join.
     *
     * @param event the event received
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("mobeggdrop.update") && plugin.getUpdateReady()) {
            Formatter.formatMsg(player, Lang.UPDATE1, plugin.getUpdateName());
            Formatter.formatMsg(player, Lang.UPDATE3, "http://curse.com/bukkit-plugins/minecraft/" + Config.updateSlug);
        }
    }
}
