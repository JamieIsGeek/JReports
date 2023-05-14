package uk.jamieisgeek.jreports.Handlers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import uk.jamieisgeek.jreports.JReports;

import java.awt.*;
import java.io.IOException;

public class ReportHandler {
    private final ConfigHandler configHandler;
    private final JReports plugin;
    public ReportHandler(ConfigHandler configHandler, JReports plugin) {
        this.configHandler = configHandler;
        this.plugin = plugin;
    }

    public void createReport(String server, String offender, String reporter, String reason) {
        if (configHandler.isDiscordEnabled()) {
            sendToDiscord(server, offender, reporter, reason);
        }

        String reportMessage = ChatColor.GOLD + "New Player Report!\n\n" +
                ChatColor.RED + "Offender: " + ChatColor.GOLD + offender + "\n" +
                ChatColor.RED + "Reporter: " + ChatColor.GOLD + reporter + "\n" +
                ChatColor.RED + "Server: " + ChatColor.GOLD + server + "\n" +
                ChatColor.RED + "Reason: " + ChatColor.GOLD + reason;

        plugin.getProxy().getPlayers().stream().filter(player -> !player.hasPermission(configHandler.getAlertPermission())).forEach(player -> player.sendMessage(new TextComponent(reportMessage)));

        plugin.getLogger().info(reportMessage);
    }

    private void sendToDiscord(String server, String offender, String reporter, String reason) {
        HttpHandler httpHandler = new HttpHandler(configHandler.getDiscordWebhook());
        httpHandler.addEmbed(new HttpHandler.EmbedObject()
                .setTitle("New Player Report")
                .addField("Server", server, true)
                .addField("Offender", offender, true)
                .addField("Reporter", reporter, true)
                .addField("Reason", reason, true)
                .setColor(Color.RED)
                .setFooter("JReports", "")
        );

        try {
            httpHandler.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
