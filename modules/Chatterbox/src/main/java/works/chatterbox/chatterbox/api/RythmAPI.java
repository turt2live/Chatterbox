package works.chatterbox.chatterbox.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rythmengine.RythmEngine;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.pipeline.PipelineContext;

import java.util.Map;

public interface RythmAPI {

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
    void addVariable(@NotNull String key, @Nullable Object value);

    /**
     * Gets the RythmEngine object that
     * {@link works.chatterbox.chatterbox.pipeline.stages.impl.rythm.RythmStage RythmStage} uses to process Channel
     * formats ({@link Channel#getFormat()}.
     *
     * @return RythmEngine
     */
    @NotNull
    RythmEngine getRythmEngine();

    /**
     * Gets an immutable copy of the variables map. These are permanent variables that are passed to all messages.
     *
     * @return Immutable variables map
     */
    @NotNull
    Map<String, Object> getVariables();

    void removeVariable(@NotNull String key);

    /**
     * Renders a template using the RythmEngine returned by {@link #getRythmEngine()}.
     *
     * @param template Template to render
     * @return Rendered template
     */
    @NotNull
    String render(@NotNull String template);

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
    String render(@NotNull String template, @NotNull Map<String, Object> extraVariables);
}
