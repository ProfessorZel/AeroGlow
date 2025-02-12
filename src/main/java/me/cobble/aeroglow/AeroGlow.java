package me.cobble.aeroglow;

import me.cobble.aeroglow.cmds.GlowCommand;
import me.cobble.aeroglow.cmds.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class AeroGlow extends JavaPlugin {

    private static  HashSet<UUID> glowing = new HashSet<>();

    @Deprecated
    public static boolean isGlowing(Player user) {
        if (user==null) return false;
        return glowing.contains(user.getUniqueId());
    }

    public static boolean isGlowing(UUID user) {
        if (user==null) return false;
        return glowing.contains(user);
    }

    public static boolean setGlowing(UUID user, boolean newstate) {
        if (user==null) return false;
        if (newstate)
            return glowing.add(user);
        else
            return glowing.remove(user);
    }



    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        Config.setup();
        Config.get().options().copyDefaults(true);
        Config.save();

        List<String> str =  Config.get().getStringList("glowing");
        for (String s: str) {
            try{
                glowing.add(UUID.fromString(s));
            }catch (IllegalArgumentException ignored){}
        }
        PluginCommand cmd =  this.getCommand("glow");
                if (cmd!=null) cmd.setExecutor(new GlowCommand());
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        //TODO: use collection collector to one line this.
        List<String> str = new ArrayList<>();
        for (UUID s: glowing) {
            str.add(s.toString());
        }
        Config.setup();
        Config.get().options().copyDefaults(true);
        Config.get().set("glowing", str);
        Config.save();
    }
}
