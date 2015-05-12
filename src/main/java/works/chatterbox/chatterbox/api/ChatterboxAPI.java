package works.chatterbox.chatterbox.api;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.channel.ChannelAPI;
import works.chatterbox.chatterbox.api.message.MessageAPI;
import works.chatterbox.chatterbox.api.player.PlayerAPI;
import works.chatterbox.chatterbox.api.rythm.RythmAPI;

public class ChatterboxAPI {

    private final Chatterbox chatterbox;
    private final PlayerAPI playerAPI;
    private final MessageAPI messageAPI;
    private final ChannelAPI channelAPI;
    private final RythmAPI rythmAPI;

    public ChatterboxAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
        this.playerAPI = new PlayerAPI(this.chatterbox);
        this.messageAPI = new MessageAPI(this.chatterbox);
        this.channelAPI = new ChannelAPI(this.chatterbox);
        this.rythmAPI = new RythmAPI();
    }

    @NotNull
    public ChannelAPI getChannelAPI() {
        return this.channelAPI;
    }

    @NotNull
    public Chatterbox getChatterbox() {
        return this.chatterbox;
    }

    @NotNull
    public MessageAPI getMessageAPI() {
        return this.messageAPI;
    }

    /**
     * Gets the player API, which has methods pertaining to players and their various functions in Chatterbox.
     *
     * @return PlayerAPI
     */
    @NotNull
    public PlayerAPI getPlayerAPI() {
        return this.playerAPI;
    }

    @NotNull
    public RythmAPI getRythmAPI() {
        return this.rythmAPI;
    }
}
