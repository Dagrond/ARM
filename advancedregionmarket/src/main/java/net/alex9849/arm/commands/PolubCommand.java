package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolubCommand extends BasicArmCommand {

    public PolubCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "polub",
                Arrays.asList("(?i)polub"),
                Arrays.asList("polub"),
                Arrays.asList(Permission.MEMBER_UPGRADE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "polub "+player.getName());
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        return new ArrayList<>();
    }
}
