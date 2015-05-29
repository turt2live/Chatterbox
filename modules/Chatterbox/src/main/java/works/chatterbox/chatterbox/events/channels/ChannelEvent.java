/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.events.channels;

import com.google.common.base.Preconditions;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.events.ChatterboxEvent;

public abstract class ChannelEvent extends ChatterboxEvent implements Cancellable {

    private final Channel channel;
    private boolean cancelled;

    public ChannelEvent(@NotNull final Channel channel) {
        Preconditions.checkNotNull(channel, "channel was null");
        this.channel = channel;
    }

    @NotNull
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}
