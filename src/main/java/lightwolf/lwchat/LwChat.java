package lightwolf.lwchat;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LwChat extends JavaPlugin implements Listener {
    String customFormat = "";
    List<String> worldsWithoutChat = new ArrayList();

    public void onEnable() {
        this.loadCfg();
        this.saveCfg();
    }

    private void loadCfg() {
        this.customFormat = this.getConfig().getString("custom-chat-format", "");
        this.worldsWithoutChat = this.getConfig().getStringList("worlds.no-chat");
    }

    private void saveCfg() {
        this.getConfig().set("custom-chat-format", "");
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getConfig().set("worlds.no-chat", this.worldsWithoutChat);
        this.saveConfig();
    }

    private void sendMessageInWorld(Player sender, String msg, String chatFormat) {
        if (!this.worldsWithoutChat.contains(sender.getWorld().getName()) && sender.hasPermission("lwchat.chat")) {
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();

            for (Player player : onlinePlayers) {
                if (!this.worldsWithoutChat.contains(player.getWorld().getName()) && player.hasPermission("lwchat.chat")) {
                    player.sendMessage(chatFormat.replace("%1$s", sender.getName()).replace("%2$s", msg));
                }
            }
        }
    }


    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onChat(AsyncPlayerChatEvent event) {
        this.sendMessageInWorld(event.getPlayer(), event.getMessage(), ChatColor.translateAlternateColorCodes('&', this.customFormat.isEmpty() ? event.getFormat() : this.customFormat));
        event.setCancelled(true);
    }
}
