/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.events.channels.messages;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.wrappers.CPlayer;

public class ChannelPostMessageEvent extends ChannelMessageEvent {

    public ChannelPostMessageEvent(@NotNull final Channel channel, @NotNull final CPlayer cplayer, @NotNull final Message message) {
        super(channel, cplayer, message);
    }
}
