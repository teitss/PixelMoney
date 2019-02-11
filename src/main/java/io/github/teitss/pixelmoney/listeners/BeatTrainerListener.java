package io.github.teitss.pixelmoney.listeners;

import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import io.github.teitss.pixelmoney.PixelMoney;
import io.github.teitss.pixelmoney.config.Config;
import io.github.teitss.pixelmoney.config.Group;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.script.ScriptException;
import java.math.BigDecimal;

public class BeatTrainerListener {

    @SubscribeEvent
    public void defeatNPC(BeatTrainerEvent event) {

        Player player = (Player) event.player;
        player.getOption("pixelmoney:group").ifPresent(value -> {
            Group group = Config.getGroups().get(value);

            if (group == null) {
                player.sendMessage(Text.of(TextColors.RED, "PixelMoney didn't find your group configuration, please" +
                        " contact an administrator."));
                return;
            }

            if (!group.getDimensions().contains(event.player.dimension))
                return;

            if (group.isPixelmonWinMoneyDisabled())
                event.trainer.winMoney = 0;

            String normalReward = group.getNormalNPCDefeatReward();
            String bossReward = group.getBossNPCDefeatReward();

            if (normalReward.isEmpty() && bossReward.isEmpty())
                return;

            UniqueAccount acc = PixelMoney.getInstance().getEconomyService().getOrCreateAccount(player.getUniqueId()).get();
            NPCTrainer trainer = event.trainer;
            BigDecimal reward = BigDecimal.ZERO;

            if (trainer.getBossMode() != EnumBossMode.NotBoss && !bossReward.isEmpty())
                reward = handleExpression(bossReward, trainer);
            else if (!normalReward.isEmpty())
                reward = handleExpression(normalReward, trainer);

            // Handle Happy Hour ability and Amulet Coin effect
            int rewardMultiplier = BattleRegistry.getBattle(event.player).getPlayer(event.player).getPrizeMoneyMultiplier();
            if (rewardMultiplier > 1 && group.isRewardMultiplierEnabled())
                reward = reward.multiply(BigDecimal.valueOf(rewardMultiplier));

            acc.deposit(PixelMoney.getInstance().getEconomyService().getDefaultCurrency(), reward,
                    Sponge.getCauseStackManager().getCurrentCause());
            if (!PixelMoney.getInstance().getToggledPlayers().contains(player.getUniqueId())) {
                String parsedMessage = StringUtils.replace(group.getDefeatMessage(),
                        "%reward%", reward.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
                parsedMessage = StringUtils.replace(parsedMessage,
                        "%entity-name%", trainer.getName(trainer.getName()));

                player.sendMessages(group.getChatType(), TextSerializers.FORMATTING_CODE.deserialize(parsedMessage));
            }

        });

    }

    private BigDecimal handleExpression(String expression, NPCTrainer trainer) {
        String parsedExpression = StringUtils.replace(expression, "%level%", String.valueOf(trainer.getLvl()));
        parsedExpression = StringUtils.replace(parsedExpression, "%boss-type%", String.valueOf(trainer.getBossMode().index));
        try {
            return new BigDecimal(PixelMoney.getInstance().getScriptEngine().eval(parsedExpression).toString());
        } catch (ScriptException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

}
