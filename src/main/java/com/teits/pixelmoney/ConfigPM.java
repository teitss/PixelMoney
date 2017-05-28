package com.teits.pixelmoney;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigPM {
	
	public double money;
	public boolean levelbased;
	public String operationType;
	public BigDecimal amount;
	public String logmessage;
	public String reloadmessage;
	public String turnonlogmessage;
	public String turnofflogmessage;
	public String tagcolor;
	public String tagtext;
	
	public CommentedConfigurationNode configInit(ConfigurationLoader<CommentedConfigurationNode> cm, CommentedConfigurationNode cn) {
		try {
			cn = cm.load();
		}
		catch (IOException ec){
			ec.printStackTrace();
		}
		return cn;
	}
	
	public void configSetup(Path filePath, ConfigurationLoader<CommentedConfigurationNode> configManager, CommentedConfigurationNode confignode) {
		try {
			Files.createFile(filePath);
			confignode = configManager.createEmptyNode();
			confignode.getNode("pixelmoney", "money").setValue(10)
				.setComment("Amount of money to be rewarded/Number to be used in levelbased's mathematical operations");
			confignode.getNode("pixelmoney", "levelbased", "enabled").setValue(false)
				.setComment("Enable/Disable per pokemon's level reward");
			confignode.getNode("pixelmoney", "levelbased", "operation-type").setValue("MULTIPLICATION")
				.setComment("You can choose 'MULTIPLICATION', 'DIVISION', 'ADDITION' or 'SUBTRACTION'");
			confignode.getNode("pixelmoney", "messages", "log-message")
				.setValue("You've gained $%amount%  for killing a(n) %pokemon%!")
				.setComment("You can use Ampersanding Formatting(&1&n) and the placeholders %amount%, %pokemon%");
			confignode.getNode("pixelmoney", "messages", "turnon-message")
				.setValue("You've turned on notifications")
				.setComment("You can use Ampersanding Formatting(&1&2)");
			confignode.getNode("pixelmoney", "messages", "tag-color")
			.setValue("WHITE")
			.setComment("You can use AQUA, BLACK, BLUE, GOLD, GRAY, GREEN, RED, WHITE, YELLOW, LIGHT_PURPLE, DARK_AQUA, DARK_BLUE, DARK_GRAY, DARK_PURPLE, DARK_GREEN, DARK_RED");
			confignode.getNode("pixelmoney", "messages", "turnoff-message")
				.setValue("You've turned off notifications")
				.setComment("You can use Ampersanding Formatting(&1&2)");
			confignode.getNode("pixelmoney", "messages", "reload-message")
				.setValue("Config reloaded!")
				.setComment("You can use Ampersanding Formatting(&1&2)");
			configManager.save(confignode);
		}
		catch (IOException ec){
			ec.printStackTrace();
		}
		
	}
	public void configLoad(CommentedConfigurationNode cn) {
		tagcolor = cn.getNode("pixelmoney", "messages", "tag-color").getString();
		tagtext = getTagColor(tagcolor) +  "[PixelMoney] ";
		levelbased = cn.getNode("pixelmoney", "levelbased", "enabled").getBoolean();
		operationType = cn.getNode("pixelmoney", "levelbased", "operation-type").getString();
		money = cn.getNode("pixelmoney", "money").getInt();
		logmessage = tagtext + cn.getNode("pixelmoney", "messages", "log-message").getString();
		reloadmessage = tagtext + cn.getNode("pixelmoney", "messages", "reload-message").getString();
		turnonlogmessage = tagtext + cn.getNode("pixelmoney", "messages", "turnon-message").getString();
		turnofflogmessage = tagtext + cn.getNode("pixelmoney", "messages", "turnoff-message").getString();
	}
	public BigDecimal setAmount(EntityPixelmon poke) {
		if(levelbased==true) {
			if(operationType.equals("MULTIPLICATION"))
				return amount = new BigDecimal(poke.getLvl().getLevel() * money);
			else if(operationType.equals("DIVISION"))
				return amount = new BigDecimal(poke.getLvl().getLevel() / money);
			else if(operationType.equals("ADDITION"))
				return amount = new BigDecimal(poke.getLvl().getLevel() + money);
			else if(operationType.equals("SUBTRACTION"))
				return amount = new BigDecimal(poke.getLvl().getLevel() - money);
		}	
		else {
			return amount = new BigDecimal(money);
		}
		return null;
	}
	public String getTagColor(String color) {
		if(color.equals("AQUA"))
			return "&b";
		else if(color.equals("BLACK"))
			return "&0";
		else if(color.equals("BLUE"))
			return "&9";
		else if(color.equals("GOLD"))
			return "&6";
		else if(color.equals("GRAY"))
			return "&7";
		else if(color.equals("GREEN"))
			return "&a";
		else if(color.equals("RED"))
			return "&c";
		else if(color.equals("WHITE"))
			return "&f";
		else if(color.equals("YELLOW"))
			return "&e";
		else if(color.equals("LIGHT_PURPLE"))
			return "&d";
		else if(color.equals("DARK_AQUA"))
			return "&3";
		else if(color.equals("DARK_BLUE"))
			return "&1";
		else if(color.equals("DARK_GRAY"))
			return "&8";
		else if(color.equals("DARK_PURPLE"))
			return "&5";
		else if(color.equals("DARK_GREEN"))
			return "&2";
		else if(color.equals("DARK_RED"))
			return "&4";
		else
			return null;
	}
}
