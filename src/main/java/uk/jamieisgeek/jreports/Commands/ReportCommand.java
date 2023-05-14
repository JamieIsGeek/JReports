package uk.jamieisgeek.jreports.Commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import uk.jamieisgeek.jreports.Handlers.ConfigHandler;
import uk.jamieisgeek.jreports.Handlers.ReportHandler;
import uk.jamieisgeek.jreports.JReports;

import java.util.ArrayList;
import java.util.List;

public class ReportCommand extends Command implements TabExecutor {
    private final JReports plugin;
    private final ConfigHandler configHandler;
    private final ReportHandler reportHandler;
    public ReportCommand(String name, String permission, JReports plugin) {
        super(name, permission);
        this.plugin = plugin;
        this.configHandler = plugin.getConfigHandler();
        this.reportHandler = plugin.getReportHandler();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer player)) {
            plugin.getLogger().warning("Only players can use this command!");
            return;
        }

        if(args[0] == null || plugin.getProxy().getPlayer(args[0])  == null) {
            player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', configHandler.getMessage("invalid-user"))));
            return;
        }

        if(args[1] == null) {
            player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', configHandler.getMessage("no-reason-provided"))));
            return;
        }

        ProxiedPlayer offenderPlayer = plugin.getProxy().getPlayer(args[0]);

        reportHandler.createReport(offenderPlayer.getServer().getInfo().getName(), args[0], player.getName(), makeReason(args));
        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', configHandler.getMessage("report-sent"))));
    }

    private String makeReason(String[] args) {
        StringBuilder message = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        return message.toString();
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if(strings.length == 1) {
            List<String> onlinePlayers = new ArrayList<>();
            plugin.getProxy().getPlayers().forEach(player -> onlinePlayers.add(player.getName()));

            return onlinePlayers;
        }
        return null;
    }
}
