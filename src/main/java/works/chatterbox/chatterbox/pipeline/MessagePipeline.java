package works.chatterbox.chatterbox.pipeline;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
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

    private int indexOf(final Class<? extends Stage> clazz) {
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
    public boolean addStageAfter(final Class<? extends Stage> clazz, final Stage stage) {
        final int index = this.indexOf(clazz);
        if (index == -1) return false;
        this.stages.add(index + 1, stage);
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
    public boolean addStageBefore(final Class<? extends Stage> clazz, final Stage stage) {
        final int index = this.indexOf(clazz);
        if (index == -1) return false;
        this.stages.add(index, stage);
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
     * Sends a message through this pipeline, possibly modifying it in each stage.
     *
     * @param message Message to send through pipeline
     */
    public void send(@NotNull final Message message) {
        Preconditions.checkNotNull(message, "message was null");
        final PipelineContext context = new PipelineContext();
        this.stages.forEach(stage -> stage.process(message, context));
    }
}
