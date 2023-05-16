package uk.jamieisgeek.jreports.Handlers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
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

        String reportMessage = formatReportMessage(configHandler.getMessage("reportAlert"), offender, reporter, server, reason);

        TextComponent connectMessage = new TextComponent(ChatColor.GREEN + "[Connect to Server]\n");
        connectMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + server));
        connectMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "Connect to offender's server").create()));
        plugin.getProxy().getPlayers().stream().filter(player -> player.hasPermission(configHandler.getAlertPermission())).forEach(player -> {
            player.sendMessage(new TextComponent(reportMessage));
            player.sendMessage(connectMessage);
        });

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

    private String formatReportMessage(String reportMessage, String offender, String reporter, String server, String reason) {
        String newMessage = ChatColor.translateAlternateColorCodes('&', reportMessage);
        newMessage = newMessage.replaceAll("%offender%", offender);
        newMessage = newMessage.replaceAll("%reporter%", reporter);
        newMessage = newMessage.replaceAll("%server%", server);
        newMessage = newMessage.replaceAll("%reason%", reason);

        return newMessage;
    }
}
