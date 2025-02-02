package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.handler.CommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HelpCommand extends BasicArmCommand {

    private final String regex_args = "(?i)pomoc [0-9]+";
    private CommandHandler cmdHandler;
    private String headline;
    private String[] betweenCmds;

    public HelpCommand(CommandHandler cmdHandler, AdvancedRegionMarket plugin, String headline, String[] betweenCmds, String permission) {
        super(true, plugin, "pomoc", Arrays.asList("(?i)pomoc [0-9]+", "(?i)pomoc"),
                Arrays.asList("pomoc", "help [strona]"), Arrays.asList(permission));
        this.cmdHandler = cmdHandler;
        this.headline = headline;
        this.betweenCmds = betweenCmds;
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        int selectedpage = 1;
        if (command.matches(this.regex_args)) {
            selectedpage = Integer.parseInt(command.split(" ")[1]);
        }

        List<BasicArmCommand> commands = this.cmdHandler.getCommands();
        List<String> usages = new ArrayList<>();

        for (BasicArmCommand armCommand : commands) {
            if(armCommand.getPermissions().stream().anyMatch(p -> sender.hasPermission(p))) {
                usages.addAll(armCommand.getUsage());
            }
        }

        Collections.sort(usages);

        final int commandsPerPage = 7;
        int pages = usages.size() / commandsPerPage;
        if ((usages.size() % commandsPerPage) != 0) {
            pages++;
        }

        if (pages < selectedpage) {
            selectedpage = pages;
        }

        int firstCommand = (selectedpage * commandsPerPage) - commandsPerPage;
        int lastCommand = selectedpage * commandsPerPage;

        if (usages.size() < lastCommand) {
            lastCommand = usages.size();
        }

        sender.sendMessage(this.headline.replace("%actualpage%", selectedpage + "").replace("%maxpage%", pages + ""));
        for (int i = firstCommand; i < lastCommand; i++) {
            String sendmessage = ChatColor.GOLD + "/" + commandLabel + " ";
            for (String bcmd : betweenCmds) {
                sendmessage = sendmessage + bcmd + " ";
            }
            sendmessage = sendmessage + usages.get(i);
            sender.sendMessage(sendmessage);
        }

        return true;
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        return new ArrayList<>();
    }


}
