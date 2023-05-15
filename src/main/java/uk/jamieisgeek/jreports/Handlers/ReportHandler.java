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

    public void createReport(String server, String offender, String reporter, String reason, String uuid) {
        if (configHandler.isDiscordEnabled()) {
            sendToDiscord(server, offender, reporter, reason, uuid);
        }

        String reportMessage = ChatColor.WHITE + "[" + ChatColor.BLUE + "JReports" + ChatColor.WHITE + "] " + ChatColor.GOLD + "New Player Report!\n\n" +
                ChatColor.RED + ChatColor.BOLD + "Offender: " + ChatColor.YELLOW + offender + "\n" +
                ChatColor.RED + ChatColor.BOLD + "Reporter: " + ChatColor.YELLOW + reporter + "\n" +
                ChatColor.RED + ChatColor.BOLD + "Server: " + ChatColor.YELLOW + server + "\n" +
                ChatColor.RED + ChatColor.BOLD + "Reason: " + ChatColor.YELLOW + reason + "\n";

        plugin.getProxy().getPlayers().stream().filter(player -> player.hasPermission(configHandler.getAlertPermission())).forEach(player -> player.sendMessage(new TextComponent(reportMessage)));

        plugin.getLogger().info(reportMessage);
    }

    private void sendToDiscord(String server, String offender, String reporter, String reason, String uuid) {
        HttpHandler httpHandler = new HttpHandler(configHandler.getDiscordWebhook());
        httpHandler.addEmbed(new HttpHandler.EmbedObject()
                .setTitle("Player Report")
                .addField("Server","`" + server + "`", false)
                .addField("Offender","`" + offender + "`", false)
                .addField("Reporter","`" + reporter + "`", false)
                .addField("Reason","`" + reason + "`", false)
                .setColor(Color.RED)
                .setFooter("JReports", "")
                .setThumbnail("https://api.tydiumcraft.net/skin?uuid=" + uuid + "&type=body&direction=right")
        );

        try {
            httpHandler.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
