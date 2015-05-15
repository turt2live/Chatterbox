package works.chatterbox.chatterbox.pipeline.stages.impl.channel;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagStage implements Stage {

    private final Chatterbox chatterbox;
    private final Pattern tagPattern = Pattern.compile("^@([^\\s]+)");

    public TagStage(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
    }

    /**
     * This processes @-tags. If a message starts with {@code @tag} where {@code tag} is the tag of a channel, this will
     * reroute the message to that channel and remove the tag from the message.
     * <p>Intended effect: Reroute the message to the tagged channel
     */
    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        final String content = message.getMessage();
        if (!content.startsWith("@")) return;
        final Matcher m = tagPattern.matcher(content);
        if (!m.find()) return;
        final String tag = m.group(1);
        final Channel channel = this.chatterbox.getAPI().getChannelAPI().getChannelByTag(tag);
        // TODO: Should this cancel the message and send an error or just ignore the @-tag?
        if (channel == null || !message.getSender().getChannels().contains(channel)) return;
        message.setMessage(m.replaceFirst("").trim());
        message.setChannel(channel);
    }
}
