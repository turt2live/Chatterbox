/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.titles;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.OptionalInt;

public class Titles {

    private final Map<Integer, Prefix> prefixes = Maps.newHashMap();
    private final Map<Integer, Suffix> suffixes = Maps.newHashMap();

    public void addPrefix(final int priority, @NotNull final Prefix prefix) {
        Preconditions.checkNotNull(priority, "priority was null");
        this.prefixes.put(priority, prefix);
    }

    public void addSuffix(final int priority, @NotNull final Suffix suffix) {
        Preconditions.checkNotNull(suffix, "suffix was null");
        this.suffixes.put(priority, suffix);
    }

    @Nullable
    public Prefix getPrefix(final int priority) {
        return this.prefixes.get(priority);
    }

    @Nullable
    public Prefix getPrefix() {
        final OptionalInt max = this.prefixes.keySet().stream()
            .mapToInt(key -> key)
            .max();
        if (!max.isPresent()) {
            return null;
        }
        return this.getPrefix(max.getAsInt());
    }

    @Nullable
    public Suffix getSuffix(final int priority) {
        return this.suffixes.get(priority);
    }

    @Nullable
    public Suffix getSuffix() {
        final OptionalInt max = this.suffixes.keySet().stream()
            .mapToInt(key -> key)
            .max();
        if (!max.isPresent()) {
            return null;
        }
        return this.getSuffix(max.getAsInt());
    }

}
