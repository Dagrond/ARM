package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SellBackCommand extends BasicArmCommand {
    private final String regex_nomoney = "(?i)usun [^;\n ]+ (?i)bezpieniedzy";

    public SellBackCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "usun",
                Arrays.asList("(?i)usun", "(?i)usun [^;\n ]+", "(?i)usun [^;\n ]+ (?i)bezpieniedzy"),
                Arrays.asList("usun [dzialka]"),
                Arrays.asList(Permission.MEMBER_SELLBACK));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        boolean noMoney = false;
        Region region = null;
        if (command.split(" ").length > 1) {
            region = getPlugin().getRegionManager()
                    .getRegionbyNameAndWorldCommands(command.split(" ")[1], player.getLocation().getWorld().getName());
        }
        if (region == null) {
            List<Region> lr = getPlugin().getRegionManager().getRegionsByLocation(player.getLocation());
            if (lr.isEmpty()) {
                player.sendMessage("§cMusisz stać na działce, którą chcesz usunąć!");
                return true;
            } else {
                region = lr.get(0);
            }
        }
        if (!region.getRegion().hasOwner(player.getUniqueId())) {
            throw new InputException(player, Messages.REGION_NOT_OWN);
        }
        if (!region.isSold()) {
            throw new InputException(player, Messages.REGION_NOT_SOLD);
        }
        String confirmQuestion = Messages.SELLBACK_WARNING;
        if(command.matches(this.regex_nomoney)) {
            confirmQuestion = confirmQuestion.replace("%paybackmoney%", Double.toString(0));
            noMoney = true;
        }
        confirmQuestion = region.replaceVariables(confirmQuestion);
        player.sendMessage(Messages.PREFIX + confirmQuestion);
        Gui.openSellWarning(player, region, noMoney, Arrays.asList(confirmQuestion.split(String.valueOf(Character.LINE_SEPARATOR))),
                Player::closeInventory);
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if(args.length == 2) {
            returnme.addAll(getPlugin().getRegionManager()
                    .completeTabRegions(player, args[1], PlayerRegionRelationship.OWNER, true, true));
        } else if(args.length == 3) {
            if("bezpieniedzy".toLowerCase().startsWith(args[2])) {
                returnme.add("bezpieniedzy");
            }
        }
        return returnme;
    }
}
