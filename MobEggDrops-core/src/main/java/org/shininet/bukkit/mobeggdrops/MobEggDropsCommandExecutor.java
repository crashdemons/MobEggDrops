/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.shininet.bukkit.mobeggdrops;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

/**
 * Defines a command handler for the plugin.
 *
 * @author meiskam
 */
class MobEggDropsCommandExecutor implements CommandExecutor, TabCompleter {

    private final MobEggDrops plugin;

    public MobEggDropsCommandExecutor(MobEggDrops plugin) {
        this.plugin = plugin;
    }

    private void formatMsg(CommandSender sender, String commandscope, String message, String... replacements) {
        Formatter.formatMsg(sender, Lang.BRACKET_LEFT + commandscope + Lang.BRACKET_RIGHT + Lang.SPACE + message, replacements);
    }
    
    private String getConfigDisplay(String key){
        return key + Lang.COLON_SPACE + Config.getValueDisplayString(plugin.configFile, key);
    }

    private boolean onCommandConfigGet(CommandSender sender, Command cmd, String label, String[] args, String scope) {
        if (!sender.hasPermission("mobeggdrops.config.get")) {
            formatMsg(sender, scope, Lang.ERROR_PERMISSION);
            return true;
        }
        if (args.length == 3) {
            String key = args[2].toLowerCase();
            formatMsg(sender, scope, getConfigDisplay(key));
        } else {
            formatMsg(sender, scope, Lang.SYNTAX + Lang.COLON_SPACE + scope + Lang.SPACE + Lang.OPT_VARIABLE_REQUIRED);//Syntax: ph config get <whatever>
            formatMsg(sender, scope, Lang.CONFIG_VARIABLES + Lang.COLON_SPACE + Config.configKeysString);//Config variables: x, y, z
        }
        return true;
    }

    private boolean onCommandConfigSet(CommandSender sender, Command cmd, String label, String[] args, String scope) {
        if (!sender.hasPermission("mobeggdrops.config.set")) {
            formatMsg(sender, scope, Lang.ERROR_PERMISSION);
            return true;
        }
        if (args.length == 3) {
            String key = args[2].toLowerCase();
            plugin.configFile.set(key, null);
            plugin.saveConfig();
            formatMsg(sender, scope, getConfigDisplay(key));
            return true;
        } else if (args.length == 4) {
            String key = args[2].toLowerCase();
            String value = args[3].toLowerCase();
            
            try{
                Config.setValue(plugin.configFile, key, value);
            }catch(NumberFormatException e){
                formatMsg(sender, scope, Lang.ERROR_NUMBERCONVERT, value);
            }catch(IllegalStateException e){
                plugin.logger.warning(Formatter.format(Lang.ERROR_CONFIGTYPE, Config.configKeys.get(key).toString()));
            }
            
            plugin.saveConfig();
            formatMsg(sender, scope, getConfigDisplay(key));
            return true;
        } else {
            formatMsg(sender, scope, Lang.SYNTAX + Lang.COLON_SPACE + scope + Lang.SPACE + Lang.OPT_VARIABLE_REQUIRED + Lang.SPACE + Lang.OPT_VALUE_OPTIONAL);
            formatMsg(sender, scope, Lang.CONFIG_VARIABLES + Lang.COLON_SPACE + Config.configKeysString);
            return true;
        }
    }

    private boolean onCommandConfigReload(CommandSender sender, Command cmd, String label, String[] args, String scope) {
        if (!sender.hasPermission("mobeggdrops.config.set")) {
            formatMsg(sender, scope, Lang.ERROR_PERMISSION);
            return true;
        }
        plugin.reloadConfig();
        plugin.configFile = plugin.getConfig();
        Lang.reload();
        plugin.onConfigReloaded();
        formatMsg(sender, scope, Lang.CONFIG_RELOADED);
        return true;
    }

