package br.github.superteits.pixelmoney.config;

import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;

import java.math.BigDecimal;
import java.util.Objects;

public class Group {

    private int id;
    private double money;
    private boolean isLevelBased;
    private boolean isCaptureRewardEnabled;
    private String operationType;
    private boolean isBossRewardEnable;
    private double equalBossReward;
    private double uncommonBossReward;
    private double rareBossReward;
    private double legendaryBossReward;
    private double ultimateBossReward;
    private boolean isNpcRewardEnabled;
    private String logMessage;
    private String logMessage2;
    private String turnoOnLogMessage;
    private String turnOffLogMessage;
    private String chatType;

    public Group(int id, double money, boolean isLevelBased, boolean isCaptureRewardEnabled, String operationType, boolean isBossRewardEnable,
                 double equalBossReward, double uncommonBossReward, double rareBossReward, double legendaryBossReward,
                 double ultimateBossReward, boolean isNpcRewardEnabled, String logMessage, String logMessage2,
                 String turnoOnLogMessage, String turnOffLogMessage, String chatType) {
        this.id = id;
        this.money = money;
        this.isLevelBased = isLevelBased;
        this.isCaptureRewardEnabled = isCaptureRewardEnabled;
        this.operationType = operationType;
        this.isBossRewardEnable = isBossRewardEnable;
        this.equalBossReward = equalBossReward;
        this.uncommonBossReward = uncommonBossReward;
        this.rareBossReward = rareBossReward;
        this.legendaryBossReward = legendaryBossReward;
        this.ultimateBossReward = ultimateBossReward;
        this.isNpcRewardEnabled = isNpcRewardEnabled;
        this.logMessage = logMessage;
        this.logMessage2 = logMessage2;
        this.turnoOnLogMessage = turnoOnLogMessage;
        this.turnOffLogMessage = turnOffLogMessage;
        this.chatType = chatType;
    }

    public int getGroupID() {
        return this.id;
    }

    public double getMoney() {
        return money;
    }

    public boolean isLevelBased() {
        return isLevelBased;
    }

    public String getOperationType() {
        return operationType;
    }

    public boolean isBossRewardEnable() {
        return isBossRewardEnable;
    }

    public double getEqualBossReward() {
        return equalBossReward;
    }

    public double getUncommonBossReward() {
        return uncommonBossReward;
    }

    public double getRareBossReward() {
        return rareBossReward;
    }

    public double getLegendaryBossReward() {
        return legendaryBossReward;
    }

    public double getUltimateBossReward() {
        return ultimateBossReward;
    }

    public boolean isNpcRewardEnabled() {
        return isNpcRewardEnabled;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public String getTurnoOnLogMessage() {
        return turnoOnLogMessage;
    }

    public String getTurnOffLogMessage() {
        return turnOffLogMessage;
    }

    public boolean isCaptureRewardEnabled() {
        return isCaptureRewardEnabled;
    }

    public String getLogMessage2() {
        return logMessage2;
    }

    public ChatType getChatType() {
        if(chatType.equalsIgnoreCase("SYSTEM"))
            return ChatTypes.SYSTEM;
        else if(chatType.equalsIgnoreCase("ACTION_BAR"))
            return ChatTypes.ACTION_BAR;
        else
            return ChatTypes.CHAT;
    }

    public String getChatTypeAsString() {
        return chatType;
    }

    public BigDecimal getBossReward(EntityPixelmon p) {
        switch (p.getBossMode().index) {
            case 1:
                return BigDecimal.valueOf(equalBossReward);
            case 2:
                return BigDecimal.valueOf(uncommonBossReward);
            case 3:
                return BigDecimal.valueOf(rareBossReward);
            case 4:
                return BigDecimal.valueOf(legendaryBossReward);
            case 5:
                return BigDecimal.valueOf(ultimateBossReward);
            default:
                return BigDecimal.valueOf(equalBossReward);
        }
    }

    public BigDecimal getBossReward(NPCTrainer p) {
        switch (p.getBossMode().index) {
            case 1:
                return BigDecimal.valueOf(equalBossReward);
            case 2:
                return BigDecimal.valueOf(uncommonBossReward);
            case 3:
                return BigDecimal.valueOf(rareBossReward);
            case 4:
                return BigDecimal.valueOf(legendaryBossReward);
            case 5:
                return BigDecimal.valueOf(ultimateBossReward);
            default:
                return BigDecimal.valueOf(equalBossReward);
        }
    }

    public BigDecimal getMoneyReward(EntityPixelmon pixelmon) {
        BigDecimal reward = BigDecimal.valueOf(0.0);
        if(isLevelBased()) {
            switch(getOperationType()) {
                case "MULTIPLICATION":
                    reward = new BigDecimal(pixelmon.getLvl().getLevel() * getMoney());
                    break;
                case "DIVISION":
                    reward = new BigDecimal(pixelmon.getLvl().getLevel() / getMoney());
                    break;
                case "ADDITION":
                    reward = new BigDecimal(pixelmon.getLvl().getLevel() + getMoney());
                    break;
                case "SUBTRACTION":
                    reward = new BigDecimal(pixelmon.getLvl().getLevel() - getMoney());
                    break;
                default:
                    reward = new BigDecimal(pixelmon.getLvl().getLevel() + getMoney());
            }
        }
        else {
            reward = BigDecimal.valueOf(money);
        }
        return reward;
    }

    public BigDecimal getMoneyReward(NPCTrainer trainer) {
        BigDecimal reward = BigDecimal.valueOf(0.0);
        if(isLevelBased()) {
            switch(getOperationType()) {
                case "MULTIPLICATION":
                    reward = new BigDecimal(trainer.getLvl() * getMoney());
                    break;
                case "DIVISION":
                    reward = new BigDecimal(trainer.getLvl() / getMoney());
                    break;
                case "ADDITION":
                    reward = new BigDecimal(trainer.getLvl() + getMoney());
                    break;
                case "SUBTRACTION":
                    reward = new BigDecimal(trainer.getLvl() - getMoney());
                    break;
                default:
                    reward = new BigDecimal(trainer.getLvl() + getMoney());
            }
        }
        else {
            reward = BigDecimal.valueOf(money);
        }
        return reward;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Group) {
            Group group = (Group) obj;
            if(this.getGroupID() == group.getGroupID())
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Group{[ID=" + getGroupID() + "]}";
    }
}
