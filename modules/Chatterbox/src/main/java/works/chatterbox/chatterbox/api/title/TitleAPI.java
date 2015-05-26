/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.api.title;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.titles.Prefix;
import works.chatterbox.chatterbox.titles.Suffix;
import works.chatterbox.chatterbox.titles.Titles;

import java.util.UUID;

public class TitleAPI {

    private final Chatterbox chatterbox;
    private final LoadingCache<UUID, Titles> titlesCache = CacheBuilder.newBuilder()
        .build(new CacheLoader<UUID, Titles>() {
            @Override
            public Titles load(@NotNull final UUID key) throws Exception {
                return TitleAPI.this.populateDefaultTitles(key, new Titles());
            }
        });

    public TitleAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
    }

    private void addTitles(final int priority, final Titles titles, final ConfigurationNode node) {
        if (node.isVirtual()) return;
        final ConfigurationNode prefix = node.getNode("prefix");
        if (!prefix.isVirtual()) {
            titles.addPrefix(priority, new Prefix(prefix.getString()));
        }
        final ConfigurationNode suffix = node.getNode("suffix");
        if (!suffix.isVirtual()) {
            titles.addSuffix(priority, new Suffix(suffix.getString()));
        }
    }

    @NotNull
    private Titles populateDefaultTitles(@NotNull final UUID uuid, @NotNull final Titles titles) {
        Preconditions.checkNotNull(uuid, "uuid was null");
        Preconditions.checkNotNull(titles, "titles was null");
        final ConfigurationNode node = this.chatterbox.getConfiguration().getNode("titles").getNode("individuals").getNode(uuid.toString().toLowerCase());
        this.addTitles(100, titles, node);
        final ConfigurationNode asterisk = this.chatterbox.getConfiguration().getNode("titles").getNode("*");
        this.addTitles(0, titles, asterisk);
        return titles;
    }

    @NotNull
    public Titles getTitles(@NotNull final UUID uuid) {
        return this.titlesCache.getUnchecked(uuid);
    }

    public void invalidate(@NotNull final UUID uuid) {
        Preconditions.checkNotNull(uuid, "uuid was null");
    }
}
