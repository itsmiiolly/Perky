package com.me.itsmiiolly.perky;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;
import com.me.itsmiiolly.perky.modules.Hug;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;

public class Core extends JavaPlugin {
    private CommandsManager<CommandSender> commands;
    public CommandsManagerRegistration cmdRegister;
    
    private HashMap<String, PerkyModule> modules = Maps.newHashMap();

    @Override
    public void onEnable() {
        setupCommands();
        
        Config.load(this);
        
        registerModule("hug", new Hug());
        
        for (PerkyModule m : modules.values())
            m.load(this);
    }

    @Override
    public void onDisable() {
        for (PerkyModule m : modules.values())
            m.unload(this);
    }
    
    private void registerModule(String name, PerkyModule inst) {
        if (!Config.Modules.getEnabledModules().contains(name)) return; //Do not register disabled modules
        
        Bukkit.getPluginManager().registerEvents(inst, this);
        cmdRegister.register(inst.getClass());
        modules.put(name, inst);
    }

    private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };
        cmdRegister = new CommandsManagerRegistration(this, this.commands);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd,
            String commandLabel, String[] args) {
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else if (e.getCause() instanceof IllegalArgumentException) {
                sender.sendMessage(ChatColor.RED
                        + e.getMessage().replace("java.lang.IllegalArgumentException: ", ""));
            } else {
                sender.sendMessage(ChatColor.RED + "An error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }
}
