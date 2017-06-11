package com.teits.pixelmoney;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.spongepowered.api.entity.living.player.Player;

import com.google.common.base.CharMatcher;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigPM {
	
	public HashMap<Integer, Double> money = new HashMap<>();
	public HashMap<Integer, Boolean> levelbased = new HashMap<>();
	public HashMap<Integer, String> operationType = new HashMap<>();
	public ArrayList<String> perms = new ArrayList<>();
	public ArrayList<Integer> pw = new ArrayList<>();
	public BigDecimal amount;
	public String logmessage;
	public String reloadmessage;
	public String turnonlogmessage;
	public String turnofflogmessage;
	public String tagcolor;
	public String tagtext;
	public ArrayList<Integer> d = new ArrayList<>();
	public int configver;
	public int numberofgroups;
	public ConfigurationNode dconf;
	
	public CommentedConfigurationNode configInit(ConfigurationLoader<CommentedConfigurationNode> cm, CommentedConfigurationNode cn) {
		try {
			cn = cm.load();
		}
		catch (IOException ec){
			ec.printStackTrace();
		}
		return cn;
	}
	public void checkVer(CommentedConfigurationNode cn) {
		configver = cn.getNode("configVer").getInt();
	}
	public void configSetup(Path filePath, ConfigurationLoader<CommentedConfigurationNode> configManager, CommentedConfigurationNode confignode) {
		try {
			Files.createFile(filePath);
			confignode = configManager.createEmptyNode();
			confignode.getNode("configVer").setValue(1).setComment("Don't change this!");
			confignode.getNode("number-of-groups").setValue(1).setComment("If you want to add more groups, change this number");
			confignode.getNode("group1", "money").setValue(10)
				.setComment("Amount of money to be rewarded/Number to be used in levelbased's mathematical operations");
			confignode.getNode("group1", "weight").setValue(1).setComment("Weight is the main identifier of a group, set different weights for different groups");
			confignode.getNode("group1", "levelbased", "enabled").setValue(false)
				.setComment("Enable/Disable per pokemon's level reward");
			confignode.getNode("group1", "levelbased", "operation-type").setValue("MULTIPLICATION")
				.setComment("You can choose 'MULTIPLICATION', 'DIVISION', 'ADDITION' or 'SUBTRACTION'");
			confignode.getNode("messages", "log-message")
				.setValue("You've gained $%amount%  for killing a(n) %pokemon%!")
				.setComment("You can use Ampersanding Formatting(&1&n) and the placeholders %amount%, %pokemon%");
			confignode.getNode("messages", "turnon-message")
				.setValue("You've turned on notifications")
				.setComment("You can use Ampersanding Formatting(&1&2)");
			confignode.getNode("messages", "tag-color")
				.setValue("WHITE")
				.setComment("You can use AQUA, BLACK, BLUE, GOLD, GRAY, GREEN, RED, WHITE, YELLOW, LIGHT_PURPLE, DARK_AQUA, DARK_BLUE, DARK_GRAY, DARK_PURPLE, DARK_GREEN, DARK_RED");
			confignode.getNode("messages", "turnoff-message")
				.setValue("You've turned off notifications")
				.setComment("You can use Ampersanding Formatting(&1&2)");
			confignode.getNode("messages", "reload-message")
				.setValue("Config reloaded!")
				.setComment("You can use Ampersanding Formatting(&1&2)");
			confignode.getNode("dimensions")
			.setValue(new ArrayList<Integer>())
			.setComment("Dimensions where plugin will work [0, -1, 47]");
			configManager.save(confignode);
		}
		catch (IOException ec){
			ec.printStackTrace();
		}
		
	}
	public void configLoad(CommentedConfigurationNode cn) {
		numberofgroups = cn.getNode("number-of-groups").getInt();
		tagcolor = cn.getNode("messages", "tag-color").getString();
		tagtext = getTagColor(tagcolor) +  "[PixelMoney] ";
		logmessage = tagtext +cn.getNode("messages", "log-message").getString();
		reloadmessage = tagtext + cn.getNode("messages", "reload-message").getString();
		turnonlogmessage = tagtext + cn.getNode("messages", "turnon-message").getString();
		turnofflogmessage = tagtext + cn.getNode("messages", "turnoff-message").getString();
		if(d.isEmpty() == false) {
			d.clear();
			for(ConfigurationNode dim : cn.getNode("dimensions").getChildrenList()) {
				int dime = dim.getInt();
				d.add(dime);
			}
		}
		else {
			for(ConfigurationNode dim : cn.getNode("dimensions").getChildrenList()) {
				int dime = dim.getInt();
				d.add(dime);
			}
		}
		if(numberofgroups != 0) {
			for(int i = 1; i <= numberofgroups; i++) {
				levelbased.put(Integer.valueOf(cn.getNode("group" + i, "weight").getInt()), Boolean.valueOf(cn.getNode("group" + i, "levelbased", "enabled").getBoolean()));
				operationType.put(Integer.valueOf(cn.getNode("group" + i, "weight").getInt()), cn.getNode("group" + i, "levelbased", "operation-type").getString());
				money.put(Integer.valueOf(cn.getNode("group" + i, "weight").getInt()), Double.valueOf(cn.getNode("group" + i, "money").getDouble()));
				perms.add("teits.pixelmoney.weight." + cn.getNode("group" + i, "weight").getInt());
			}
		}
	}
	public BigDecimal setAmount(EntityPixelmon poke, Player p) {
		if(levelbased.get(getWeight(p)).booleanValue() == true) {
			if(operationType.get(getWeight(p)).equals("MULTIPLICATION"))
				return amount = new BigDecimal(poke.getLvl().getLevel() * money.get(getWeight(p)).doubleValue());
			else if(operationType.get(getWeight(p)).equals("DIVISION"))
				return amount = new BigDecimal(poke.getLvl().getLevel() / money.get(getWeight(p)).doubleValue());
			else if(operationType.get(getWeight(p)).equals("ADDITION"))
				return amount = new BigDecimal(poke.getLvl().getLevel() + money.get(getWeight(p)).doubleValue());
			else if(operationType.get(getWeight(p)).equals("SUBTRACTION"))
				return amount = new BigDecimal(poke.getLvl().getLevel() - money.get(getWeight(p)).doubleValue());
		}	
		else {
			return amount = new BigDecimal(money.get(getWeight(p)).doubleValue());
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
	public Integer getWeight(Player p) {
		if(p.hasPermission("teits.pixelmoney.enable")) {
			pw.clear();
			for(int i = 0; i < perms.size(); i++) {
				if(p.hasPermission(perms.get(i))) {
					Integer w = Integer.valueOf(CharMatcher.DIGIT.retainFrom(perms.get(i)));
					pw.add(w);
				}	
			}
		return Collections.max(pw);
		}
			/*for(String perm : perms) {
				if(p.hasPermission(perm)) {
					int w = Integer.valueOf(CharMatcher.DIGIT.retainFrom(perm));
					return Math.max(0, w);
				}
			}
		}*/
		return 1;
	}
}
