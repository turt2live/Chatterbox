package works.chatterbox.chatterbox.api.rythm;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rythmengine.RythmEngine;

import java.util.Map;

public class RythmAPI {

    private final RythmEngine rythm = new RythmEngine();
    private final Map<String, Object> perMessageVariables = Maps.newHashMap();
    private final Map<String, Object> variables = Maps.newHashMap();

    private void ensureNotPrivate(@NotNull final String key) {
        Preconditions.checkNotNull(key, "key was null");
        Preconditions.checkArgument(!key.toLowerCase().startsWith("chatterbox"), "The chatterbox namespace cannot be modified");
    }

    public void addVariable(@NotNull final String key, @Nullable final Object value) {
        Preconditions.checkNotNull(key, "key was null");
        this.ensureNotPrivate(key);
        this.variables.put(key, value);
    }

    @NotNull
    public Map<String, Object> getPerMessageVariables() {
        return this.perMessageVariables;
    }

    @NotNull
    public RythmEngine getRythmEngine() {
        return this.rythm;
    }

    /**
     * Gets an immutable copy of the variables map.
     *
     * @return Immutable variables map
     */
    @NotNull
    public Map<String, Object> getVariables() {
        return ImmutableMap.copyOf(this.variables);
    }

    public void removeVariable(@NotNull final String key) {
        Preconditions.checkNotNull(key, "key was null");
        this.ensureNotPrivate(key);
        this.variables.remove(key);
    }

    @NotNull
    public String render(@NotNull final String template) {
        Preconditions.checkNotNull(template, "template was null");
        return this.render(template, Maps.newHashMap());
    }

    @NotNull
    public String render(@NotNull final String template, @NotNull final Map<String, Object> extraVariables) {
        Preconditions.checkNotNull(template, "template was null");
        Preconditions.checkNotNull(extraVariables, "extraVariables was null");
        extraVariables.putAll(this.getVariables());
        return this.rythm.render(template, extraVariables);
    }

}