    private boolean onCommandConfig(CommandSender sender, Command cmd, String label, String[] args, String scope) {
        if (args.length == 1) {
            formatMsg(sender, scope, Lang.SUBCOMMANDS + Lang.COLON_SPACE
                    + Lang.CMD_GET + Lang.COMMA_SPACE
                    + Lang.CMD_SET + Lang.COMMA_SPACE
                    + Lang.CMD_RELOAD
            );
            //[ph:config] Subcommands: get, set, reload
            return true;
        }
        if (args[1].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_GET))) {
            return onCommandConfigGet(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_GET);
        } else if (args[1].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_SET))) {
            return onCommandConfigSet(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_SET);
        } else if (args[1].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_RELOAD))) {
            return onCommandConfigReload(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_RELOAD);
        } else {
            scope += Lang.COLON + Lang.CMD_UNKNOWN;
            formatMsg(sender, scope, Lang.ERROR_INVALID_SUBCOMMAND);
            return true;
        }
    }

    private boolean onCommandSpawn(CommandSender sender, Command cmd, String label, String[] args, String scope) {
        String skullOwner;
        boolean haspermission;
        Player reciever = null;
        int quantity = Config.defaultStackSize;
        boolean isConsoleSender = !(sender instanceof Player);

        boolean usevanillaskull = plugin.configFile.getBoolean("dropvanillaeggs");

        if (isConsoleSender) {
            if ((args.length != 3) && (args.length != 4)) {
                formatMsg(sender, scope, Lang.SYNTAX + Lang.COLON_SPACE + scope + Lang.SPACE + Lang.OPT_HEADNAME_REQUIRED + Lang.SPACE + Lang.OPT_RECEIVER_REQUIRED + Lang.SPACE + Lang.OPT_AMOUNT_OPTIONAL);
                return true;
            }
        } else {
            reciever = (Player) sender;
        }
        if (args.length == 1 || args.length == 2 && sender.getName().equalsIgnoreCase(args[1])) {
            skullOwner = sender.getName();
            haspermission = sender.hasPermission("mobeggdrops.spawn.own");
        } else if (args.length == 2) {
            skullOwner = args[1];
            haspermission = sender.hasPermission("mobeggdrops.spawn");
        } else if ((args.length == 3) || (args.length == 4)) {
            if (args.length == 4) {
                try {
                    quantity = Integer.parseInt(args[3]);
                } catch (NumberFormatException ignored) {
                }
            }
            reciever = plugin.getServer().getPlayer(args[2]);
            if (reciever == null) {
                formatMsg(sender, scope, Lang.ERROR_NOT_ONLINE, args[2]);
                return true;
            }
            skullOwner = args[1];
            if (reciever.equals(sender)) {
                haspermission = sender.hasPermission("mobeggdrops.spawn");
            } else {
                haspermission = sender.hasPermission("mobeggdrops.spawn.forother");
            }
        } else {
            formatMsg(sender, scope, Lang.SYNTAX + Lang.COLON_SPACE + scope + Lang.SPACE + Lang.OPT_HEADNAME_OPTIONAL + Lang.SPACE + Lang.OPT_RECEIVER_OPTIONAL + Lang.SPACE + Lang.OPT_AMOUNT_OPTIONAL);
            return true;
        }
        if (!haspermission) {
            formatMsg(sender, scope, Lang.ERROR_PERMISSION);
            return true;
        }
        if (plugin.configFile.getBoolean("fixcase")) {
            skullOwner = fixcase(skullOwner);
        }
        boolean addLore = plugin.configFile.getBoolean("addlore");
        if (InventoryManager.addEgg(reciever, skullOwner, quantity, usevanillaskull, addLore)) {
            formatMsg(sender, scope, Lang.SPAWNED_HEAD, skullOwner);
        } else {
            formatMsg(sender, scope, Lang.ERROR_INV_FULL);
        }
        return true;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("MobEggDrops")) {
            return false;
        }

        /*if(args.length==1){
            if(args[0].equalsIgnoreCase("test")){
                Player p = (Player) sender;
                sender.sendMessage("RN "+sender.hasPermission("mobeggdrops.rename"));
                sender.sendMessage("RN* "+sender.hasPermission("mobeggdrops.rename.*"));
                sender.sendMessage("RNM "+sender.hasPermission("mobeggdrops.rename.mob"));
                sender.sendMessage("RNP "+sender.hasPermission("mobeggdrops.rename.player"));
                sender.sendMessage("SP "+sender.hasPermission("mobeggdrops.spawn"));
                sender.sendMessage("SPO"+sender.hasPermission("mobeggdrops.spawn.own"));
                sender.sendMessage("SPFO"+sender.hasPermission("mobeggdrops.spawn.forothers"));
                return true;
            }
        }*/
        String scope = label;

        if (args.length == 0) {
            formatMsg(sender, scope, Lang.SUBCOMMANDS + Lang.COLON_SPACE
                    + Lang.CMD_CONFIG + Lang.COMMA_SPACE
                    + Lang.CMD_SPAWN + Lang.COMMA_SPACE
            );
            //[ph] Subcommands: config, spawn, rename
            return true;
        }
        if (args[0].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_CONFIG))) {
            return onCommandConfig(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_CONFIG);
        } else if (args[0].equalsIgnoreCase(Formatter.formatStrip(Lang.CMD_SPAWN))) {
            return onCommandSpawn(sender, cmd, label, args, scope + Lang.COLON + Lang.CMD_SPAWN);
        } else {
            scope += Lang.COLON + Lang.CMD_UNKNOWN;
            formatMsg(sender, scope, Lang.ERROR_INVALID_SUBCOMMAND);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("MobEggDrops")) {
            return null;
        }

        ArrayList<String> completions = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }

        final String cmd_config = Formatter.formatStrip(Lang.CMD_CONFIG);
        final String cmd_get = Formatter.formatStrip(Lang.CMD_GET);
        final String cmd_reload = Formatter.formatStrip(Lang.CMD_RELOAD);
        final String cmd_set = Formatter.formatStrip(Lang.CMD_SET);
        final String cmd_spawn = Formatter.formatStrip(Lang.CMD_SPAWN);

        if (args.length == 1) {
            if (cmd_config.startsWith(args[0])) {
                completions.add(cmd_config);
            }
            if (cmd_spawn.startsWith(args[0])) {
                completions.add(cmd_spawn);
            }
            return sort(completions);
        }
        if (args[0].equals(cmd_config)) {
            if (args.length == 2) {
                if (cmd_get.startsWith(args[1])) {
                    completions.add(cmd_get);
                }
                if (cmd_set.startsWith(args[1])) {
                    completions.add(cmd_set);
                }
                if (cmd_reload.startsWith(args[1])) {
                    completions.add(cmd_reload);
                }
                return sort(completions);
            }

            if (args[1].equals(cmd_get) || args[1].equals(cmd_set)) {
                if (args.length == 3) {
                    for (String keySet : Config.configKeys.keySet()) {
                        if (keySet.startsWith(args[2])) {
                            completions.add(keySet);
                        }
                    }
                    return sort(completions);
                }
            }
            return completions;
        } else if (args[0].equals(cmd_spawn)) {
            if (args.length > 3) {
                return completions;
            }
        }
        return null;
    }

    private List<String> sort(List<String> completions) {
        completions.sort(String.CASE_INSENSITIVE_ORDER);
        return completions;
    }

    /**
     * Attempts to find the correct casing for the input username by searching
     * online players, then offline players.
     *
     * @param inputName the username to fix
     * @return the same username, but with the case changed, if there was a
     * match.
     */
    private static String fixcase(String inputName) {
        String inputNameLC = inputName.toLowerCase();
        Player player = Bukkit.getServer().getPlayerExact(inputNameLC);

        if (player != null) {
            return player.getName();
        }

        for (OfflinePlayer offPlayer : Bukkit.getServer().getOfflinePlayers()) {
            if ((offPlayer.getName() != null) && (offPlayer.getName().toLowerCase().equals(inputNameLC))) {
                return offPlayer.getName();
            }
        }

        return inputName;
    }
}
