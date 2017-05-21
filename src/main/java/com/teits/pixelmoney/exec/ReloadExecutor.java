package com.teits.pixelmoney.exec;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.teits.pixelmoney.PixelMoney;

public class ReloadExecutor implements CommandExecutor {
	
	static PixelMoney instance;
	public static void set(PixelMoney p) {
		instance = p;
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		instance.config.configLoad(instance.config.configInit(instance.getConfigManager(), instance.getConfigNode()));
		src.sendMessage(Text.of(TextColors.GREEN, "[PixelMoney] Config reloaded!"));
		return CommandResult.success();
	}
}
