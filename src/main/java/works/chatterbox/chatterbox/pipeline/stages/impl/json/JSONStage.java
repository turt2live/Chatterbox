package works.chatterbox.chatterbox.pipeline.stages.impl.json;

import com.google.common.base.Preconditions;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.messages.JSONSectionMessage;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;
import works.chatterbox.chatterbox.pipeline.stages.impl.rythm.ChatterboxSpecialUtilities;
import works.chatterbox.chatterbox.shaded.mkremins.fanciful.FancyMessage;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONStage implements Stage {

    private final Chatterbox chatterbox;
    private final Pattern startJSON = Pattern.compile(ChatterboxSpecialUtilities.getSignifier() + "Start JSON (.+?)" + ChatterboxSpecialUtilities.getSignifier());
    private final Pattern endJSON = Pattern.compile(ChatterboxSpecialUtilities.getSignifier() + "End JSON (.+?)" + ChatterboxSpecialUtilities.getSignifier());

    public JSONStage(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
    }

    @Nullable
    private String getTooltip(final Message message, final String sectionName) {
        // Get the section for this section name
        String section = message.getChannel().getJSONSection(sectionName);
        // Assert that we have some JSON
        Preconditions.checkState(section != null, "JSON section " + sectionName + " was empty");
        // Remove the ending newline
        if (section.endsWith("\n")) {
            section = section.substring(0, section.length() - 1);
        }
        // Make a special JSONSectionMessage for it
        final JSONSectionMessage jsonMessage = new JSONSectionMessage(message, section);
        // Run it through the pipeline
        this.chatterbox.getAPI().getMessageAPI().getMessagePipeline().send(jsonMessage);
        // If something cancelled it, move on (like cancelJSON())
        return jsonMessage.isCancelled() ? null : jsonMessage.getFormat();
    }

    @Nullable
    private FancyMessage handleJSON(@NotNull final Message message) {
        Preconditions.checkNotNull(message, "message was null");
        final String format = message.getFormat();
        final Matcher start = this.startJSON.matcher(format);
        final Matcher end = this.endJSON.matcher(format);
        final FancyMessage fm = new FancyMessage();
        boolean isJSON = false;
        int lastCloseEnd = 0;
        // While we can find a start AND an end tag
        while (start.find() && end.find()) {
            // Get the name
            final String sectionName = start.group(1);
            // Assert that the names are the same (can't have JSON within JSON)
            Preconditions.checkState(sectionName.equals(end.group(1)), "JSON sections did not match");
            // Remove the tags
            message.setFormat(
                format.substring(0, start.start()) +
                    format.substring(start.end(), end.start()) +
                    format.substring(end.end(), format.length())
            );
            // Add content to the FancyMessage
            if (lastCloseEnd == 0) {
                fm.text(format.substring(lastCloseEnd, start.start()));
            } else {
                fm.then(format.substring(lastCloseEnd, start.start()));
            }
            fm.then(format.substring(start.end(), end.start()));
            // Get the tooltip
            final String tooltip = this.getTooltip(message, sectionName);
            // If it isn't null, apply it
            if (tooltip != null) {
                fm.tooltip(tooltip);
                // We need to send JSON messages now
                isJSON = true;
            }
            // Set the last close
            lastCloseEnd = end.end();
        }
        // Empty message
        if (lastCloseEnd == 0) return null;
        // Add on any bits at the end
        if (end.end() != format.length()) {
            fm.then(format.substring(end.end(), format.length()));
        }
        // Return the JSON if we need to, otherwise null
        return isJSON ? fm : null;
    }

    private boolean recipient(final Message message, final PipelineContext context) {
        final ConfigurationNode recipientMessages = context.getProperties().getNode("recipientMessages");
        if (recipientMessages.isVirtual()) return false;
        for (final Player p : message.getRecipients()) {
            final String format = recipientMessages.getNode(p.getUniqueId()).getString();
            p.sendMessage(format);
        }
        this.sendToConsole(message.getSender(), message.getFormat());
        message.setCancelled(true);
        return true;
    }

    private void sendToConsole(final CPlayer cplayer, final String message) {
        final Player p = cplayer.getPlayer();
        if (p != null) {
            p.getServer().getConsoleSender().sendMessage(message);
        }
    }

    private boolean single(final Message message, final boolean toConsole) {
        final FancyMessage fm = this.handleJSON(message);
        if (fm == null) {
            // It's not JSON, so use the server
            return false;
        }
        // Don't use the server
        message.setCancelled(true);
        // Send JSON to everyone
        message.getRecipients().forEach(fm::send);
        if (toConsole) {
            // Send it to console, if we can
            this.sendToConsole(message.getSender(), fm.toOldMessageFormat());
        }
        return true;
    }

    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        if (message.isCancelled() || message instanceof JSONSectionMessage) return;
        if (this.recipient(message, context)) return;
        this.single(message, true);
    }
}
