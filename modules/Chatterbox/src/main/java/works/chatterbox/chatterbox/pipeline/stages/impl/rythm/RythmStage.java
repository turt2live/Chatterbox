/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.pipeline.stages.impl.rythm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.messages.Message;
import works.chatterbox.chatterbox.pipeline.PipelineContext;
import works.chatterbox.chatterbox.pipeline.stages.Stage;

import java.util.Map;

public class RythmStage implements Stage {

    private final Chatterbox chatterbox;
    private final Map<String, Object> vars = Maps.newHashMap();

    public RythmStage(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
        this.vars.put("chatterbox", new ChatterboxSpecialUtilities());
    }

    private void addAncientVariables(@NotNull final Map<String, Object> vars, @NotNull final Message message) {
        Preconditions.checkNotNull(vars, "vars was null");
        Preconditions.checkNotNull(message, "message was null");
        final Player p = message.getSender().getPlayer();
        if (p != null) {
            vars.put("playerName", p.getName());
            vars.put("playerDisplayName", p.getDisplayName());
            vars.put("playerWorld", p.getWorld().getName());
            vars.put("playerUUID", p.getUniqueId().toString());
        }
        final Channel c = message.getChannel();
        vars.put("channelName", c.getName());
        vars.put("channelTag", c.getTag());
    }

    private Map<String, Object> getVariablesFor(@NotNull final Message message) {
        Preconditions.checkNotNull(message, "message was null");
        final Map<String, Object> vars = Maps.newHashMap();
        vars.put("message", message.getMessage());
        vars.put("cplayer", message.getSender());
        vars.put("player", message.getSender().getPlayer());
        vars.put("channel", message.getChannel());
        this.addAncientVariables(vars, message);
        return vars;
    }

    /**
     * This will process the message's format using the RythmEngine provided by
     * {@link works.chatterbox.chatterbox.api.rythm.RythmAPI RythmAPI}. Certain variables, such as the player, channel,
     * and message, are provided to the engine.
     * <p>Intended effect: The message's format is processed by Rythm
     */
    @Override
    public void process(@NotNull final Message message, @NotNull final PipelineContext context) {
        if (message.isCancelled()) return;
        final Map<String, Object> vars = Maps.newHashMap(context.getCustomVariables());
        vars.putAll(this.getVariablesFor(message));
        vars.putAll(this.vars);
        message.setFormat(
            this.chatterbox.getAPI().getRythmAPI().render(message.getFormat(), vars)
        );
    }
}
