/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.mobeggdrops.api;

import org.bukkit.Material;

/**
 * Object representing the type of Head supported by PlayerHeads. You can obtain
 * the headtype for various things through API methods - to compare head types,
 * you can use equals()
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public interface EggType {

    // 4.10.0 API
    //------------------------------------------------------------------
    /**
     * Gets the item displayname for the associated skulltype, as defined in the
     * "lang" file.
     *
     * @return A string containing the skulltype's displayname
     */
    public String getDisplayName();


    /**
     * Get the underlying bukkit implementation details for the head on this
     * server version. Includes things like the material id for blocks, items,
     * skinnability, etc.
     *
     * @return an object providing implementation details
     */
    public Material getMaterial();

    /**
     * Determine if this headtype is the same as another.
     *
     * @param h the headtype to check
     * @return whether the types were equal
     */
    public boolean equals(Object h);
    
    // 5.0.0 API
    //------------------------------------------------------------------
    /**
     * Return the associated Enum value for the HeadType
     * @return enum value
     */
    public Enum toEnum();
    
    @Override
    public int hashCode();
}
