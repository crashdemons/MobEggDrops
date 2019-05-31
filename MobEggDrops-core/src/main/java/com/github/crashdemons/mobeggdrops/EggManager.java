
package com.github.crashdemons.mobeggdrops;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.shininet.bukkit.mobeggdrops.Config;
import org.shininet.bukkit.mobeggdrops.Lang;

/**
 * Defines an abstract class of methods for creating, updating, and applying information to heads managed by the plugin.
 * @author crash
 */
public final class EggManager {
    
    private EggManager(){}
    
    /**
     * Applies Lore text (including the PlayerHeads plugin name) to a playerhead's meta.
     * @param headMeta The ItemMeta associated with the playerhead to modify
     * @param extra Extra lore text to display under the "PlayerHeads" line.
     */
    private static void applyLore(ItemMeta headMeta,String extra){
        ArrayList<String> lore = new ArrayList<>();
        //lore.add(" ");
        if(!Lang.LORE_PLUGIN_NAME.isEmpty()) lore.add(ChatColor.BLUE+""+ChatColor.ITALIC+Lang.LORE_PLUGIN_NAME);
        if(!extra.isEmpty()) lore.add(extra);
        headMeta.setLore(lore);
    }
    
    /**
     * Sets a display name for the playerhead item's meta
     * @param headMeta The ItemMeta associated with the playerhead to modify
     * @param display The string containing the display name to set
     */
    private static void applyDisplayName(ItemMeta headMeta,String display){
        if(display!=null) headMeta.setDisplayName(display);
    }
    

    
    /**
     * Creates a stack of heads for the specified Mob's SkullType.
     * 
     * The quantity of heads will be defined by Config.defaultStackSize (usually 1)
     * 
     * @param type The InternalEggType to create heads of.
     * @param useVanillaHeads Whether to permit vanilla head-items to be used in place of custom playerheads for supported mobs.
     * @param addLore controls whether any lore text should be added to the head (is currently applied only to custom heads).
     * @return The ItemStack of heads desired.
     * @see org.shininet.bukkit.playerheads.Config#defaultStackSize
     */
    public static ItemStack MobEgg(InternalEggType type, boolean useVanillaHeads, boolean addLore){
        return MobEgg(type,Config.defaultStackSize, useVanillaHeads, addLore);
    }
    
    /**
     * Creates a stack of heads for the specified Mob's SkullType
     * @param type The InternalEggType to create heads of.
     * @param quantity the number of heads to create in the stack
     * @param unused unused
     * @param addLore controls whether any lore text should be added to the head (is currently applied only to custom heads).
     * @return The ItemStack of heads desired.
     */
    public static ItemStack MobEgg(InternalEggType type,int quantity,boolean unused, boolean addLore){
        Material mat = type.getMaterial();
        if(mat==null) return null;
       
        //System.out.println("Player-head");
        ItemStack stack = new ItemStack(mat,quantity);
        ItemMeta headMeta = (ItemMeta) stack.getItemMeta();

        String displayName = type.getDisplayName();
        if(displayName!=null && !displayName.isEmpty())
            applyDisplayName(headMeta,ChatColor.RESET + "" + ChatColor.YELLOW + displayName);

        if(addLore) applyLore(headMeta,ChatColor.GREEN+Lang.LORE_HEAD_MOB);
        stack.setItemMeta(headMeta);
        return stack;
    }
    
    
    /**
     * Spawns a skull itemstack using an input spawn string or username.
     * 
     * The quantity of heads will be defined by Config.defaultStackSize (usually 1)
     * @param spawnString the spawn string indicating the type of skull or username indicating the owner of the skull.
     * @param unused unused
     * @param addLore controls whether any lore text should be added to the head. (currently only applied to custom mobheads and player heads)
     * @return The skull itemstack desired. If the spawn string is recognized, this will be the corresponding entity's head, otherwise it will be a playerhead for the name supplied.
     */
    public static ItemStack spawnEgg(String spawnString, boolean unused, boolean addLore){
        return spawnEgg(spawnString,Config.defaultStackSize,unused,addLore);
    }
    
    /**
     * Spawns a skull itemstack using an input spawn string or username.
     * @param spawnString the spawn string indicating the type of skull or username indicating the owner of the skull.
     * @param quantity the number of items to spawn in this stack.
     * @param unused Whether to permit vanilla head-items to be used in place of custom playerheads for supported mobs. (if the spawn string is recognized)
     * @param addLore controls whether any lore text should be added to the head. (currently only applied to custom mobheads and player heads)
     * @return The skull itemstack desired. If the spawn string is recognized, this will be the corresponding entity's head, otherwise it will be a playerhead for the name supplied.
     */
    public static ItemStack spawnEgg(String spawnString, int quantity, boolean unused,boolean addLore){
        InternalEggType type;
        if(spawnString.isEmpty()) return null;
        else type = InternalEggType.getBySpawnName(spawnString);
        if(type==null){
            return null;
        }else{
            return MobEgg(type,quantity,unused,addLore);
        }
    }
}
