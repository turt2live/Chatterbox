/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline.stages.impl.json;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.ChatColor;
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
import works.chatterbox.chatterbox.shaded.mkremins.fanciful.MessagePart;
import works.chatterbox.chatterbox.shaded.mkremins.fanciful.TextualComponent;
import works.chatterbox.chatterbox.wrappers.CPlayer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JSONStage implements Stage {

    private final static Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
    private final Chatterbox chatterbox;
    private final Pattern startJSON = Pattern.compile(Pattern.quote(ChatterboxSpecialUtilities.getSignifier()) + "Start JSON (.+?)" + Pattern.quote(ChatterboxSpecialUtilities.getSignifier()));
    private final Pattern endJSON = Pattern.compile(Pattern.quote(ChatterboxSpecialUtilities.getSignifier()) + "End JSON (.+?)" + Pattern.quote(ChatterboxSpecialUtilities.getSignifier()));

    public JSONStage(final Chatterbox chatterbox) {
        this.chatterbox = chatterbox;
    }

    private void addToFancyMessage(@NotNull final FancyMessage message, @NotNull final ChatColor chatColor) {
        Preconditions.checkNotNull(message, "message was null");
        Preconditions.checkNotNull(chatColor, "chatColor was null");
        if (chatColor.isColor()) {
            message.color(chatColor);
        } else if (chatColor != ChatColor.RESET) {
            message.style(chatColor);
        }
    }

    @NotNull
    private ChatColor colorPart(@NotNull final MessagePart part, @NotNull ChatColor lastColor) {
        Preconditions.checkNotNull(part, "part was null");
        Preconditions.checkNotNull(lastColor, "lastColor was null");
        final List<ChatColor> colors = this.getLastColors(part.text.getReadableString());
        if (colors.isEmpty() || colors.stream().allMatch(ChatColor::isFormat)) {
            colors.add(lastColor);
        }
        final String stripped = ChatColor.stripColor(part.text.getReadableString());
        part.text = TextualComponent.rawText(stripped);
        for (final ChatColor color : colors) {
            if (color.isColor()) {
                lastColor = part.color = color;
            } else if (color.isFormat()) {
                part.styles.add(color);
            } else {
                lastColor = ChatColor.RESET;
            }
        }
        return lastColor;
    }

    @NotNull
    private List<ChatColor> getLastColors(@NotNull final String segment) {
        Preconditions.checkNotNull(segment, "segment was null");
        return Arrays.stream(ChatColor.getLastColors(segment).split("§"))
            .filter(s -> s.length() == 1)
            .map(ChatColor::getByChar)
            .filter(cc -> cc != null)
            .collect(Collectors.toList());
    }

    @Nullable
    private String getTooltip(@NotNull final Message message, @NotNull final String sectionName) {
        Preconditions.checkNotNull(message, "message was null");
        Preconditions.checkNotNull(sectionName, "sectionName was null");
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
            final String before = format.substring(lastCloseEnd, start.start());
            // Add content to the FancyMessage
            if (lastCloseEnd == 0) {
                fm.text(before);
            } else {
                fm.then(before);
            }
            // Add any necessary colors
            this.getLastColors(format.substring(0, lastCloseEnd)).forEach(cc -> this.addToFancyMessage(fm, cc));
            fm.then(format.substring(start.end(), end.start()));
            // Add any necessary colors
            this.getLastColors(format.substring(0, start.start())).forEach(cc -> this.addToFancyMessage(fm, cc));
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
            // Add any necessary colors
            this.getLastColors(format.substring(0, end.end())).forEach(cc -> this.addToFancyMessage(fm, cc));
        }
        // Return the JSON if we need to, otherwise null
        if (isJSON) {
            // Process old colors into JSON colors, fixing #3
            this.oldColorsToNew(fm);
            return fm;
        }
        return null;
    }

    @NotNull
    private MessagePart makeNewPart(@NotNull final String contents, @NotNull final MessagePart oldPart) {
        Preconditions.checkNotNull(contents, "contents was null");
        Preconditions.checkNotNull(oldPart, "oldPart was null");
        final MessagePart part;
        try {
            part = oldPart.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
        part.text = TextualComponent.rawText(contents);
        return part;
    }

    private void oldColorsToNew(@NotNull final FancyMessage message) {
        Preconditions.checkNotNull(message, "message was null");
        final List<MessagePart> newParts = Lists.newArrayList();
        for (final MessagePart part : message) {
            final String readable = part.text.getReadableString();
            final Matcher matcher = JSONStage.STRIP_COLOR_PATTERN.matcher(readable);
            int lastStart = 0;
            while (matcher.find()) {
                // substring from last end of colors to this start of colors
                final MessagePart newPart = this.makeNewPart(readable.substring(lastStart, matcher.start()), part);
                newParts.add(newPart);
                lastStart = matcher.start();
            }
            // substring from last start of colors to end
            newParts.add(this.makeNewPart(readable.substring(lastStart, readable.length()), part));
        }
        ChatColor lastColor = ChatColor.WHITE;
        final Iterator<MessagePart> partIterator = newParts.iterator();
        while (partIterator.hasNext()) {
            final MessagePart part = partIterator.next();
            lastColor = this.colorPart(part, lastColor);
            if (part.text.getReadableString().isEmpty()) {
                partIterator.remove();
            }
        }
        message.getMessageParts().clear();
        message.getMessageParts().addAll(newParts);
    }

    private boolean recipient(@NotNull final Message message, @NotNull final PipelineContext context) {
        Preconditions.checkNotNull(message, "message was null");
        Preconditions.checkNotNull(context, "context was null");
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

    private void sendToConsole(@NotNull final CPlayer cplayer, @NotNull final String message) {
        Preconditions.checkNotNull(cplayer, "cplayer was null");
        Preconditions.checkNotNull(message, "message was null");
        final Player p = cplayer.getPlayer();
        if (p != null) {
            p.getServer().getConsoleSender().sendMessage(message);
        }
    }

    private boolean single(@NotNull final Message message, final boolean toConsole) {
        Preconditions.checkNotNull(message, "message was null");
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
        Preconditions.checkNotNull(message, "message was null");
        Preconditions.checkNotNull(context, "context was null");
        if (message.isCancelled() || message instanceof JSONSectionMessage) return;
        if (this.recipient(message, context)) return;
        this.single(message, true);
    }
}
