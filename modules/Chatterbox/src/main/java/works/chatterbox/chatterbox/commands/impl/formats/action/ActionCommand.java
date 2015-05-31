/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.commands.impl.formats.action;

import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.commands.ReflectCommand;
import works.chatterbox.chatterbox.commands.impl.formats.FormatCommand;

@ReflectCommand(
    name = "action",
    description = "Performs an action.",
    aliases = {"me"},
    usage = "/<command> [message]",
    keys = {
        "chatterbox action",
        "chatterbox me",
        "chat action",
        "chat me"
    }
)
public class ActionCommand extends FormatCommand {

    public ActionCommand(final Chatterbox instance, final String name) {
        super(instance, name, true, "action");
    }
}
