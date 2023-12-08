package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.exceptions.NoSaveLocationException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.minifeatures.teleporter.Teleporter;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.SellType;
import net.alex9849.arm.util.UtilMethods;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomCommand extends BasicArmCommand {

    public DomCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "dom",
                Arrays.asList("(?i)dom", "(?i)dom [^;\n ]+"),
                Arrays.asList("dom [dzialka]"),
                Arrays.asList(Permission.MEMBER_TP, Permission.ADMIN_TP));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        Region region;
        String[] args = command.split(" ");
        if (args.length > 1) {
            region = getPlugin().getRegionManager()
                    .getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());

            if (region == null) {
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }

            if (!region.getRegion().hasMember(player.getUniqueId()) && !region.getRegion().hasOwner(player.getUniqueId())) {
                if (!player.hasPermission(Permission.ADMIN_TP)) {
                    throw new InputException(sender, Messages.NOT_A_MEMBER_OR_OWNER);
                }
            }
        } else {
            region = UtilMethods.getPlayerRegionBySellType(player.getUniqueId(), getPlugin(), SellType.SELL);
            if (region == null) {
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }
        }
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
            PlayerRegionRelationship playerRegionRelationship = null;
            if (player.hasPermission(Permission.ADMIN_TP)) {
                playerRegionRelationship = PlayerRegionRelationship.ALL;
            } else {
                playerRegionRelationship = PlayerRegionRelationship.MEMBER_OR_OWNER;
            }
            return getPlugin().getRegionManager()
                    .completeTabRegions(player, args[1], playerRegionRelationship, true, true);
        } else {
            return new ArrayList<>();
        }
    }
}
