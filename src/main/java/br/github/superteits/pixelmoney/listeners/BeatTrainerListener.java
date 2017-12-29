package br.github.superteits.pixelmoney.listeners;

import br.github.superteits.pixelmoney.PixelMoney;
import br.github.superteits.pixelmoney.Group;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.math.BigDecimal;
import java.util.Optional;

public class BeatTrainerListener {

    @SubscribeEvent
    public void defeatNPC(BeatTrainerEvent event) {
        if(PixelMoney.INSTANCE.getConfig().getEnabledDimensions().contains(event.player.dimension)) {
            Player p = (Player) event.player;
            if(p.hasPermission("pixelmoney.enable")) {
                Group group = PixelMoney.INSTANCE.getConfig().getGroups().get(PixelMoney.INSTANCE.getConfig().getGroupIndex(p));
                BigDecimal reward = BigDecimal.valueOf(0.0);
                Optional<UniqueAccount> uOpt = PixelMoney.INSTANCE.getEconomyService().getOrCreateAccount(p.getUniqueId());
                if(uOpt.isPresent()) {
                    UniqueAccount acc = uOpt.get();
                    if(event.trainer.getBossMode().equals(EnumBossMode.NotBoss)) {
                        reward = group.getMoneyReward(event.trainer);
                    }
                    else {
                        reward = group.getBossReward(event.trainer);
                    }
                    acc.deposit(PixelMoney.INSTANCE.getEconomyService().getDefaultCurrency(),
                            reward,
                            Cause.builder().append(PixelMoney.INSTANCE).build(EventContext.builder().add(EventContextKeys.PLUGIN, PixelMoney.INSTANCE.getPluginContainer()).build()));
                    if(!PixelMoney.INSTANCE.getToggledPlayers().contains(p.getUniqueId())) {
                        p.sendMessages(group.getChatType(), TextSerializers.FORMATTING_CODE.deserialize(group.getLogMessage()
                                .replaceAll("%amount%", reward.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString())
                                .replaceAll("%pokemon%", event.trainer.getName(event.trainer.getName()))));
                    }
                }
            }
        }
    }

}
