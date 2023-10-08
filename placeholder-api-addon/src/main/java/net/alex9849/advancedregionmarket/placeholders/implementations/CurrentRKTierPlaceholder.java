package net.alex9849.advancedregionmarket.placeholders.implementations;

import net.alex9849.advancedregionmarket.placeholders.AbstractOfflinePlayerPlaceholder;
import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.regions.Region;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class CurrentRKTierPlaceholder extends AbstractOfflinePlayerPlaceholder {

    public CurrentRKTierPlaceholder(AdvancedRegionMarket plugin) {
        super(plugin, "tier");
    }

    @Override
    public String getReplacement(OfflinePlayer offlinePlayer, String[] arguments) {
        if(!(offlinePlayer instanceof Player)) {
            return "";
        }
        Player player = (Player) offlinePlayer;
        List<Region> rl = plugin.getRegionManager().getRegionsByOwner(player.getUniqueId());
        if (rl.isEmpty()) return "brak";
        else return rl.get(0).getRegionKind().getName().replaceFirst("tier", "");
    }
}
