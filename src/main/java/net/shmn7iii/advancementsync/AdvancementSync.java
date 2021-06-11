package net.shmn7iii.advancementsync;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

public class AdvancementSync extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Hello!");
    }


    @Override
    public void onDisable() {
        getLogger().info("Goodbye!");
    }

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent e) {
        e.getPlayer().getWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, true);
        Player p = e.getPlayer();
        Advancement ad = e.getAdvancement();

        for(String cre_str: ad.getCriteria()){
            if(cre_str.startsWith("has_the_recipe")) return;
        }

        SyncAllAdvsFrom(p);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        for(Player p : Bukkit.getOnlinePlayers()){
            SyncAllAdvsFrom(p);
        }
    }

    public static void awardPlayer(Player p, Advancement ad){
        AdvancementProgress ap = p.getAdvancementProgress(ad);
        if(!ap.isDone()){
            for(String cre_str: ad.getCriteria()){
                ap.awardCriteria(cre_str);
            }
        }
    }


    public static void SyncAllAdvsFrom(Player from){
        Iterator<Advancement> it = Bukkit.getServer().advancementIterator();
        while (it.hasNext()) {
            Advancement ad = it.next();
            AdvancementProgress ap_from = from.getAdvancementProgress(ad);
            if(ap_from.isDone()){
                for(Player p: Bukkit.getOnlinePlayers()){
                    awardPlayer(p, ad);
                }
            }
        }
    }
}
