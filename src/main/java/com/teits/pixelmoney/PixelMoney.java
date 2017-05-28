package com.teits.pixelmoney;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.teits.pixelmoney.exec.ReloadExecutor;
import com.teits.pixelmoney.exec.ToggleExecutor;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;


@Plugin ( name = PixelMoney.plName, id = "pixelmoney", version = PixelMoney.plVer, authors = PixelMoney.plAuthor, dependencies = {@Dependency(id = "pixelmon")})
public class PixelMoney {
	
	public static final String plName = "PixelMoney";
	public static final String plVer = "1.1";
	public static final String plAuthor = "Teits";
	
	@Inject
	Logger logger;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	public Path defaultConfig;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path filePath = Paths.get(defaultConfig + "/pixelmoney.conf");
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configManager = HoconConfigurationLoader.builder().setPath(filePath).build();
    
	private CommentedConfigurationNode configNode;
	public static List<UUID> toggle = new ArrayList<>();
	EconomyService economyService;
	Pixelmon Pixelmon;
	public EntityPixelmon poke;
	public ConfigPM config = new ConfigPM();
	public PixelMoney instance;
	
	public ConfigurationLoader<CommentedConfigurationNode> getConfigManager() {
		return configManager;
	}
	public CommentedConfigurationNode getConfigNode() {
		return configNode;
	}
	
	@Listener
	public void onServerStart(GameInitializationEvent e){
		
		ReloadExecutor.set(this);
		logger.info(plName + " " + plVer + " by " + plAuthor);
		
		if(Files.exists(filePath)) {
			logger.info("Loading config file");
			config.configLoad(config.configInit(configManager, configNode));
		}
		else {
			logger.info("Creating config file");
			config.configSetup(filePath, configManager, configNode);
			logger.info("Loading config file");
			config.configLoad(config.configInit(configManager, configNode));
		}
		
		Pixelmon.EVENT_BUS.register(this);
		
		logger.info("Registering commands");
		
		CommandSpec toggle = CommandSpec.builder()
				.permission("teits.pixelmoney.toggle")
				.description(Text.of("Toggle the money log messages"))
				.executor(new ToggleExecutor())
				.build();
		CommandSpec reload = CommandSpec.builder()
				.permission("teits.pixelmoney.reload")
				.description(Text.of("Reload PixelMoney's config"))
				.executor(new ReloadExecutor())
				.build();
		CommandSpec main = CommandSpec.builder()
				.permission("teits.pixelmoney")
				.description(Text.of("PixelMoney base command"))
				.child(reload, "reload")
				.child(toggle, "togglemsg")
				.build();
		
		Sponge.getCommandManager().register(this, main, "pixelmoney", "pm");
		
		logger.info("Successfully initializated!");
		logger.info("Thank you for using " + plName);
	}
	
	@Listener
	public void onChangeServiceProvider(ChangeServiceProviderEvent event) {
	        if (event.getService().equals(EconomyService.class)) {
	                economyService = (EconomyService) event.getNewProviderRegistration().getProvider();
	        }
	}
	@SubscribeEvent
	public void onBeat(BeatWildPixelmonEvent event) { 
		Player p = (Player) event.player;
		if(p.hasPermission("teits.pixelmoney")) {
			Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(p.getUniqueId());
			if (uOpt.isPresent()) {
			    UniqueAccount acc = uOpt.get();
			    for (PixelmonWrapper wrapper : event.wpp.controlledPokemon) {
			    	poke = wrapper.pokemon;
			    	config.setAmount(poke);
			    	if(config.setAmount(poke)==null){
			    		System.out.println("You have a problem in your config file");
			    	}else{
			    		acc.deposit(economyService.getDefaultCurrency(), config.amount, Cause.source(this).build());
		    			if(toggle.contains(p.getUniqueId())) {
		    				return ;
		    			}else{
		    				p.sendMessages(TextSerializers.FORMATTING_CODE.deserialize(config.logmessage
		    						.replaceAll("%amount%", config.amount.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString())
		    						.replaceAll("%pokemon%", poke.getPokemonName())));
		    			}
			    	}
			    }
			}
		}
	}
}
