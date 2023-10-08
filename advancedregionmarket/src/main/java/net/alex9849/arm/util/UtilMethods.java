package net.alex9849.arm.util;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.SellType;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class UtilMethods {

    public static void deleteFilesRec(File file) {
        if(file.isDirectory()) {
            for(File child : file.listFiles()) {
                deleteFilesRec(child);
            }
        }
        file.delete();
    }

    public static Region getPlayerRegionBySellType(UUID player, AdvancedRegionMarket arm, SellType type) {
        List<Region> regionList = arm.getRegionManager().getRegionsByOwner(player);
        if (regionList.isEmpty()) return null;
        for (Region r : regionList) {
            if (type == null) return r;
            else if (r.getSellType() == type) return r;
        }
        return null;
    }

    public static Region getRegionAtPlayerPosOrPlayers(Player player, AdvancedRegionMarket arm, SellType type) {
        List<Region> regionList = arm.getRegionManager().getRegionsByLocation(player.getLocation());
        if (regionList.isEmpty()) return getPlayerRegionBySellType(player.getUniqueId(), arm, type);
        for (Region r : regionList) {
            if (type == null) return r;
            else if (r.getSellType() == type) return r;
        }
        return null;
    }

}
