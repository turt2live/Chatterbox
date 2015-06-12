/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.events.channels;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.events.ChatterboxEvent;

public abstract class ChannelEvent extends ChatterboxEvent {

    protected Channel channel;

    public ChannelEvent(final boolean isAsync, @NotNull final Channel channel) {
        super(isAsync);
        Preconditions.checkNotNull(channel, "channel was null");
        this.channel = channel;
    }

    public ChannelEvent(@NotNull final Channel channel) {
        this(false, channel);
    }

    @NotNull
    public Channel getChannel() {
        return this.channel;
    }

}
