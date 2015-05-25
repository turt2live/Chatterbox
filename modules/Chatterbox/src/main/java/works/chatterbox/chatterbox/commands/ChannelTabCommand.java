package works.chatterbox.chatterbox.commands;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import works.chatterbox.chatterbox.Chatterbox;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public abstract class ChannelTabCommand extends TabCommand<Chatterbox> {

    protected ChannelTabCommand(final Chatterbox instance, final String name, final boolean checkPermissions, final Short[] cts) {
        super(instance, name, checkPermissions, Stream.concat(Arrays.stream(cts), Arrays.stream(new Short[]{CompletionType.LIST.getShort()})).toArray(Short[]::new));
    }

    @Override
    protected List<String> customList(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
        final Collection<String> namesAndTags = this.plugin.getAPI().getChannelAPI().getAllChannelNames();
        namesAndTags.addAll(this.plugin.getAPI().getChannelAPI().getAllChannelTags());
        return Lists.newArrayList(namesAndTags);
    }

}
