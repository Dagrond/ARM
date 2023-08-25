package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.*;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuyCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)zajmij [^;\n ]+";

    public BuyCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "zajmij",
                Arrays.asList("(?i)zajmij"),
                Arrays.asList("zajmij"),
                Arrays.asList(Permission.MEMBER_BUY));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "daddon zajmij "+player.getName());
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        return new ArrayList<>();
    }
}
