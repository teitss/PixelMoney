package br.github.superteits.pixelmoney.listeners;

import br.github.superteits.pixelmoney.PixelMoney;
import br.github.superteits.pixelmoney.config.Group;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.math.BigDecimal;
import java.util.Optional;

public class BeatWildPixelmonListener {

    @SubscribeEvent
    public void defeatWild(BeatWildPixelmonEvent event) {
        if(PixelMoney.INSTANCE.getConfig().getEnabledDimensions().contains(event.player.dimension)) {
            Player p = (Player) event.player;
            if(p.hasPermission("pixelmoney.enable")) {
                Group group = PixelMoney.INSTANCE.getConfig().getGroups().get(PixelMoney.INSTANCE.getConfig().getGroupIndex(p));
                BigDecimal reward = BigDecimal.valueOf(0.0);
                Optional<UniqueAccount> uOpt = PixelMoney.INSTANCE.getEconomyService().getOrCreateAccount(p.getUniqueId());
                if (uOpt.isPresent()) {
                    UniqueAccount acc = uOpt.get();
                    for (PixelmonWrapper wrapper : event.wpp.allPokemon) {
                        EntityPixelmon poke = wrapper.pokemon;
                        if(poke.isBossPokemon()) {
                            reward = group.getBossReward(poke);
                        }
                        else {
                            reward = group.getMoneyReward(poke);
                        }
                        acc.deposit(PixelMoney.INSTANCE.getEconomyService().getDefaultCurrency(),
                                reward,
                                Cause.builder().append(PixelMoney.INSTANCE).build(EventContext.builder().add(EventContextKeys.PLUGIN, PixelMoney.INSTANCE.getPluginContainer()).build()));
                        if(!PixelMoney.INSTANCE.getToggledPlayers().contains(p.getUniqueId())) {
                            p.sendMessages(group.getChatType(), TextSerializers.FORMATTING_CODE.deserialize(group.getLogMessage()
                                    .replaceAll("%amount%", reward.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString())
                                    .replaceAll("%pokemon%", wrapper.getPokemonName())));
                        }
                    }
                }
            }
        }
    }

}
