package com.teits.pixelmoney;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigPM {
	
	public double money;
	public boolean levelbased;
	public String operationType;
	public BigDecimal amount;
	
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
			confignode.getNode("pixelmoney", "money").setValue(10).setComment("Amount of money to be rewarded/Number to be used in levelbased's mathematical operations");
			confignode.getNode("pixelmoney", "levelbased", "enabled").setValue(false).setComment("Enable/Disable per pokemon's level reward");
			confignode.getNode("pixelmoney", "levelbased", "operation-type").setValue("MULTIPLICATION").setComment("You can choose 'MULTIPLICATION', 'DIVISION', 'ADDITION' or 'SUBTRACTION'");
			configManager.save(confignode);
		}
		catch (IOException ec){
			ec.printStackTrace();
		}
		
	}
	public void configLoad(CommentedConfigurationNode cn) {
		levelbased = cn.getNode("pixelmoney", "levelbased", "enabled").getBoolean();
		operationType = cn.getNode("pixelmoney", "levelbased", "operation-type").getString();
		money = cn.getNode("pixelmoney", "money").getInt();
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
}
