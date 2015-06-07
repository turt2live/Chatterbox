/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.events.channels;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.events.ChatterboxEvent;

public class ChannelCreateEvent extends ChannelEvent {

    public ChannelCreateEvent(@NotNull final Channel channel) {
        super(channel);
    }

    public static HandlerList getHandlerList() {
        return ChatterboxEvent.getHandlerList(ChannelCreateEvent.class.getName());
    }

    public void setChannel(@NotNull final Channel channel) {
        this.channel = channel;
    }
}
