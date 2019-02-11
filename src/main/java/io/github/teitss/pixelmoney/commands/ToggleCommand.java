package io.github.teitss.pixelmoney.commands;

import io.github.teitss.pixelmoney.PixelMoney;
import io.github.teitss.pixelmoney.config.Config;
import io.github.teitss.pixelmoney.config.Group;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

public class ToggleCommand {

	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Toggle the money log messages"))
			.permission("pixelmoney.command.toggle")
			.executor(new CommandExecutor() {
				@Override
				public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
					if(src instanceof Player) {
                        Player player = (Player) src;
                        player.getOption("pixelmoney:group").ifPresent(value -> {
                            Group group = Config.getGroups().get(value);

                            if (group == null) {
                                player.sendMessage(Text.of(TextColors.RED, "PixelMoney didn't find your group configuration, please" +
                                        " contact an administrator."));
                                return;
                            }

                            if (PixelMoney.getInstance().getToggledPlayers().contains(player.getUniqueId())) {
                                PixelMoney.getInstance().getToggledPlayers().remove(player.getUniqueId());
                                player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(group.getTurnOnMessageLog()));
                            } else {
                                PixelMoney.getInstance().getToggledPlayers().add(player.getUniqueId());
                                player.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize(group.getTurnOffMessageLog())));
                            }
                        });

					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}
}
