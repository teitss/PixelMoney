package br.github.superteits.pixelmoney.commands;

import br.github.superteits.pixelmoney.Group;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import br.github.superteits.pixelmoney.PixelMoney;

public class ToggleCommad implements CommandExecutor {
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player p = (Player) src;
			Group group = PixelMoney.INSTANCE.getConfig().getGroups().get(PixelMoney.INSTANCE.getConfig().getGroupIndex(p));
			if(PixelMoney.INSTANCE.getToggledPlayers().contains(p.getUniqueId())) {
				PixelMoney.INSTANCE.getToggledPlayers().remove(p.getUniqueId());
				p.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(group.getTurnoOnLogMessage()));
			}
			else {
				PixelMoney.INSTANCE.getToggledPlayers().add(p.getUniqueId());
				p.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(group.getTurnOffLogMessage())));
			}
		}
		if(src instanceof ConsoleSource) {
			src.sendMessage(Text.of(TextColors.RED, "This command cannot be runned by Console"));
		}
		if(src instanceof CommandBlock) {
			src.sendMessage(Text.of(TextColors.RED, "This command cannot be runned by Command Block"));
		}
		return CommandResult.success();
	}

}
