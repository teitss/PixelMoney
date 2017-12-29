package br.github.superteits.pixelmoney;

import br.github.superteits.pixelmoney.commands.ReloadCommand;
import br.github.superteits.pixelmoney.commands.ToggleCommad;
import br.github.superteits.pixelmoney.config.Config;
import br.github.superteits.pixelmoney.config.GroupSerializer;
import br.github.superteits.pixelmoney.listeners.BeatTrainerListener;
import br.github.superteits.pixelmoney.listeners.BeatWildPixelmonListener;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.Text;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.UUID;


@Plugin ( name = PixelMoney.NAME, id = "pixelmoney", version = PixelMoney.VERSION, authors = PixelMoney.AUTHOR, description = PixelMoney.DESC, dependencies = {@Dependency(id = "pixelmon")})
public class PixelMoney {

	public static final String NAME = "PixelMoney";
	public static final String VERSION = "2.1.0";
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
	public static PixelMoney INSTANCE;

	@Listener
	public void onServerStart(GameInitializationEvent e){
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Group.class), new GroupSerializer());
		INSTANCE = this;
		if(Files.exists(defaultConfig)) {
			config.loadConfig(configManager);
		}
		else {
			config.setupConfig(configManager);
			config.loadConfig(configManager);
		}
		logger.info("Registering listeners...");
		Pixelmon.EVENT_BUS.register(new BeatTrainerListener());
		Pixelmon.EVENT_BUS.register(new BeatWildPixelmonListener());

		logger.info("Registering commands...");

		CommandSpec toggleSpec = CommandSpec.builder()
				.permission("pixelmoney.command.toggle")
				.description(Text.of("Toggle the money log messages"))
				.executor(new ToggleCommad())
				.build();
		CommandSpec reloadSpec = CommandSpec.builder()
				.permission("pixelmoney.command.reload")
				.description(Text.of("Reload PixelMoney's config"))
				.executor(new ReloadCommand())
				.build();
		CommandSpec baseSpec = CommandSpec.builder()
				.description(Text.of("PixelMoney 2.1.0"))
				.child(reloadSpec, "reload")
				.child(toggleSpec, "togglemsg")
				.build();

		Sponge.getCommandManager().register(this, baseSpec, "pixelmoney", "pm", "pmoney", "pixelm");

		logger.info("Successfully initializated!");
	}

	@Listener
	public void onChangeServiceProvider(ChangeServiceProviderEvent event) {
		if (event.getService().equals(EconomyService.class)) {
			economyService = (EconomyService) event.getNewProviderRegistration().getProvider();
		}
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
