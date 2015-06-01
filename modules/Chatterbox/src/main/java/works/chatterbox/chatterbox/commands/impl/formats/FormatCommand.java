/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.commands.impl.formats;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.commands.BaseCommand;

import java.util.Map;

public abstract class FormatCommand extends BaseCommand<Chatterbox> {

    private final static Joiner spaceJoiner = Joiner.on(' ');
    private final String nodeName;

    public FormatCommand(final Chatterbox instance, final String name, final boolean checkPermissions, final String nodeName) {
        super(instance, name, checkPermissions);
        this.nodeName = nodeName;
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Map<String, Object> variables = Maps.newHashMap();
        variables.put("sender", cs);
        variables.put("message", FormatCommand.spaceJoiner.join(args));
        final String rendered = this.plugin.getAPI().getRythmAPI().render(
            this.plugin.getConfiguration().getNode("formats").getNode(this.nodeName).getString("@(message)"),
            variables
        );
        this.plugin.getServer().broadcastMessage(
            ChatColor.translateAlternateColorCodes('&', rendered)
        );
        return true;
    }
}
