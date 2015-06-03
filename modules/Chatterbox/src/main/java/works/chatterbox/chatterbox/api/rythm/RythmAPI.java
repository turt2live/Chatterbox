/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.api.rythm;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rythmengine.RythmEngine;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.pipeline.PipelineContext;

import java.util.Map;

/**
 * The Rythm API handles formatting and rendering via a {@link RythmEngine}.
 */
public class RythmAPI {

    private final RythmEngine rythm;
    private final Map<String, Object> variables = Maps.newHashMap();

    public RythmAPI(@NotNull final Chatterbox chatterbox) {
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

    /**
     * Adds a variable that will be available to all messages. Note that these variables will not be cleared or reset
     * between messages sent through the Pipeline. To add per-message variables, see
     * {@link PipelineContext#getCustomVariables()}.
     * <p>Note: @args directives are automatically added by
     * {@link works.chatterbox.chatterbox.pipeline.stages.impl.rythm.RythmStage RythmStage}.
     *
     * @param key   Key of the variable. It will be accessible to Rythm by this name
     * @param value Value of the variable
     */
    public void addVariable(@NotNull final String key, @Nullable final Object value) {
        Preconditions.checkNotNull(key, "key was null");
        this.ensureNotPrivate(key);
        this.variables.put(key, value);
    }

    /**
     * Gets the RythmEngine object that
     * {@link works.chatterbox.chatterbox.pipeline.stages.impl.rythm.RythmStage RythmStage} uses to process Channel
     * formats ({@link Channel#getFormat()}.
     *
     * @return RythmEngine
     */
    @NotNull
    public RythmEngine getRythmEngine() {
        return this.rythm;
    }

    /**
     * Gets an immutable copy of the variables map. These are permanent variables that are passed to all messages.
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

    /**
     * Renders a template using the RythmEngine returned by {@link #getRythmEngine()}.
     *
     * @param template Template to render
     * @return Rendered template
     */
    @NotNull
    public String render(@NotNull final String template) {
        Preconditions.checkNotNull(template, "template was null");
        return this.render(template, Maps.newHashMap());
    }

    /**
     * Renders a template using the RythmEngine returned by {@link #getRythmEngine()} and the extra variables provided.
     * The variables are the lowest priority, meaning that any internal variables or permanent variables
     * ({@link #getVariables()}) with the same key will override a key provided in {@code extraVariables}.
     *
     * @param template       Template to render
     * @param extraVariables Extra variables to provide the template
     * @return Rendered template
     */
    @NotNull
    public String render(@NotNull final String template, @NotNull final Map<String, Object> extraVariables) {
        Preconditions.checkNotNull(template, "template was null");
        Preconditions.checkNotNull(extraVariables, "extraVariables was null");
        extraVariables.putAll(this.getVariables());
        return this.rythm.render(this.addArgsDirective(template, extraVariables), extraVariables);
    }

}
