package io.github.teitss.pixelmoney.commands;

import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class BaseCommand {

    CommandSpec commandSpec = CommandSpec.builder()
            .description(Text.of("PixelMoney's base command"))
            .child(new ToggleCommand().getCommandSpec(), "togglemsg")
            .build();

    public CommandSpec getCommandSpec() {
        return commandSpec;
    }
}
