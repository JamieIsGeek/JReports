package uk.jamieisgeek.jreports;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import uk.jamieisgeek.jreports.Commands.ReloadCommand;
import uk.jamieisgeek.jreports.Commands.ReportCommand;
import uk.jamieisgeek.jreports.Handlers.ConfigHandler;
import uk.jamieisgeek.jreports.Handlers.ReportHandler;

public final class JReports extends Plugin {
    private ConfigHandler configHandler;
    private ReportHandler reportHandler;

    @Override
    public void onEnable() {
        this.configHandler = new ConfigHandler(this);
        this.reportHandler = new ReportHandler(configHandler, this);

        getProxy().getPluginManager().registerCommand(this, new ReportCommand("report", configHandler.getReportPermission(), this));
        getProxy().getPluginManager().registerCommand(this, new ReloadCommand("jrr", configHandler.getReloadPermission(), this));
        getLogger().info(ChatColor.GREEN + "JReports has enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.GREEN + "JReports has disabled!");
    }

    public void Reload() {
        configHandler = new ConfigHandler(this);
        reportHandler = new ReportHandler(configHandler, this);
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public ReportHandler getReportHandler() {
        return reportHandler;
    }
}
