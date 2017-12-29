package br.github.superteits.pixelmoney.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.serializer.TextSerializers;

import br.github.superteits.pixelmoney.PixelMoney;

public class ReloadCommand implements CommandExecutor {
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		PixelMoney.INSTANCE.getConfig().loadConfig(PixelMoney.INSTANCE.getConfigManager());
		src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(PixelMoney.INSTANCE.getConfig().getReloadMessage()));
		return CommandResult.success();
	}
}
