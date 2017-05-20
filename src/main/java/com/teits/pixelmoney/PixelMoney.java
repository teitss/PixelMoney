package com.teits.pixelmoney;

import java.io.IOException;
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
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.teits.pixelmoney.exec.ReloadExecutor;
import com.teits.pixelmoney.exec.ToggleExecutor;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;


@Plugin ( name = "PixelMoney", id = "pixelmoney", version = "1.0", authors = "Teits", dependencies = {@Dependency(id = "pixelmon")})
public class PixelMoney {
	@Inject
	Logger logger;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	public Path defaultConfig;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	public Path filePath = Paths.get(defaultConfig + "/pixelmoney.conf");
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	public ConfigurationLoader<CommentedConfigurationNode> configManager = HoconConfigurationLoader.builder().setPath(filePath).build();
	
	public ConfigurationNode confignode;
	public int money;
	public boolean levelbased;
	
    public static List<UUID> toggle = new ArrayList<>();
	EconomyService economyService;
	Pixelmon Pixelmon;
	
	@Listener
	public void onServerStart(GameInitializationEvent e){
		
		
		if(Files.exists(filePath)) {
			init();
			load();
		}
		else {
			setup();
			init();
			load();
		}
		
		Pixelmon.EVENT_BUS.register(this);
		
		logger.info("---------------------------");
		logger.info("|        PixelMoney       |");
		logger.info("|Initializating the plugin|");
		logger.info("---------------------------");
		
		CommandSpec toggle = CommandSpec.builder()
				.permission("pixelmoney.toggle")
				.description(Text.of("Toggle the money log messages"))
				.executor(new ToggleExecutor())
				.build();
		CommandSpec reload = CommandSpec.builder()
				.permission("pixelmoney.reload")
				.description(Text.of("Reload PixelMoney's config"))
				.executor(new ReloadExecutor())
				.build();
		CommandSpec main = CommandSpec.builder()
				.permission("pixelmoney.reload")
				.description(Text.of("Reload PixelMoney's config"))
				.child(reload, "reload")
				.child(toggle, "togglemsg")
				.build();
		
		Sponge.getCommandManager().register(this, main, "pixelmoney", "pm");
		
		logger.info("Successfully initializated!");
	}
	
	public void init() {
		try {
			confignode = configManager.load();
		}
		catch (IOException ec){
			ec.printStackTrace();
		}
	}
	
	public void setup() {
		try {
			Files.createFile(filePath);
			confignode = configManager.createEmptyNode();
			confignode.getNode("pixelmoney", "money").setValue(10);
			confignode.getNode("pixelmoney", "levelbased").setValue(false);
			configManager.save(confignode);
		}
		catch (IOException ec){
			ec.printStackTrace();
		}
		
	}
	public void load() {
			money = confignode.getNode("pixelmoney", "money").getInt();
			levelbased = confignode.getNode("pixelmoney", "levelbased").getBoolean();
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
			    	EntityPixelmon poke = wrapper.pokemon;
			    	if (levelbased == true) {
			    		BigDecimal amount = new BigDecimal((poke.getLvl().getLevel()) * money);
			    		acc.deposit(economyService.getDefaultCurrency(), amount, Cause.source(this).build());
		    			if(toggle.contains(p.getUniqueId())) {
		    				return;
		    			}
		    			else {
		    				p.sendMessage(Text.of(TextColors.GREEN, "[PixelMoney] You've gained $" + amount.setScale(2, BigDecimal.ROUND_HALF_DOWN) + " for kill a(n) " + poke.getPokemonName() + "!"));
		    			}
			    	}
			    	else {
			    		BigDecimal amount = new BigDecimal(money);
			    		acc.deposit(economyService.getDefaultCurrency(), amount, Cause.source(this).build());
		    				if(toggle.contains(p.getUniqueId())) {
		    					return;
		    				}
		    				else {
		    					p.sendMessage(Text.of(TextColors.GREEN, "[PixelMoney] You've gained $" + amount.setScale(2, BigDecimal.ROUND_HALF_DOWN) + " for kill a(n) " + poke.getPokemonName() + "!"));
		    				}
			    	}
			    }
			}
		}
	}
}
