package uk.jamieisgeek.jreports.Commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import uk.jamieisgeek.jreports.JReports;

public class ReloadCommand extends Command {
    private final JReports plugin;
    public ReloadCommand(String name, String permission, JReports plugin) {
        super(name, permission);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        plugin.Reload();
        commandSender.sendMessage(new TextComponent(ChatColor.GREEN + "Plugin reloaded!"));
    }
}
