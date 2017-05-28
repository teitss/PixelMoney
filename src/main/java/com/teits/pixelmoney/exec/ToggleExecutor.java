package com.teits.pixelmoney.exec;

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

import com.teits.pixelmoney.PixelMoney;

public class ToggleExecutor implements CommandExecutor {

	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player p = (Player) src;
			if(PixelMoney.toggle.contains(p.getUniqueId())) {
				PixelMoney.toggle.remove(p.getUniqueId());
				p.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(ReloadExecutor.instance.config.turnonlogmessage));
			}
			else {
				PixelMoney.toggle.add(p.getUniqueId());
				p.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(ReloadExecutor.instance.config.turnofflogmessage)));
			}
		}
		if(src instanceof ConsoleSource) {
			src.sendMessage(Text.of(TextColors.RED, "This command cannot be runned for Console"));
		}
		if(src instanceof CommandBlock) {
			src.sendMessage(Text.of(TextColors.RED, "This command cannot be runned for Command Block"));
		}
		return CommandResult.success();
	}

}
