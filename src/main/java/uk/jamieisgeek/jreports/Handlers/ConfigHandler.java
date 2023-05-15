package uk.jamieisgeek.jreports.Handlers;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import uk.jamieisgeek.jreports.JReports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConfigHandler {
    private final JReports plugin;
    private Configuration config;
    public ConfigHandler(JReports plugin) {
        this.plugin = plugin;

        try {
            this.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init() throws IOException {
        if (!plugin.getDataFolder().exists()) {
            plugin.getLogger().info("Created config folder: " + plugin.getDataFolder().mkdir());
        }

        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            FileOutputStream outputStream = new FileOutputStream(configFile);
            InputStream in = plugin.getResourceAsStream("config.yml");
            in.transferTo(outputStream);
        }

        if (!plugin.getDataFolder().exists()) {
            plugin.getLogger().info("Created config folder: " + plugin.getDataFolder().mkdir());
        }

        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));
    }

    public String getMessage(String path) {
        Configuration messages = config.getSection("messages");
        return messages.getString(path);
    }

    public String getAlertPermission() {
        return config.getString("alertPermission");
    }

    public boolean isDiscordEnabled() {
        return config.getBoolean("discord.sendToDiscord");
    }

    public String getDiscordWebhook() {
        return config.getString("discord.webhook");
    }

    public String getReportPermission() {
        return config.getString("reportPermission");
    }

    public String getReloadPermission() {
        return config.getString("reloadPermission");
    }
}
