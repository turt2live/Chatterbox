/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.commands.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.commands.BaseCommand;
import works.chatterbox.chatterbox.commands.ReflectCommand;

@ReflectCommand(
    name = "reload",
    description = "Reloads the plugin.",
    keys = {
        "chatterbox reload",
        "chat reload"
    }
)
public class ReloadCommand extends BaseCommand<Chatterbox> {

    public ReloadCommand(final Chatterbox instance, final String name) {
        super(instance, name, false);
    }

    @Override
    protected boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        this.plugin.onDisable();
        this.plugin.load(true);
        cs.sendMessage(ChatColor.BLUE + this.plugin.getAPI().getLanguageAPI().getLanguage(cs).getAString("RELOADED"));
        return true;
    }
}
