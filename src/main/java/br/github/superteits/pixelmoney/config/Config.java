package br.github.superteits.pixelmoney.config;

import java.io.IOException;
import java.util.ArrayList;

import br.github.superteits.pixelmoney.Group;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class Config {

	private GroupSerializer serializer = new GroupSerializer();
	private ArrayList<Group> groups = new ArrayList<>();
	private ArrayList<Integer> enabledDimensions = new ArrayList<>();
	private String reloadMessage;
	public int configver;

	public void loadConfig(ConfigurationLoader<CommentedConfigurationNode> configManager) {
		try {
			CommentedConfigurationNode configNode = configManager.load();
			groups.clear();
			enabledDimensions.clear();
			for (ConfigurationNode node : configNode.getNode("Groups").getChildrenList()) {
				Group group = serializer.deserialize(TypeToken.of(Group.class), node);
				groups.add(group.getGroupID(), group);
			}
			for (ConfigurationNode node : configNode.getNode("Dimensions").getChildrenList()) {
				enabledDimensions.add(node.getInt());
			}
			reloadMessage = configNode.getNode("Reload message").getString();
		}
		catch(IOException e) {
			e.printStackTrace();
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}

	public void setupConfig(ConfigurationLoader<CommentedConfigurationNode> configManager) {
		try {
			CommentedConfigurationNode configNode = configManager.createEmptyNode();
			groups.add(new Group(0, 10.0, false, "ADDITION", true,
					10.0, 20.0, 30.0, 40.0,
					50.0, true, "&a[Pixelmoney] &fYou gained &2$%amount% &ffor defeating &6%pokemon%!",
					"&a[Pixelmoney] You turned on notifications", "&a[Pixelmoney] You turned off notifications",
					"ACTION_BAR"));
			enabledDimensions.add(0);
			configNode.getNode("Groups").setValue(new TypeToken<ArrayList<Group>>() {}, groups);
			configNode.getNode("Dimensions").setValue(enabledDimensions).setComment("Dimensions where plugin will work [0, -1, 47].");
			configNode.getNode("Reload message").setValue("&a[Pixelmoney] Config reloaded!");
			configManager.save(configNode);
		}
		catch(IOException e) {
			e.printStackTrace();
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}

	}

	public int getGroupIndex(Player p) {
		for(int i = groups.size() - 1; i > -1; i--) {
			if(p.hasPermission("pixelmoney.group." + i)) {
				return i;
			}
		}
		return 0;
	}

	public ArrayList<Integer> getEnabledDimensions() {
		return this.enabledDimensions;
	}

	public ArrayList<Group> getGroups() {
		return this.groups;
	}

	public String getReloadMessage() {
		return this.reloadMessage;
	}
}
