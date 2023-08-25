package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListRegionsCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)lista [^;\n ]+";

    public ListRegionsCommand(AdvancedRegionMarket plugin) {
        super(true, plugin, "lista",
                Arrays.asList("(?i)lista", "(?i)lista [^;\n ]+"),
                Arrays.asList("lista", "lista [gracz]"),
                Arrays.asList(Permission.MEMBER_LISTREGIONS, Permission.ADMIN_LISTREGIONS));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        String playerName = sender.getName();
        if (command.matches(this.regex_with_args)) {
            String[] args = command.split(" ");
            if(!sender.getName().equalsIgnoreCase(args[1]) && !sender.hasPermission(Permission.ADMIN_LISTREGIONS)) {
                throw new InputException(sender, Messages.NO_PERMISSION);
            }
            playerName = args[1];
        }

        OfflinePlayer oplayer = Bukkit.getOfflinePlayer(playerName);
        if (oplayer == null) {
            throw new InputException(sender, Messages.PLAYER_NOT_FOUND);
        }

        List<Region> regionsOwner = getPlugin()
                .getRegionManager().getRegionsByOwner(oplayer.getUniqueId());
        List<Region> regionsMember = getPlugin()
                .getRegionManager().getRegionsByMember(oplayer.getUniqueId());
        sender.sendMessage(ChatColor.GREEN + "Działki do których należysz:");
        sender.sendMessage(ChatColor.WHITE + "Jako Właściciel: " + ChatColor.GREEN + Messages
                .getStringList(regionsOwner, x -> x.getRegion().getId(), ", "));
        sender.sendMessage(ChatColor.WHITE + "Jako Członek: " + ChatColor.GREEN + Messages
                .getStringList(regionsMember, x -> x.getRegion().getId(), ", "));
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        return CommandHandler.tabCompleteOnlinePlayers(args[1]);
    }
}
