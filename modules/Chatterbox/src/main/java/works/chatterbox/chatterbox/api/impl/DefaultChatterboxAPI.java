/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package works.chatterbox.chatterbox.api.impl;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.LanguageAPI;
import works.chatterbox.chatterbox.api.ChannelAPI;
import works.chatterbox.chatterbox.api.ChatterboxAPI;
import works.chatterbox.chatterbox.api.MessageAPI;
import works.chatterbox.chatterbox.api.MessagingAPI;
import works.chatterbox.chatterbox.api.PlayerAPI;
import works.chatterbox.chatterbox.api.RythmAPI;
import works.chatterbox.chatterbox.api.TitleAPI;
import works.chatterbox.chatterbox.api.impl.channel.DefaultChannelAPI;
import works.chatterbox.chatterbox.api.impl.language.DefaultLanguageAPI;
import works.chatterbox.chatterbox.api.impl.message.DefaultMessageAPI;
import works.chatterbox.chatterbox.api.impl.messaging.DefaultMessagingAPI;
import works.chatterbox.chatterbox.api.impl.player.DefaultPlayerAPI;
import works.chatterbox.chatterbox.api.impl.rythm.DefaultRythmAPI;
import works.chatterbox.chatterbox.api.impl.title.DefaultTitleAPI;

public class DefaultChatterboxAPI implements ChatterboxAPI {

    private final Chatterbox chatterbox;
    private final PlayerAPI playerAPI;
    private final MessageAPI messageAPI;
    private final ChannelAPI channelAPI;
    private final RythmAPI rythmAPI;
    private final TitleAPI titleAPI;
    private final MessagingAPI messagingAPI;
    private final LanguageAPI languageAPI;

    public DefaultChatterboxAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
        this.playerAPI = new DefaultPlayerAPI(this.chatterbox);
        this.messageAPI = new DefaultMessageAPI(this.chatterbox);
        this.channelAPI = new DefaultChannelAPI(this.chatterbox);
        this.rythmAPI = new DefaultRythmAPI(this.chatterbox);
        this.titleAPI = new DefaultTitleAPI(this.chatterbox);
        this.messagingAPI = new DefaultMessagingAPI(this.chatterbox);
        this.languageAPI = new DefaultLanguageAPI(this.chatterbox);
    }

    @Override
    @NotNull
    public ChannelAPI getChannelAPI() {
        return this.channelAPI;
    }

    @Override
    @NotNull
    public Chatterbox getChatterbox() {
        return this.chatterbox;
    }

    @NotNull
    @Override
    public LanguageAPI getLanguageAPI() {
        return this.languageAPI;
    }

    @Override
    @NotNull
    public MessageAPI getMessageAPI() {
        return this.messageAPI;
    }

    @Override
    @NotNull
    public MessagingAPI getMessagingAPI() {
        return this.messagingAPI;
    }

    @Override
    @NotNull
    public PlayerAPI getPlayerAPI() {
        return this.playerAPI;
    }

    @Override
    @NotNull
    public RythmAPI getRythmAPI() {
        return this.rythmAPI;
    }

    @Override
    @NotNull
    public TitleAPI getTitleAPI() {
        return this.titleAPI;
    }
}
