/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.channels;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public enum ChannelConfiguration {

    NAME("name"),
    TAG("tag"),
    FORMAT("format"),
    FORMAT_TEXT("text", FORMAT.getKey()),
    FORMAT_FILE("file", FORMAT.getKey()),
    FORMAT_JSON("json", FORMAT.getKey()),
    FORMAT_RECIPIENT("recipient", FORMAT.getKey()),
    PERMANENT("permanent"),
    RADIUS("radius"),
    RADIUS_ENABLED("enabled", RADIUS.getKey()),
    RADIUS_HORIZONTAL("horizontal", RADIUS.getKey()),
    RADIUS_VERTICAL("vertical", RADIUS.getKey()),
    MAXIMUM("maximum"),
    MAXIMUM_MEMBERS("members", MAXIMUM.getKey()),
    WORLDS("worlds"),
    WORLDS_ALL("all", WORLDS.getKey()),
    WORLDS_SELF("self", WORLDS.getKey()),
    WORLDS_INDIVIDUAL("individual", WORLDS.getKey());

    private final String key;
    private final String[] parents;

    ChannelConfiguration(@NotNull final String key, @NotNull final String... parents) {
        Preconditions.checkNotNull(key, "key was null");
        Preconditions.checkNotNull(parents, "parents was null");
        this.key = key;
        this.parents = parents;
    }

    public String getKey() {
        return this.key;
    }

    public String[] getParents() {
        return this.parents;
    }

    @Override
    public String toString() {
        return this.getKey();
    }
}
