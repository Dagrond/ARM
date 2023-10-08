package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.flaggroups.FlagGroup;
import net.alex9849.arm.gui.Gui;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlageditorCommand extends BasicArmCommand {
    private final String regex_with_args = "(?i)ustawienia [^;\n ]+";

    public FlageditorCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "ustawienia",
                Arrays.asList("(?i)ustawienia [^;\n ]+", "(?i)ustawienia"),
                Arrays.asList("ustawienia [dzialka]", "ustawienia"),
                Arrays.asList(Permission.MEMBER_FLAGEDITOR, Permission.ADMIN_FLAGEDITOR));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        if(!FlagGroup.isFeatureEnabled()) {
            throw new InputException(sender, Messages.FLAGGROUP_FEATURE_DISABLED);
        }

        Player player = (Player) sender;
        Region selRegion;
        if (command.matches(this.regex_with_args)) {
            selRegion = getPlugin().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, command.split(" ")[1]);
        } else {
            selRegion = getPlugin().getRegionManager()
                    .getRegionAtPositionOrNameCommand(player, "");
        }

        if (selRegion == null) {
            throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
        }
        if (!(selRegion.getRegion().hasOwner(player.getUniqueId()) || player.hasPermission(Permission.ADMIN_FLAGEDITOR))) {
            throw new InputException(player, Messages.REGION_NOT_OWN);
        }

        Gui.openFlagEditor(player, selRegion, 0, p -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dm open dzialka "+p.getName()));
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        if(args.length != 2) {
            return new ArrayList<>();
        }
        PlayerRegionRelationship playerRegionRelationship = null;
        if (player.hasPermission(Permission.ADMIN_FLAGEDITOR)) {
            playerRegionRelationship = PlayerRegionRelationship.ALL;
        } else {
            playerRegionRelationship = PlayerRegionRelationship.MEMBER_OR_OWNER;
        }
        return getPlugin().getRegionManager()
                .completeTabRegions(player, args[1], playerRegionRelationship, true, true);
    }
}
