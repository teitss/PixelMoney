package io.github.teitss.pixelmoney.listeners;

import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
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

public class BeatWildPixelmonListener {

    @SubscribeEvent
    public void defeatWild(BeatWildPixelmonEvent event) {
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

            String normalReward = group.getNormalPokemonDefeatReward();
            String bossReward = group.getBossPokemonDefeatReward();

            if (normalReward.isEmpty() && bossReward.isEmpty())
                return;

            UniqueAccount acc = PixelMoney.getInstance().getEconomyService().getOrCreateAccount(player.getUniqueId()).get();

            for (PixelmonWrapper pw : event.wpp.allPokemon) {
                BigDecimal reward = BigDecimal.ZERO;
                EntityPixelmon pokemon = pw.entity;
                // Evaluates expressions from string using JS engine.
                if (pokemon.isBossPokemon() && !bossReward.isEmpty())
                    reward = handleExpression(bossReward, pokemon);
                else if (!normalReward.isEmpty())
                    reward = handleExpression(normalReward, pokemon);

                // Handle Happy Hour ability and Amulet Coin effect
                int rewardMultiplier = BattleRegistry.getBattle(event.player).getPlayer(event.player).getPrizeMoneyMultiplier();
                if (rewardMultiplier > 1 && group.isRewardMultiplierEnabled() && group.isExtendedMultiplierEnabled())
                    reward = reward.multiply(BigDecimal.valueOf(rewardMultiplier));

                //Deposits money
                acc.deposit(PixelMoney.getInstance().getEconomyService().getDefaultCurrency(), reward,
                        Sponge.getCauseStackManager().getCurrentCause());

                //Handle Log message send
                if (!PixelMoney.getInstance().getToggledPlayers().contains(player.getUniqueId())) {
                    String parsedMessage = StringUtils.replace(group.getDefeatMessage(),
                            "%reward%", reward.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
                    parsedMessage = StringUtils.replace(parsedMessage,
                            "%entity-name%", pokemon.getPokemonName());

                    player.sendMessages(group.getChatType(), TextSerializers.FORMATTING_CODE.deserialize(parsedMessage));
                }
            }
        });
    }

    private BigDecimal handleExpression(String expression, EntityPixelmon poke) {
        String parsedExpression = StringUtils.replace(expression, "%level%", String.valueOf(poke.getLvl().getLevel()));
        parsedExpression = StringUtils.replace(parsedExpression, "%boss-type%", String.valueOf(poke.getBossMode().index));
        try {
            return new BigDecimal(PixelMoney.getInstance().getScriptEngine().eval(parsedExpression).toString());
        } catch (ScriptException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

}
