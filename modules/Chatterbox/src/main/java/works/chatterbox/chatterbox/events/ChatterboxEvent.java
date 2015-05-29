/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class ChatterboxEvent extends Event {

    private final static HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return ChatterboxEvent.handlerList;
    }
}
