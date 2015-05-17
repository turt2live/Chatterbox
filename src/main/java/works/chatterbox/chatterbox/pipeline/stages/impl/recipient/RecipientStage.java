package works.chatterbox.chatterbox.pipeline.stages.impl.recipient;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.messages.RecipientSectionMessage;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;
import works.chatterbox.chatterbox.pipeline.stages.impl.rythm.ChatterboxSpecialUtilities;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecipientStage implements Stage {

    private final Chatterbox chatterbox;
    private final Pattern recipient = Pattern.compile(ChatterboxSpecialUtilities.getSignifier() + "Recipient (.+?)" + ChatterboxSpecialUtilities.getSignifier());

    public RecipientStage(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
    }

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        if (message.isCancelled()) return;
        final Matcher m = this.recipient.matcher(message.getFormat());
        final Map<UUID, String> messages = Maps.newHashMap();
        boolean isRecipient = false;
        for (final Player player : message.getRecipients()) {
            String playerFormat = message.getFormat();
            while (m.find()) {
                String replace = message.getChannel().getRecipientSection(m.group(1));
                if (replace != null) {
                    final Message recipientMessage = new RecipientSectionMessage(message, replace);
                    final PipelineContext recipientContext = new PipelineContext();
                    recipientContext.getCustomVariables().put("recipient", player);
                    this.chatterbox.getAPI().getMessageAPI().getMessagePipeline().send(recipientMessage, recipientContext);
                    replace = recipientMessage.isCancelled() ? null : recipientMessage.getFormat();
                }
                if (replace != null) isRecipient = true;
                playerFormat = m.replaceFirst(replace == null ? "" : replace);
            }
            messages.put(player.getUniqueId(), playerFormat);
        }
        if (!isRecipient) return;
        context.getProperties().getNode("recipientMessages").setValue(messages);
    }
}
