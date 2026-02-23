package com.example.chestlock;
import org.bukkit.plugin.java.JavaPlugin;
public class ChestLockPlugin extends JavaPlugin {
    private LockData lockData;
    @Override
    public void onEnable() {
        lockData = new LockData();
        getServer().getPluginManager().registerEvents(new ChestListener(this, lockData), this);
        getLogger().info("ChestLock wlaczony!");
    }
    @Override
    public void onDisable() { getLogger().info("ChestLock wylaczony."); }
    public LockData getLockData() { return lockData; }
}
