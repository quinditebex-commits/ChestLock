package com.example.chestlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
public class ChestListener implements Listener {
    private final ChestLockPlugin plugin;
    private final LockData data;
    private static final Material LOCK_ITEM = Material.TRIPWIRE_HOOK;
    public ChestListener(ChestLockPlugin plugin, LockData data) { this.plugin = plugin; this.data = data; }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (block.getType() != Material.CHEST && block.getType() != Material.TRAPPED_CHEST) return;
        Player player = event.getPlayer();
        String locKey = data.locationKey(block.getLocation());
        if (player.getInventory().getItemInMainHand().getType() == LOCK_ITEM) {
            event.setCancelled(true);
            if (data.isLocked(locKey)) { player.sendMessage(ChatColor.RED + "Ta skrzynia jest juz zamknieta!"); return; }
            data.setPendingSet(player.getUniqueId(), locKey);
            player.sendMessage(ChatColor.YELLOW + "Wpisz PIN na czacie (min. 3 cyfry):");
            return;
        }
        if (data.isLocked(locKey)) {
            event.setCancelled(true);
            data.setPendingUnlock(player.getUniqueId(), locKey);
            player.sendMessage(ChatColor.GOLD + "Skrzynia zamknieta! Wpisz PIN:");
        }
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage().trim();
        if (data.hasPendingSet(player.getUniqueId())) {
            event.setCancelled(true);
            if (!msg.matches("\\d{3,}")) { player.sendMessage(ChatColor.RED + "PIN musi byc cyframi (min. 3)!"); return; }
            String locKey = data.getPendingSet(player.getUniqueId());
            data.setPin(locKey, msg);
            data.clearPendingSet(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Skrzynia zamknieta kodem: " + msg);
            return;
        }
        if (data.hasPendingUnlock(player.getUniqueId())) {
            event.setCancelled(true);
            String locKey = data.getPendingUnlock(player.getUniqueId());
            if (data.getPin(locKey).equals(msg)) {
                data.clearPendingUnlock(player.getUniqueId());
                data.removePin(locKey);
                player.sendMessage(ChatColor.GREEN + "Poprawny PIN! Mozesz otworzyc skrzynie.");
            } else {
                data.clearPendingUnlock(player.getUniqueId());
                player.sendMessage(ChatColor.RED + "Bledny PIN!");
            }
        }
    }
}
