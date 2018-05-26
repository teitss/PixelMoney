package br.github.superteits.pixelmoney;

import br.github.superteits.pixelmoney.commands.BaseCommand;
import br.github.superteits.pixelmoney.config.Config;
import br.github.superteits.pixelmoney.config.Group;
import br.github.superteits.pixelmoney.config.GroupSerializer;
import br.github.superteits.pixelmoney.listeners.BeatTrainerListener;
import br.github.superteits.pixelmoney.listeners.BeatWildPixelmonListener;
import br.github.superteits.pixelmoney.listeners.CaptureListener;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.permission.PermissionService;

import java.nio.file.Files;
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
	public static final String VERSION = "2.2.1";
	public static final String AUTHOR = "Teits";
	public static final String DESC = "Adds configurable Sponge money rewards for defeating Pokémons and NPC trainers.";

	@Inject
	private Logger logger;

	@Inject
	private PluginContainer pluginContainer;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfig;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;

	private HashSet<UUID> toggle = new HashSet<>();
	private EconomyService economyService;
	private PermissionService permissionService;
	private Config config = new Config();
	private boolean haveServerEconomyPlugin = false;
	public static PixelMoney INSTANCE;

	@Listener
	public void onServerStart(GameInitializationEvent e){
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Group.class), new GroupSerializer());
		INSTANCE = this;
		config.initConfig(configManager, defaultConfig);
		logger.info("Registering listeners...");
		registerListeners();
		logger.info("Registering commands...");
		registerCommands();
		logger.info("Successfully initializated!");
	}


	@Listener
	public void onServerStarted(GameStartedServerEvent e) {
		if(!haveServerEconomyPlugin) {
			Sponge.getEventManager().unregisterPluginListeners(this);
			for(CommandMapping commandMapping : Sponge.getCommandManager().getOwnedBy(this)) {
				Sponge.getCommandManager().removeMapping(commandMapping);
			}
			logger.error("PixelMoney didn't found a valid economy plugin. Unloading...");
		}

	}


	@Listener
	public void onChangeServiceProvider(ChangeServiceProviderEvent event) {
		if (event.getService().equals(EconomyService.class)) {
			economyService = (EconomyService) event.getNewProviderRegistration().getProvider();
			haveServerEconomyPlugin = true;
		}
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

	public Config getConfig() {
		return this.config;
	}

	public PluginContainer getPluginContainer() {
		return this.pluginContainer;
	}

	public HashSet<UUID> getToggledPlayers() {
		return this.toggle;
	}
}
