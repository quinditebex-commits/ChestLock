package com.example.chestlock;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class LockData {
    private final Map<String, String> lockedChests = new HashMap<>();
    private final Map<UUID, String> pendingSetPin = new HashMap<>();
    private final Map<UUID, String> pendingUnlock = new HashMap<>();
    public String locationKey(org.bukkit.Location loc) {
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }
    public boolean isLocked(String key) { return lockedChests.containsKey(key); }
    public String getPin(String key) { return lockedChests.get(key); }
    public void setPin(String key, String pin) { lockedChests.put(key, pin); }
    public void removePin(String key) { lockedChests.remove(key); }
    public void setPendingSet(UUID uuid, String key) { pendingSetPin.put(uuid, key); }
    public String getPendingSet(UUID uuid) { return pendingSetPin.get(uuid); }
    public void clearPendingSet(UUID uuid) { pendingSetPin.remove(uuid); }
    public boolean hasPendingSet(UUID uuid) { return pendingSetPin.containsKey(uuid); }
    public void setPendingUnlock(UUID uuid, String key) { pendingUnlock.put(uuid, key); }
    public String getPendingUnlock(UUID uuid) { return pendingUnlock.get(uuid); }
    public void clearPendingUnlock(UUID uuid) { pendingUnlock.remove(uuid); }
    public boolean hasPendingUnlock(UUID uuid) { return pendingUnlock.containsKey(uuid); }
}
