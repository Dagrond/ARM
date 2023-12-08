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

public class PrzeniesCommand extends BasicArmCommand {

    private final String regex_with_args = "(?i)przenies [^;\n ]+ [^;\n ]+";

    public PrzeniesCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "przenies",
                Arrays.asList("(?i)przenies", "(?i)przenies [^;\n ]+ [^;\n ]+"),
                Arrays.asList("przenies", "przenies [obroc] [kąt]"),
                Arrays.asList(Permission.MEMBER_MOVE));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        if (command.matches(this.regex_with_args)) {
            if (command.split(" ")[1].equalsIgnoreCase("obroc")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "daddon przenies 0 "+command.split(" ")[2]+" true "+player.getName());
            } else {
                player.sendMessage("§cNieznany argument: "+command.split(" ")[1]+"§c. Użycie: /d przenies [obroc] [kąt]");
            }
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "daddon przenies 0 0 true "+player.getName());
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("obroc");
        } else if (args.length == 3) {
            return Arrays.asList("90", "180", "270");
        } else {
            return new ArrayList<>();
        }
    }
}
