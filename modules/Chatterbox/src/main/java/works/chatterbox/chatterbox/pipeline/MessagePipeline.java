/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

import java.util.List;

/**
 * The message pipeline. Raw messages are sent through this pipeline in order to be processed. A Message object is given
 * to the {@link #send(Message)} method, which then applies the {@link Stage#process(Message, PipelineContext)} method
 * to it for every stage this pipeline contains, in order. When finished, the Message object may be changed and should
 * be ready for use.
 */
public class MessagePipeline {

    private final List<Stage> stages = Lists.newArrayList();

    private int indexOf(@NotNull final Class<? extends Stage> clazz) {
        Preconditions.checkNotNull(clazz, "clazz was null");
        int index = -1;
        for (int i = 0; i < this.stages.size(); i++) {
            final Stage s = this.stages.get(i);
            if (!clazz.isAssignableFrom(s.getClass())) continue;
            index = i;
            break;
        }
        return index;
    }

    /**
     * Adds a stage to this pipeline at the specified index.
     *
     * @param index Index to add stage at
     * @param stage Stage to add
     */
    public void addStage(int index, @NotNull final Stage stage) {
        Preconditions.checkArgument(index >= 0 && index <= this.stages.size(), "index was not valid");
        Preconditions.checkNotNull(stage, "stage was null");
        this.stages.add(index, stage);
    }

    /**
     * Adds a stage to this pipeline.
     *
     * @param stage Stage to add
     */
    public void addStage(@NotNull final Stage stage) {
        Preconditions.checkNotNull(stage, "stage was null");
        this.stages.add(stage);
    }

    /**
     * Adds a stage to this pipeline, after the first stage that is the same class as or a subclass of the given class.
     * <p>This will return false if the stage was not added, meaning that a class matching the class provided could not
     * be found.
     *
     * @param clazz Class to add stage after
     * @param stage Stage to add
     * @return true if the stage was added, false if not
     */
    public boolean addStageAfter(@NotNull final Class<? extends Stage> clazz, @NotNull final Stage stage) {
        Preconditions.checkNotNull(clazz, "clazz was null");
        Preconditions.checkNotNull(stage, "stage was null");
        final int index = this.indexOf(clazz);
        if (index == -1) return false;
        this.addStage(index + 1, stage);
        return true;
    }

    /**
     * Adds a stage to this pipeline, before the first stage that is the same class as or a subclass of the given class.
     * <p>This will return false if the stage was not added, meaning that a class matching the class provided could not
     * be found.
     *
     * @param clazz Class to add stage before
     * @param stage Stage to add
     * @return true if the stage was added, false if not
     */
    public boolean addStageBefore(@NotNull final Class<? extends Stage> clazz, @NotNull final Stage stage) {
        Preconditions.checkNotNull(clazz, "clazz was null");
        Preconditions.checkNotNull(stage, "stage was null");
        final int index = this.indexOf(clazz);
        if (index == -1) return false;
        this.addStage(index, stage);
        return true;
    }

    /**
     * Returns an immutable copy of the list of stages in this pipeline.
     *
     * @return Immutable list
     */
    @NotNull
    public List<Stage> getStages() {
        return ImmutableList.copyOf(this.stages);
    }

    /**
     * Removes a stage from this pipeline.
     *
     * @param stage Stage to remove
     * @return {@link List#remove(Object)}
     */
    public boolean removeStage(@NotNull final Stage stage) {
        Preconditions.checkNotNull(stage, "stage was null");
        return this.stages.remove(stage);
    }

    /**
     * Removes the first stage in the pipeline that is the same as or is a subclass of the provided class.
     * <p>This will return null if the stage was not removed, meaning that a class matching the class provided could
     * not be found.
     *
     * @param clazz Class of the stage to remove
     * @return Stage that was removed or null
     */
    @Nullable
    public Stage removeStage(@NotNull final Class<? extends Stage> clazz) {
        Preconditions.checkNotNull(clazz, "clazz was null");
        final int index = this.indexOf(clazz);
        if (index == -1) return null;
        return this.stages.remove(index);
    }

    /**
     * Sends a message through this pipeline, possibly modifying it in each stage.
     *
     * @param message Message to send through pipeline
     */
    public void send(@NotNull final Message message) {
        this.send(message, new PipelineContext());
    }

    /**
     * Sends a message through this pipeline with the given context, possibly modifying it in each stage.
     *
     * @param message Message to send through pipeline
     * @param context Context to use for pipeline
     */
    public void send(@NotNull final Message message, @NotNull final PipelineContext context) {
        Preconditions.checkNotNull(message, "message was null");
        Preconditions.checkNotNull(context, "context was null");
        this.stages.forEach(stage -> {
            try {
                final List<String> skipStages = context.getProperties().getNode("chatterbox_skip_stages").getList(input -> {
                    if (input == null) return null;
                    return input.toString();
                });
                if (skipStages.contains(stage.getClass().getCanonicalName())) return;
                stage.process(message, context);
            } catch (final Throwable t) {
                new RuntimeException("An exception occurred while running the stage " + stage.getClass().getSimpleName(), t).printStackTrace();
            }
        });
    }
}
