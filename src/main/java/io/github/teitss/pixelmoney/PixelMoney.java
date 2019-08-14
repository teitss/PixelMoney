package io.github.teitss.pixelmoney;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import io.github.teitss.pixelmoney.commands.BaseCommand;
import io.github.teitss.pixelmoney.config.Config;
import io.github.teitss.pixelmoney.listeners.BeatTrainerListener;
import io.github.teitss.pixelmoney.listeners.BeatWildPixelmonListener;
import io.github.teitss.pixelmoney.listeners.CaptureListener;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.UUID;


@Plugin (name = PixelMoney.NAME,
		id = "pixelmoney",
		version = PixelMoney.VERSION,
		authors = PixelMoney.AUTHOR,
		description = PixelMoney.DESC,
		dependencies = {@Dependency(id = "pixelmon")})
public class PixelMoney {

	public static final String NAME = "PixelMoney";
    public static final String VERSION = "3.0.3";
	public static final String AUTHOR = "Teits";
	public static final String DESC = "Adds configurable Sponge money rewards for defeating Pokémons and NPC trainers.";

	@Inject
	private Logger logger;

	@Inject
	private PluginContainer pluginContainer;
    private static PixelMoney instance;

    private ConfigurationLoader<CommentedConfigurationNode> configManager;
	@Inject
    @ConfigDir(sharedRoot = true)
    private Path configDir;
	private EconomyService economyService;
    private HashSet<UUID> toggle;
    private boolean foundEconomyPlugin = false;
    private ScriptEngine scriptEngine;

    public static PixelMoney getInstance() {
        return instance;
    }

	@Listener
    public void onServerStart(GameInitializationEvent e){
        instance = this;
        configManager = HoconConfigurationLoader.builder().setPath(configDir.resolve("pixelmoney.conf")).build();
        Config.setup(configDir, configManager);
        logger.info("Registering listeners...");
        registerListeners();
        logger.info("Registering commands...");
        registerCommands();
        logger.info("Successfully initializated!");
        toggle = new HashSet<>();
        scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

    }

	@Listener
	public void onServerStarted(GameStartedServerEvent e) {
        if (!foundEconomyPlugin) {
			Sponge.getEventManager().unregisterPluginListeners(this);
			for(CommandMapping commandMapping : Sponge.getCommandManager().getOwnedBy(this)) {
				Sponge.getCommandManager().removeMapping(commandMapping);
			}
			logger.error("PixelMoney didn't found a valid economy plugin. Unloading...");
		}

	}

	@Listener
    public void onGameReload(GameReloadEvent e) {
        Config.setup(configDir, configManager);
	}

	private void registerCommands() {
		Sponge.getCommandManager().register(this, new BaseCommand().getCommandSpec(), "pixelmoney", "pm", "pmoney", "pixelm");
	}
	private void registerListeners() {
		Pixelmon.EVENT_BUS.register(new BeatTrainerListener());
		Pixelmon.EVENT_BUS.register(new BeatWildPixelmonListener());
		Pixelmon.EVENT_BUS.register(new CaptureListener());
	}

	public ConfigurationLoader<CommentedConfigurationNode> getConfigManager() {
		return this.configManager;
	}

	public EconomyService getEconomyService() {
		return this.economyService;
	}

	public PluginContainer getPluginContainer() {
		return this.pluginContainer;
	}

	public HashSet<UUID> getToggledPlayers() {
		return this.toggle;
	}

    @Listener
    public void onChangeServiceProvider(ChangeServiceProviderEvent event) {
        if (event.getService().equals(EconomyService.class)) {
            economyService = (EconomyService) event.getNewProviderRegistration().getProvider();
            foundEconomyPlugin = true;
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }
}
