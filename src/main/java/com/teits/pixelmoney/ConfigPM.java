package com.teits.pixelmoney;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;

import com.google.common.base.CharMatcher;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigPM {
	
	public HashMap<Integer, Double> money = new HashMap<>();
	public HashMap<Integer, Boolean> levelbased = new HashMap<>();
	public HashMap<Integer, String> operationType = new HashMap<>();
	public HashMap<Integer, Boolean> boss = new HashMap<>();
	public HashMap<Integer, Double> bequal = new HashMap<>();
	public HashMap<Integer, Double> buncommon = new HashMap<>();
	public HashMap<Integer, Double> brare = new HashMap<>();
	public HashMap<Integer, Double> blegendary = new HashMap<>();
	public HashMap<Integer, Double> bultimate = new HashMap<>();
	public HashMap<Integer, Boolean> npc = new HashMap<>();
	public ArrayList<String> perms = new ArrayList<>();
	public ArrayList<Integer> pw = new ArrayList<>();
	public BigDecimal amount;
	public String logmessage;
	public String reloadmessage;
	public String turnonlogmessage;
	public String turnofflogmessage;
	public String chattype;
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
	public void configUpdate(ConfigurationLoader<CommentedConfigurationNode> configManager, CommentedConfigurationNode confignode) {
		confignode.getNode("messages", "chat-type")
			.setValue("ACTION_BAR")
			.setComment("Chat type to send log message, you can use 'SYSTEM', 'CHAT' or 'ACTION_BAR'");
		confignode.getNode("group1", "npc", "enabled")
			.setValue(true)
			.setComment("Enable NPCs rewards");
		confignode.getNode("group1", "boss", "enabled")
			.setValue(true)
			.setComment("Enable reward for Boss Pokemon");
		confignode.getNode("group1", "boss", "equal-reward")
			.setValue(10)
			.setComment("Reward for the pokemon boss mode 'Equal'");
		confignode.getNode("group1", "boss", "uncommon-reward")
			.setValue(20)
			.setComment("Reward for the pokemon boss mode 'Uncommon'");
		confignode.getNode("group1", "boss", "rare-reward")
			.setValue(30)
			.setComment("Reward for the pokemon boss mode 'Rare'");
		confignode.getNode("group1", "boss", "legendary-reward")
			.setValue(40)
			.setComment("Reward for the pokemon boss mode 'Legendary'");
		confignode.getNode("group1", "boss", "ultimate-reward")
			.setValue(50)
			.setComment("Reward for the pokemon boss mode 'Ultimate'");
		confignode.getNode("configVer").setValue(2);
		try {
			configManager.save(confignode);
		}
		catch (IOException io) {
			io.printStackTrace();
		}
		
	}
	public void configSetup(Path filePath, ConfigurationLoader<CommentedConfigurationNode> configManager, CommentedConfigurationNode confignode) {
		try {
			Files.createFile(filePath);
			confignode = configManager.createEmptyNode();
			confignode.getNode("configVer").setValue(1).setComment("Don't change this!");
			confignode.getNode("number-of-groups").setValue(1).setComment("If you want to add more groups, change this number");
			confignode.getNode("group1", "money").setValue(10)
				.setComment("Amount of money to be rewarded/Number to be used in levelbased's mathematical operations");
			confignode.getNode("group1", "npc", "enabled")
				.setValue(true)
				.setComment("Enable NPCs rewards");
			confignode.getNode("group1", "weight")
				.setValue(1)
				.setComment("Weight is the main identifier of a group, set different weights for different groups");
			confignode.getNode("group1", "boss", "enabled")
				.setValue(true)
				.setComment("Enable reward for Boss Pokemon");
			confignode.getNode("group1", "boss", "equal-reward")
				.setValue(10)
				.setComment("Reward for the pokemon boss mode 'Equal'");
			confignode.getNode("group1", "boss", "uncommon-reward")
				.setValue(20)
				.setComment("Reward for the pokemon boss mode 'Uncommon'");
			confignode.getNode("group1", "boss", "rare-reward")
				.setValue(30)
				.setComment("Reward for the pokemon boss mode 'Rare'");
			confignode.getNode("group1", "boss", "legendary-reward")
				.setValue(40)
				.setComment("Reward for the pokemon boss mode 'Legendary'");
			confignode.getNode("group1", "boss", "ultimate-reward")
				.setValue(50)
				.setComment("Reward for the pokemon boss mode 'Ultimate'");
			confignode.getNode("group1", "levelbased", "enabled")
				.setValue(false)
				.setComment("Enable/Disable per pokemon's level reward");
			confignode.getNode("group1", "levelbased", "operation-type")
				.setValue("MULTIPLICATION")
				.setComment("You can choose 'MULTIPLICATION', 'DIVISION', 'ADDITION' or 'SUBTRACTION'");
			confignode.getNode("messages", "log-message")
				.setValue("&a[Pixelmoney] &fYou've gained &2$%amount% for has defeated &6%pokemon%!")
				.setComment("You can use Ampersanding Formatting(&1&n) and the placeholders %amount%, %pokemon%");
			confignode.getNode("messages", "chat-type")
				.setValue("ACTION_BAR")
				.setComment("Chat type to send log message, you can use 'SYSTEM', 'CHAT' or 'ACTION_BAR'");
			confignode.getNode("messages", "turnon-message")
				.setValue("&a[Pixelmoney] You've turned on notifications")
				.setComment("You can use Ampersanding Formatting(&1&2)");
			confignode.getNode("messages", "turnoff-message")
				.setValue("&a[Pixelmoney] You've turned off notifications")
				.setComment("You can use Ampersanding Formatting(&1&2)");
			confignode.getNode("messages", "reload-message")
				.setValue("&a[Pixelmoney] Config reloaded!")
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
		logmessage = cn.getNode("messages", "log-message").getString();
		reloadmessage = cn.getNode("messages", "reload-message").getString();
		turnonlogmessage = cn.getNode("messages", "turnon-message").getString();
		turnofflogmessage = cn.getNode("messages", "turnoff-message").getString();
		chattype = cn.getNode("messages", "chat-type").getString();
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
				boss.put(Integer.valueOf(cn.getNode("group" + i, "weight").getInt()), Boolean.valueOf(cn.getNode("group" + i, "boss", "enabled").getBoolean()));
				bequal.put(Integer.valueOf(cn.getNode("group" + i, "weight").getInt()), Double.valueOf(cn.getNode("group" + i, "boss", "equal-reward").getDouble()));
				buncommon.put(Integer.valueOf(cn.getNode("group" + i, "weight").getInt()), Double.valueOf(cn.getNode("group" + i, "boss", "uncommon-reward").getDouble()));
				brare.put(Integer.valueOf(cn.getNode("group" + i, "weight").getInt()), Double.valueOf(cn.getNode("group" + i, "boss", "rare-reward").getDouble()));
				blegendary.put(Integer.valueOf(cn.getNode("group" + i, "weight").getInt()), Double.valueOf(cn.getNode("group" + i, "boss", "legendary-reward").getDouble()));
				bultimate.put(Integer.valueOf(cn.getNode("group" + i, "weight").getInt()), Double.valueOf(cn.getNode("group" + i, "boss", "ultimate-reward").getDouble()));
				npc.put(Integer.valueOf(cn.getNode("group" + i, "weight").getInt()), Boolean.valueOf(cn.getNode("group" + i, "npc", "enabled").getBoolean()));
			}
		}
	}
	public BigDecimal setAmountNormalWild(PixelmonWrapper wrapper, Player p) {
		if(levelbased.get(getWeight(p)).booleanValue() == true) {
			if(operationType.get(getWeight(p)).equals("MULTIPLICATION"))
				return amount = new BigDecimal(wrapper.getLevelNum() * money.get(getWeight(p)).doubleValue());
			else if(operationType.get(getWeight(p)).equals("DIVISION"))
				return amount = new BigDecimal(wrapper.getLevelNum() / money.get(getWeight(p)).doubleValue());
			else if(operationType.get(getWeight(p)).equals("ADDITION"))
				return amount = new BigDecimal(wrapper.getLevelNum() + money.get(getWeight(p)).doubleValue());
			else if(operationType.get(getWeight(p)).equals("SUBTRACTION"))
				return amount = new BigDecimal(wrapper.getLevelNum() - money.get(getWeight(p)).doubleValue());
		}	
		else {
			return amount = new BigDecimal(money.get(getWeight(p)).doubleValue());
		}
		return null;
	}
	public BigDecimal setAmountBossWild(EntityPixelmon poke, Player p) {
		if(boss.get(getWeight(p)).booleanValue() == true) {
			if(poke.getBossMode().equals(EnumBossMode.Equal))
				return amount = new BigDecimal(bequal.get(getWeight(p)).doubleValue());
			else if(poke.getBossMode().equals(EnumBossMode.Uncommon))
				return amount = new BigDecimal(buncommon.get(getWeight(p)).doubleValue());
			else if(poke.getBossMode().equals(EnumBossMode.Rare))
				return amount = new BigDecimal(brare.get(getWeight(p)).doubleValue());
			else if(poke.getBossMode().equals(EnumBossMode.Legendary))
				return amount = new BigDecimal(blegendary.get(getWeight(p)).doubleValue());
			else if(poke.getBossMode().equals(EnumBossMode.Ultimate))
				return amount = new BigDecimal(bultimate.get(getWeight(p)).doubleValue());
		}
		else
			return amount = new BigDecimal(money.get(getWeight(p)).doubleValue());
		return null;
	}
	public BigDecimal setAmountNormalNPC(NPCTrainer npc, Player p) {
		if(levelbased.get(getWeight(p)).booleanValue() == true) {
			if(operationType.get(getWeight(p)).equals("MULTIPLICATION"))
				return amount = new BigDecimal(npc.getLvl() * money.get(getWeight(p)).doubleValue());
			else if(operationType.get(getWeight(p)).equals("DIVISION"))
				return amount = new BigDecimal(npc.getLvl() / money.get(getWeight(p)).doubleValue());
			else if(operationType.get(getWeight(p)).equals("ADDITION"))
				return amount = new BigDecimal(npc.getLvl() + money.get(getWeight(p)).doubleValue());
			else if(operationType.get(getWeight(p)).equals("SUBTRACTION"))
				return amount = new BigDecimal(npc.getLvl() - money.get(getWeight(p)).doubleValue());
		}	
		else
			return amount = new BigDecimal(money.get(getWeight(p)).doubleValue());
		return null;
	}
	public BigDecimal setAmountBossNPC(NPCTrainer npc, Player p) {
		if(boss.get(getWeight(p)).booleanValue() == true) {
			if(npc.getBossMode().equals(EnumBossMode.Equal))
				return amount = new BigDecimal(bequal.get(getWeight(p)).doubleValue());
			else if(npc.getBossMode().equals(EnumBossMode.Uncommon))
				return amount = new BigDecimal(buncommon.get(getWeight(p)).doubleValue());
			else if(npc.getBossMode().equals(EnumBossMode.Rare))
				return amount = new BigDecimal(brare.get(getWeight(p)).doubleValue());
			else if(npc.getBossMode().equals(EnumBossMode.Legendary))
				return amount = new BigDecimal(blegendary.get(getWeight(p)).doubleValue());
			else if(npc.getBossMode().equals(EnumBossMode.Ultimate))
				return amount = new BigDecimal(bultimate.get(getWeight(p)).doubleValue());
		}
		else
			return amount = new BigDecimal(money.get(getWeight(p)).doubleValue());
		return null;
	}
	public ChatType getChatType(String type) {
		if(type.equals("CHAT"))
			return ChatTypes.CHAT;
		else if(type.equals("ACTION_BAR"))
			return ChatTypes.ACTION_BAR;
		else if(type.equals("SYSTEM"))
			return ChatTypes.SYSTEM;
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
