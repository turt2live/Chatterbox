package works.chatterbox.chatterbox.commands.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.commands.ReflectCommand;
import works.chatterbox.chatterbox.commands.TabCommand;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.Collection;
import java.util.List;

@ReflectCommand(
    name = "channel",
    description = "Switches to (and joins, if necessary) a new channel.",
    aliases = {"ch"},
    usage = "/<command> [channel]"
)
public class ChannelCommand extends TabCommand<Chatterbox> {

    private final Joiner spaceJoiner = Joiner.on(' ');

    public ChannelCommand(final Chatterbox instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.LIST.getShort()});
    }

    @Override
    protected List<String> customList(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
        final Collection<String> namesAndTags = this.plugin.getAPI().getChannelAPI().getAllChannelNames();
        namesAndTags.addAll(this.plugin.getAPI().getChannelAPI().getAllChannelTags());
        return Lists.newArrayList(namesAndTags);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (eargs.length < 1 || !(cs instanceof Player)) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Player p = (Player) cs;
        final String channelName = this.spaceJoiner.join(eargs);
        final Channel channel = this.plugin.getAPI().getChannelAPI().getChannel(channelName);
        if (channel == null) {
            // TODO: Localization
            cs.sendMessage(ChatColor.RED + "No such channel.");
            return true;
        }
        final CPlayer cp = this.plugin.getAPI().getPlayerAPI().getCPlayer(p);
        cp.joinChannel(channel);
        cp.setMainChannel(channel);
        cs.sendMessage(ChatColor.BLUE + "Joined channel " + ChatColor.GRAY + channel.getName() + ChatColor.BLUE + ".");
        return true;
    }
}
