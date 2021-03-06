package github.scarsz.discordsrv.commands;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.GamePermissionUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Made by Scarsz
 *
 * @in /dev/hell
 * @on 3/12/2017
 * @at 1:36 PM
 */
public class CommandHelp {

    private static List<ChatColor> disallowedChatColorCharacters = new ArrayList<ChatColor>() {{
        add(ChatColor.BLACK);
        add(ChatColor.DARK_BLUE);
        add(ChatColor.GRAY);
        add(ChatColor.DARK_GRAY);
        add(ChatColor.WHITE);
        add(ChatColor.MAGIC);
        add(ChatColor.BOLD);
        add(ChatColor.STRIKETHROUGH);
        add(ChatColor.UNDERLINE);
        add(ChatColor.ITALIC);
        add(ChatColor.RESET);
    }};

    @Command(commandNames = { "?", "help" },
            helpMessage = "Shows command help for DiscordSRV's commands",
            permission = "discordsrv.help",
            usageExample = "help [command]"
    )
    public static void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            help(sender);
        } else {
            help(sender, Arrays.asList(args));
        }
    }

    private static void help(CommandSender sender) {
        ChatColor titleColor = ChatColor.RESET, commandColor = ChatColor.RESET;
        while (disallowedChatColorCharacters.contains(titleColor))
            titleColor = ChatColor.values()[DiscordSRV.getPlugin().getRandom().nextInt(ChatColor.values().length)];
        while (disallowedChatColorCharacters.contains(commandColor) || commandColor == titleColor)
            commandColor = ChatColor.values()[DiscordSRV.getPlugin().getRandom().nextInt(ChatColor.values().length)];

        List<Method> commandMethods = new ArrayList<>();
        for (Method method : DiscordSRV.getPlugin().getCommandManager().getCommands().values())
            if (!commandMethods.contains(method)) commandMethods.add(method);

        sender.sendMessage(ChatColor.DARK_GRAY + "================[ " + titleColor + "DiscordSRV" + ChatColor.DARK_GRAY + " ]================");
        for (Method commandMethod : commandMethods) {
            Command commandAnnotation = commandMethod.getAnnotation(Command.class);

            // make sure sender has permission to run the commands before showing them permissions for it
            if (!GamePermissionUtil.hasPermission(sender, commandAnnotation.permission())) continue;

            sender.sendMessage(ChatColor.GRAY + "- " + commandColor + "/discord " + String.join("/", commandAnnotation.commandNames()));
            sender.sendMessage("    " + ChatColor.ITALIC + commandAnnotation.helpMessage());
            if (!commandAnnotation.usageExample().equals("")) sender.sendMessage("    " + ChatColor.GRAY + ChatColor.ITALIC + "ex. /discord " + commandAnnotation.usageExample());
        }
    }

    /**
     * Send help specific for the given commands
     * @param sender
     * @param commands
     */
    private static void help(CommandSender sender, List<String> commands) {
        ChatColor titleColor = ChatColor.RESET, commandColor = ChatColor.RESET;
        while (disallowedChatColorCharacters.contains(titleColor))
            titleColor = ChatColor.values()[DiscordSRV.getPlugin().getRandom().nextInt(ChatColor.values().length - 1)];
        while (disallowedChatColorCharacters.contains(commandColor) || commandColor == titleColor)
            commandColor = ChatColor.values()[DiscordSRV.getPlugin().getRandom().nextInt(ChatColor.values().length - 1)];

        List<Method> commandMethods = new LinkedList<>();
        for (String commandName : commands) commandMethods.add(DiscordSRV.getPlugin().getCommandManager().getCommands().get(commandName));

        sender.sendMessage(ChatColor.DARK_GRAY + "===================[ " + titleColor + "DiscordSRV" + ChatColor.DARK_GRAY + " ]===================");
        for (Method commandMethod : commandMethods) {
            Command commandAnnotation = commandMethod.getAnnotation(Command.class);

            // make sure sender has permission to run the commands before showing them permissions for it
            if (!GamePermissionUtil.hasPermission(sender, commandAnnotation.permission())) continue;

            sender.sendMessage(ChatColor.GRAY + "- " + commandColor + "/discord " + String.join("/", commandAnnotation.commandNames()));
            sender.sendMessage("   " + ChatColor.ITALIC + commandAnnotation.helpMessage());
            if (!commandAnnotation.usageExample().equals("")) sender.sendMessage("   " + ChatColor.GRAY + ChatColor.ITALIC + "ex. /discord " + commandAnnotation.usageExample());
        }
    }

}
