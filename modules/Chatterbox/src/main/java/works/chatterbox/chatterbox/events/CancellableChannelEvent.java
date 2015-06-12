/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.events;

import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.events.channels.ChannelEvent;

public abstract class CancellableChannelEvent extends ChannelEvent implements Cancellable {

    private boolean cancelled;

    public CancellableChannelEvent(final boolean isAsync, @NotNull final Channel channel) {
        super(isAsync, channel);
    }

    public CancellableChannelEvent(@NotNull final Channel channel) {
        this(false, channel);
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
