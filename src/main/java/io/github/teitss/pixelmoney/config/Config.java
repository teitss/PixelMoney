package io.github.teitss.pixelmoney.config;

import io.github.teitss.pixelmoney.PixelMoney;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.asset.Asset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private static HashMap<String, Group> groups = new HashMap<>();

    public static void setup(Path path, ConfigurationLoader<CommentedConfigurationNode> configManager) {
        if (Files.notExists(path.resolve("pixelmoney.conf")))
            install(path);
        load(configManager);

    }

    public static void load(ConfigurationLoader<CommentedConfigurationNode> configManager) {
        try {
            ConfigurationNode configNode = configManager.load();
            configNode.getNode("groups").getChildrenList().forEach(value -> {
                try {
                    Map.Entry entry = GroupSerializer.deserialize(value);
                    groups.put(entry.getKey().toString(), (Group) entry.getValue());
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void install(Path path) {
        try {
            Asset configFile = PixelMoney.getInstance().getPluginContainer().getAsset("pixelmoney.conf").get();
            configFile.copyToDirectory(path);
            PixelMoney.getInstance().getLogger().info("PixelMoney configuration installed with success.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Group> getGroups() {
        return groups;
    }

}
