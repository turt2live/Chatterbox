/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.commands.impl.formats.say;

import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.commands.ReflectCommand;
import works.chatterbox.chatterbox.commands.impl.formats.FormatCommand;

@ReflectCommand(
    name = "say",
    description = "Displays a message to the entire server.",
    usage = "/<command> [message]",
    keys = {
        "chatterbox say",
        "chat say"
    }
)
public class SayCommand extends FormatCommand {

    public SayCommand(final Chatterbox instance, final String name) {
        super(instance, name, true, "say");
    }
}
