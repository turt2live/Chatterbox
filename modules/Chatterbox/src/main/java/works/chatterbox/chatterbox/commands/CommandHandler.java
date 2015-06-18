/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.commands;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;

import java.util.Arrays;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;

public class CommandHandler {

    private final Chatterbox chatterbox;
    private final Joiner spaceJoiner = Joiner.on(' ');
    private final Set<CommandCoupling> commands = Sets.newHashSet();

    public CommandHandler(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
    }

    public void addCommand(@NotNull final CommandCoupling command) {
        Preconditions.checkNotNull(command, "command was null");
        this.commands.add(command);
    }

    @NotNull
    public Set<CommandCoupling> getCommands() {
        return ImmutableSet.copyOf(this.commands);
    }

    public void removeCommand(@NotNull final CommandCoupling command) {
        Preconditions.checkNotNull(command, "command was null");
        this.commands.remove(command);
    }

    public void runCommand(final CommandSender cs, final String label, final String[] args) {
        if (args.length < 1) return; // should never happen
        final String commandString = label + " " + this.spaceJoiner.join(args);
        final Map<Integer, CommandCoupling> matches = Maps.newHashMap();
        for (final CommandCoupling command : this.commands) {
            final ReflectCommand rc = command.getBaseCommand().getClass().getAnnotation(ReflectCommand.class);
            if (rc == null) continue;
            final String[] keys = rc.keys();
            for (final String key : keys) {
                if (!commandString.startsWith(key)) continue;
                matches.put(key.length(), command);
                break;
            }
        }
        final OptionalInt optionalInt = matches.keySet().stream().mapToInt(i -> i).max();
        if (!optionalInt.isPresent()) {
            cs.sendMessage(ChatColor.RED + this.chatterbox.getAPI().getLanguageAPI().getLanguage(cs).getAString("NO_SUCH_COMMAND"));
            return;
        }
        final CommandCoupling command = matches.get(optionalInt.getAsInt());
        command.getCommand().execute(cs, args[0], Arrays.copyOfRange(args, 1, args.length));
    }

    public static class CommandCoupling {

        private final BaseCommand<? extends Plugin> baseCommand;
        private final Command command;

        public CommandCoupling(@NotNull final BaseCommand<? extends Plugin> baseCommand, @NotNull final Command command) {
            Preconditions.checkNotNull(baseCommand, "baseCommand was null");
            Preconditions.checkNotNull(command, "command was null");
            this.baseCommand = baseCommand;
            this.command = command;
        }

        public BaseCommand<? extends Plugin> getBaseCommand() {
            return this.baseCommand;
        }

        public Command getCommand() {
            return this.command;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                .add("baseCommand", this.baseCommand)
                .add("command", this.command)
                .toString();
        }
    }
}
