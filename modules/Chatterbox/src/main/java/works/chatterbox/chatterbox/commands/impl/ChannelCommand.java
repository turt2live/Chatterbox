/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.commands.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.commands.ChannelTabCommand;
import works.chatterbox.chatterbox.commands.ReflectCommand;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.Collection;
import java.util.List;

@ReflectCommand(
    name = "channel",
    description = "Switches to (and joins, if necessary) a new channel.",
    aliases = {"ch"},
    usage = "/<command> [channel]"
)
public class ChannelCommand extends ChannelTabCommand {

    private final Joiner spaceJoiner = Joiner.on(' ');

    public ChannelCommand(final Chatterbox instance, final String name) {
        super(instance, name, true, new Short[0]);
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
            cs.sendMessage(ChatColor.RED + this.plugin.getLanguage().getAString("NO_SUCH_CHANNEL"));
            return true;
        }
        final CPlayer cp = this.plugin.getAPI().getPlayerAPI().getCPlayer(p);
        final boolean wasInChannel = cp.getChannels().contains(channel);
        if (!wasInChannel) {
            cp.joinChannel(channel);
        }
        cp.setMainChannel(channel);
        cs.sendMessage(ChatColor.BLUE + this.plugin.getLanguage().getFormattedString(wasInChannel ? "NEW_MAIN_CHANNEL" : "JOINED_CHANNEL", ChatColor.GRAY + channel.getName() + ChatColor.BLUE));
        return true;
    }
}
