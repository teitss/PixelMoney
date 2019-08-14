package io.github.teitss.pixelmoney.config;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.Map;

public class GroupSerializer {

    public static Map.Entry<String, Group> deserialize(ConfigurationNode configurationNode) throws ObjectMappingException {
        return Maps.immutableEntry(configurationNode.getNode("id").getString(), new Group(
                configurationNode.getNode("normalPokemonDefeatReward").getString(),
                configurationNode.getNode("bossPokemonDefeatReward").getString(),
                configurationNode.getNode("normalNPCDefeatReward").getString(),
                configurationNode.getNode("bossNPCDefeatReward").getString(),
                configurationNode.getNode("capturePokemonReward").getString(),
                configurationNode.getNode("dimensions").getList(TypeToken.of(Integer.class)),
                configurationNode.getNode("captureMessage").getString(),
                configurationNode.getNode("defeatMessage").getString(),
                configurationNode.getNode("chatType").getString(),
                configurationNode.getNode("turnOnMessageLog").getString(),
                configurationNode.getNode("turnOffMessageLog").getString(),
                configurationNode.getNode("enableRewardMultiplier").getBoolean(),
                configurationNode.getNode("enableExtendedRewardMultiplier").getBoolean(),
                configurationNode.getNode("disablePixelmonWinMoney").getBoolean(),
                configurationNode.getNode("disabledPokemons").getList(TypeToken.of(String.class))
        ));
    }
}
