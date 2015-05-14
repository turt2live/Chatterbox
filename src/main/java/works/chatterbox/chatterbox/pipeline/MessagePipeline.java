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
