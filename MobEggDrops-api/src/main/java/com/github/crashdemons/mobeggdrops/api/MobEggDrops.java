/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.mobeggdrops.api;

/**
 * PlayerHeads class for managing API provisions.
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public final class MobEggDrops {
    private MobEggDrops(){}
    private static MobEggDropsAPI apiInstance = null;

    /**
     * Gets the instance of the PlayerHeads API
     *
     * @return the PlayerHeads API instance, or null if the plugin is not
     * available or you accidentally shaded the API into your plugin.
     */
    public static MobEggDropsAPI getApiInstance() {
        return apiInstance;
    }

    /**
     * Sets the API instance internally for PlayerHeads. This method should not
     * be called outside of the plugin and using it will introduce instability.
     *
     * @param api the instance of the class implementing the API to provide to
     * plugin developers.
     * @deprecated This method is intended only for internal PlayerHeads use.
     */
    @Deprecated
    public static void setApiInstance(final MobEggDropsAPI api) {
        MobEggDrops.apiInstance = api;
    }

}
