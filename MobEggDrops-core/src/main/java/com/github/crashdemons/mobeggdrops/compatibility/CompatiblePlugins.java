/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.mobeggdrops.compatibility;

import org.bukkit.plugin.Plugin;

/**
 * Class providing methods and information for inter-plugin compatibility
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public final class CompatiblePlugins {

    private CompatiblePlugins() {}

    private static boolean ready = false;
    private static Plugin parentPlugin = null;

    /**
     * Initialize plugin support classes.
     * This should be done during plugin Enable or afterwards - you may need to
     * add a SoftDepend entry for the plugin to be detected in onEnable.
     *
     * @param parentPluginInstance the plugin requesting compatibility support
     */
    public static void init(Plugin parentPluginInstance) {
        CompatiblePlugins.parentPlugin = parentPluginInstance;
        ready = true;
    }


    /**
     * Checks whether the plugin compatibility classes are ready for use.
     * (whether the class init completed without exception)
     *
     * @return whether the class is ready
     */
    public static boolean isReady() {
        return ready;
    }

}
