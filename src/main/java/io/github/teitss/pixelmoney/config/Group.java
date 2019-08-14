package io.github.teitss.pixelmoney.config;

import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;

import java.util.List;

public class Group {

    private String normalPokemonDefeatReward;
    private String bossPokemonDefeatReward;
    private String normalNPCDefeatReward;
    private String bossNPCDefeatReward;
    private String captureReward;
    private List<Integer> dimensions;
    private String captureMessage;
    private String defeatMessage;
    private String chatType;
    private String turnOnMessageLog;
    private String turnOffMessageLog;
    private boolean rewardMultiplierEnabled;
    private boolean extendedMultiplierEnabled;
    private boolean pixelmonWinMoneyDisabled;
    private List<String> disabledPokemons;

    public Group(String normalPokemonDefeatReward, String bossPokemonDefeatReward, String normalNPCDefeatReward,
                 String bossNPCDefeatReward, String captureReward, List<Integer> dimensions, String captureMessage,
                 String defeatMessage, String chatType, String turnOnMessageLog, String turnOffMessageLog,
                 boolean rewardMultiplierEnabled, boolean extendedMultiplierEnabled, boolean pixelmonWinMoneyDisabled,
                 List<String> disabledPokemons) {
        this.normalPokemonDefeatReward = normalPokemonDefeatReward;
        this.bossPokemonDefeatReward = bossPokemonDefeatReward;
        this.normalNPCDefeatReward = normalNPCDefeatReward;
        this.bossNPCDefeatReward = bossNPCDefeatReward;
        this.captureReward = captureReward;
        this.dimensions = dimensions;
        this.captureMessage = captureMessage;
        this.defeatMessage = defeatMessage;
        this.chatType = chatType;
        this.turnOnMessageLog = turnOnMessageLog;
        this.turnOffMessageLog = turnOffMessageLog;
        this.rewardMultiplierEnabled = rewardMultiplierEnabled;
        this.extendedMultiplierEnabled = extendedMultiplierEnabled;
        this.pixelmonWinMoneyDisabled = pixelmonWinMoneyDisabled;
        this.disabledPokemons = disabledPokemons;
    }

    public String getNormalPokemonDefeatReward() {
        return normalPokemonDefeatReward;
    }

    public String getBossPokemonDefeatReward() {
        return bossPokemonDefeatReward;
    }

    public String getNormalNPCDefeatReward() {
        return normalNPCDefeatReward;
    }

    public String getBossNPCDefeatReward() {
        return bossNPCDefeatReward;
    }

    public String getCaptureReward() {
        return captureReward;
    }

    public List<Integer> getDimensions() {
        return dimensions;
    }

    public String getCaptureMessage() {
        return captureMessage;
    }

    public String getDefeatMessage() {
        return defeatMessage;
    }

    public String getTurnOnMessageLog() {
        return turnOnMessageLog;
    }

    public String getTurnOffMessageLog() {
        return turnOffMessageLog;
    }

    public boolean isRewardMultiplierEnabled() {
        return rewardMultiplierEnabled;
    }

    public boolean isExtendedMultiplierEnabled() {
        return extendedMultiplierEnabled;
    }

    public boolean isPixelmonWinMoneyDisabled() {
        return pixelmonWinMoneyDisabled;
    }

    public List<String> getDisabledPokemons() {
        return disabledPokemons;
    }
    
    public ChatType getChatType() {
        switch (chatType) {
            case "SYSTEM":
                return ChatTypes.SYSTEM;
            case "ACTION_BAR":
                return ChatTypes.ACTION_BAR;
            default:
                return ChatTypes.CHAT;
        }
    }

}
