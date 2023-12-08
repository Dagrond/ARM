package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.NoSaveLocationException;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TPCommand extends BasicArmCommand {

    public TPCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "tp",
                Arrays.asList("(?i)tp [^;\n ]+"),
                Arrays.asList("tp [gracz]"),
                Arrays.asList(Permission.ADMIN_TP));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        Region region;
        String[] args = command.split(" ");
        OfflinePlayer owner = UtilMethods.getOfflinePlayer(args[1]);
        if (owner != null) {
            List<Region> ownerRegions = getPlugin().getRegionManager().getRegionsByOwner(owner.getUniqueId());
            if (ownerRegions.isEmpty()) {
                player.sendMessage("§cTen gracz nie posiada żadnych działek! Zobacz działki, do których należy jako członek wpisując §4/d lista "+args[1]);
                return true;
            } else if (ownerRegions.size() > 1) {
                player.sendMessage("§cTen gracz posiada więcej niż jedną działkę! Wpisz §4/d lista "+args[1]+"§c aby wyświetlić jego działki a następnie §4/d dom (id_działki)");
                return true;
            } else {
                region = ownerRegions.get(0);
            }
        } else throw new InputException(player, Messages.PLAYER_NOT_FOUND);
        try {
            Teleporter.teleport(player, region);
        } catch (NoSaveLocationException e) {
            throw new InputException(player, region.replaceVariables(Messages.TELEPORTER_NO_SAVE_LOCATION_FOUND));
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if (args.length == 2) {
            return CommandHandler.tabCompleteOnlinePlayers(args[1]);
        } else {
            return new ArrayList<>();
        }
    }
}
