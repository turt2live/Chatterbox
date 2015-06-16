/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.api.impl.rythm;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rythmengine.RythmEngine;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.RythmAPI;

import java.util.Map;

/**
 * The Rythm API handles formatting and rendering via a {@link RythmEngine}.
 */
public class DefaultRythmAPI implements RythmAPI {

    private final RythmEngine rythm;
    private final Map<String, Object> variables = Maps.newHashMap();

    public DefaultRythmAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        final Map<String, Object> config = Maps.newHashMap();
        config.put("engine.class_loader.parent.impl", new RythmClassLoader(chatterbox.getClass().getClassLoader(), chatterbox));
        config.put("feature.type_inference.enabled", true);
        this.rythm = new RythmEngine(config);
    }

    private String addArgsDirective(@NotNull final String template, @NotNull final Map<String, Object> allArgs) {
        Preconditions.checkNotNull(template, "template was null");
        Preconditions.checkNotNull(allArgs, "allArgs was null");
        if (allArgs.isEmpty()) return template;
        final StringBuilder sb = new StringBuilder("@args ");
        allArgs.entrySet().stream()
            .forEach(
                entry ->
                    sb
                        .append(entry.getValue().getClass().getCanonicalName())
                        .append(" ")
                        .append(entry.getKey())
                        .append(", ")
            );
        return sb.substring(0, sb.length() - 2) + ";" + template;
    }

    private void ensureNotPrivate(@NotNull final String key) {
        Preconditions.checkNotNull(key, "key was null");
        Preconditions.checkArgument(!key.toLowerCase().startsWith("chatterbox"), "The chatterbox namespace cannot be modified");
    }

    @Override
    public void addVariable(@NotNull final String key, @Nullable final Object value) {
        Preconditions.checkNotNull(key, "key was null");
        this.ensureNotPrivate(key);
        this.variables.put(key, value);
    }

    @Override
    @NotNull
    public RythmEngine getRythmEngine() {
        return this.rythm;
    }

    @Override
    @NotNull
    public Map<String, Object> getVariables() {
        return ImmutableMap.copyOf(this.variables);
    }

    @Override
    public void removeVariable(@NotNull final String key) {
        Preconditions.checkNotNull(key, "key was null");
        this.ensureNotPrivate(key);
        this.variables.remove(key);
    }

    @Override
    @NotNull
    public String render(@NotNull final String template) {
        Preconditions.checkNotNull(template, "template was null");
        return this.render(template, Maps.newHashMap());
    }

    @Override
    @NotNull
    public String render(@NotNull final String template, @NotNull final Map<String, Object> extraVariables) {
        Preconditions.checkNotNull(template, "template was null");
        Preconditions.checkNotNull(extraVariables, "extraVariables was null");
        extraVariables.putAll(this.getVariables());
        return this.rythm.render(this.addArgsDirective(template, extraVariables), extraVariables);
    }

}
