/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.events;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class ChatterboxEvent extends Event {

    private final static LoadingCache<String, HandlerList> handlerLists = CacheBuilder.newBuilder()
        .build(new CacheLoader<String, HandlerList>() {
            @Override
            public HandlerList load(@NotNull final String key) throws Exception {
                return new HandlerList();
            }
        });

    protected static HandlerList getHandlerList(@NotNull final String name) {
        Preconditions.checkNotNull(name, "name was null");
        return ChatterboxEvent.handlerLists.getUnchecked(name);
    }

    @Override
    public HandlerList getHandlers() {
        return ChatterboxEvent.handlerLists.getUnchecked(this.getClass().getName());
    }
}
