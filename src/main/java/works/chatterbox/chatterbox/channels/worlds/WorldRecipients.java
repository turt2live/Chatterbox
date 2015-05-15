package works.chatterbox.chatterbox.channels.worlds;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WorldRecipients {

    private final Map<String, Boolean> individualWorlds = Maps.newHashMap();
    private final boolean toSelf, toAll;

    public WorldRecipients(@NotNull final Map<String, Boolean> worlds, final boolean toSelf, final boolean toAll) {
        Preconditions.checkNotNull(worlds, "worlds was null");
        this.toSelf = toSelf;
        this.toAll = toAll;
        this.individualWorlds.putAll(worlds);
    }

    @NotNull
    public Map<String, Boolean> getIndividualWorlds() {
        return ImmutableMap.copyOf(this.individualWorlds);
    }

    public boolean isToAll() {
        return this.toAll;
    }

    public boolean isToSelf() {
        return this.toSelf;
    }
}
