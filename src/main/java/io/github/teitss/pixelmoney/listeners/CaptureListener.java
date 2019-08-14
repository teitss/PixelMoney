package io.github.teitss.pixelmoney.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
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

public class CaptureListener {

    @SubscribeEvent
    public void onSuccessfulCapture(CaptureEvent.SuccessfulCapture event) {
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

            if (group.getDisabledPokemons().contains(event.getPokemon().getSpecies().name))
                return;
            
            String captureReward = group.getCaptureReward();

            if (captureReward.isEmpty())
                return;

            UniqueAccount acc = PixelMoney.getInstance().getEconomyService().getOrCreateAccount(player.getUniqueId()).get();
            EntityPixelmon poke = event.getPokemon();
            BigDecimal reward = handleExpression(captureReward, poke);
            acc.deposit(PixelMoney.getInstance().getEconomyService().getDefaultCurrency(),
                    reward,
                    Sponge.getCauseStackManager().getCurrentCause());
            if (!PixelMoney.getInstance().getToggledPlayers().contains(player.getUniqueId())) {
                String parsedMessage = StringUtils.replace(group.getCaptureMessage(), "%reward%", reward.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
                parsedMessage = StringUtils.replace(parsedMessage, "%entity-name%", poke.getPokemonName());
                player.sendMessages(group.getChatType(), TextSerializers.FORMATTING_CODE.deserialize(parsedMessage));
            }
        });

    }

    private BigDecimal handleExpression(String expression, EntityPixelmon poke) {
        String parsedExpression = StringUtils.replace(expression, "%level%", String.valueOf(poke.getLvl().getLevel()));
        try {
            return new BigDecimal(PixelMoney.getInstance().getScriptEngine().eval(parsedExpression).toString());
        } catch (ScriptException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }

    }

}
