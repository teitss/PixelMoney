package br.github.superteits.pixelmoney.config;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class GroupSerializer implements TypeSerializer<Group>{

    @Override
    public Group deserialize(TypeToken<?> typeToken, ConfigurationNode configurationNode) throws ObjectMappingException {
        int id = configurationNode.getNode("Id").getInt();
        double money = configurationNode.getNode("Reward").getDouble();
        boolean isLevelBased = configurationNode.getNode("Is level based").getBoolean();
        boolean isCaptureRewardEnabled = configurationNode.getNode("Is capture reward enabled").getBoolean();
        String operationType = configurationNode.getNode("Operation type").getString();
        boolean isBossRewardEnable = configurationNode.getNode("Is boss reward enabled").getBoolean();
        double equalBossReward = configurationNode.getNode("Equal boss reward").getDouble();
        double uncommonBossReward = configurationNode.getNode("Uncommon boss reward").getDouble();
        double rareBossReward = configurationNode.getNode("Rare boss reward").getDouble();
        double legendaryBossReward = configurationNode.getNode("Legendary boss reward").getDouble();
        double ultimateBossReward = configurationNode.getNode("Ultimate boss reward").getDouble();
        boolean isNpcRewardEnabled = configurationNode.getNode("Is NPC reward enabled").getBoolean();
        String logMessage = configurationNode.getNode("Log message").getString();
        String logMessage2 = configurationNode.getNode("Capture log message").getString();
        String turnoOnLogMessage = configurationNode.getNode("Turn on log message").getString();
        String turnOffLogMessage = configurationNode.getNode("Turn off log message").getString();
        String chatType = configurationNode.getNode("Chat type").getString();
        return new Group(id, money, isLevelBased, isCaptureRewardEnabled, operationType, isBossRewardEnable, equalBossReward, uncommonBossReward,
                rareBossReward, legendaryBossReward, ultimateBossReward, isNpcRewardEnabled, logMessage, logMessage2,
                turnoOnLogMessage, turnOffLogMessage, chatType);
    }

    @Override
    public void serialize(TypeToken<?> typeToken, Group group, ConfigurationNode configurationNode) throws ObjectMappingException {
        ((CommentedConfigurationNode) configurationNode).getNode("Id")
                .setComment("Must be an integer number and unique.")
                .setValue(group.getGroupID());
        ((CommentedConfigurationNode) configurationNode).getNode("Reward")
                .setComment("Reward = Amount of money to be rewarded/Number to be used in levelbased's mathematical operations.")
                .setValue(group.getMoney());
        ((CommentedConfigurationNode) configurationNode).getNode("Is level based")
                .setComment("Enable/Disable per pokemon's level reward.")
                .setValue(group.isLevelBased());
        ((CommentedConfigurationNode) configurationNode).getNode("Is capture reward enabled")
                .setComment("Enable/Disable capture rewards.")
                .setValue(group.isCaptureRewardEnabled());
        ((CommentedConfigurationNode) configurationNode).getNode("Operation type")
                .setComment("You can choose 'MULTIPLICATION', 'DIVISION', 'ADDITION' or 'SUBTRACTION'.")
                .setValue(group.getOperationType());
        ((CommentedConfigurationNode) configurationNode).getNode("Is boss reward enabled")
                .setComment("Enable reward for Boss Pokemon.")
                .setValue(group.isBossRewardEnable());
        configurationNode.getNode("Equal boss reward").setValue(group.getEqualBossReward());
        configurationNode.getNode("Uncommon boss reward").setValue(group.getUncommonBossReward());
        configurationNode.getNode("Rare boss reward").setValue(group.getRareBossReward());
        configurationNode.getNode("Legendary boss reward").setValue(group.getLegendaryBossReward());
        configurationNode.getNode("Ultimate boss reward").setValue(group.getUltimateBossReward());
        ((CommentedConfigurationNode) configurationNode).getNode("Is NPC reward enabled")
                .setComment("Enable NPCs rewards.")
                .setValue(group.isNpcRewardEnabled());
        ((CommentedConfigurationNode) configurationNode).getNode("Log message")
                .setComment("You can use Ampersanding Formatting(&1&n) and the placeholders %amount%, %pokemon%.")
                .setValue(group.getLogMessage());
        ((CommentedConfigurationNode) configurationNode).getNode("Capture log message")
                .setComment("You can use Ampersanding Formatting(&1&n) and the placeholders %amount%, %pokemon%.")
                .setValue(group.getLogMessage2());
        configurationNode.getNode("Turn on log message").setValue(group.getTurnoOnLogMessage());
        configurationNode.getNode("Turn off log message").setValue(group.getTurnOffLogMessage());
        ((CommentedConfigurationNode) configurationNode).getNode("Chat type")
                .setComment("Chat type to send log message, you can use 'SYSTEM', 'CHAT' or 'ACTION_BAR'.")
                .setValue(group.getChatTypeAsString());
    }
}
