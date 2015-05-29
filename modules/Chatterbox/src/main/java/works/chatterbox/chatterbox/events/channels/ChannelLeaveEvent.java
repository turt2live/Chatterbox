/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.events.channels;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.events.CancellableChannelEvent;
import works.chatterbox.chatterbox.wrappers.CPlayer;

public class ChannelLeaveEvent extends CancellableChannelEvent {

    private final CPlayer cplayer;

    public ChannelLeaveEvent(@NotNull final Channel channel, @NotNull final CPlayer cplayer) {
        super(channel);
        Preconditions.checkNotNull(cplayer, "cplayer was null");
        this.cplayer = cplayer;
    }

    public CPlayer getCPlayer() {
        return this.cplayer;
    }
}
