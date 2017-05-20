package com.teits.pixelmoney.exec;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.teits.pixelmoney.PixelMoney;

public class ReloadExecutor implements CommandExecutor {
	
	PluginContainer pc = Sponge.getPluginManager().getPlugin("pixelmoney").get();
	PixelMoney instance = (PixelMoney) pc.getInstance().get();
	
	
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		instance.init();
		instance.load();
		src.sendMessage(Text.of(TextColors.GREEN, "[PixelMoney] Config reloaded"));
		return CommandResult.success();
	}

}
