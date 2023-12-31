package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.regions.Region;
import net.alex9849.arm.regions.SellType;
import net.alex9849.arm.util.UtilMethods;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LimityCommand extends BasicArmCommand {

    public LimityCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "limity",
                Arrays.asList("(?i)limity"),
                Arrays.asList("limity"),
                Arrays.asList(Permission.MEMBER_LIMITY));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException {
        Player player = (Player) sender;
        Region region = UtilMethods.getRegionAtPlayerPosOrPlayers(player, getPlugin(), SellType.SELL);
        if (region == null) {
            throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
        }

        if (!region.getRegion().hasMember(player.getUniqueId()) && !region.getRegion().hasOwner(player.getUniqueId()) && !player.hasPermission(Permission.ADMIN_ENTITYLIMIT_CHECK)) {
            throw new InputException(player, Messages.NOT_A_MEMBER_OR_OWNER);
        }
        List<Entity> entities = region.getFilteredInsideEntities(false, true,
                true, true, true, true, true, false,
                false, false, false);

        player.sendMessage(region.replaceVariables(Messages.ENTITYLIMIT_CHECK_HEADLINE));
        String totalstatus = region.getEntityLimitGroup().replaceVariables(Messages.ENTITYLIMIT_CHECK_PATTERN, entities, region.getExtraTotalEntitys());

        if ((region.getEntityLimitGroup().getSoftLimit(region.getExtraTotalEntitys()) < region.getEntityLimitGroup().getHardLimit()) && !region.isSubregion()) {
            totalstatus = totalstatus.replace("%entityextensioninfo%", region.getEntityLimitGroup().replaceVariables(Messages.ENTITYLIMIT_CHECK_EXTENSION_INFO, entities, region.getExtraTotalEntitys()));
        } else {
            totalstatus = totalstatus.replace("%entityextensioninfo%", "");
        }
        player.sendMessage(totalstatus);
        for (EntityLimit entityLimit : region.getEntityLimitGroup().getEntityLimits()) {
            String entitystatus = entityLimit.replaceVariables(Messages.ENTITYLIMIT_CHECK_PATTERN, entities, region.getExtraEntityAmount(entityLimit.getLimitableEntityType()));
            if ((entityLimit.getSoftLimit(region.getExtraEntityAmount(entityLimit.getLimitableEntityType())) < entityLimit.getHardLimit()) && !region.isSubregion()) {
                entitystatus = entitystatus.replace("%entityextensioninfo%", entityLimit.replaceVariables(Messages.ENTITYLIMIT_CHECK_EXTENSION_INFO, entities, region.getExtraEntityAmount(entityLimit.getLimitableEntityType())));
            } else {
                entitystatus = entitystatus.replace("%entityextensioninfo%", "");
            }
            player.sendMessage(entitystatus);
        }
        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        return new ArrayList<>();
    }
}
