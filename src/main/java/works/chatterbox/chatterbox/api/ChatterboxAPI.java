package works.chatterbox.chatterbox.api;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.api.channel.ChannelAPI;
import works.chatterbox.chatterbox.api.message.MessageAPI;
import works.chatterbox.chatterbox.api.player.PlayerAPI;
import works.chatterbox.chatterbox.api.rythm.RythmAPI;
import works.chatterbox.chatterbox.api.title.TitleAPI;

public class ChatterboxAPI {

    private final Chatterbox chatterbox;
    private final PlayerAPI playerAPI;
    private final MessageAPI messageAPI;
    private final ChannelAPI channelAPI;
    private final RythmAPI rythmAPI;
    private final TitleAPI titleAPI;

    public ChatterboxAPI(@NotNull final Chatterbox chatterbox) {
        Preconditions.checkNotNull(chatterbox, "chatterbox was null");
        this.chatterbox = chatterbox;
        this.playerAPI = new PlayerAPI(this.chatterbox);
        this.messageAPI = new MessageAPI(this.chatterbox);
        this.channelAPI = new ChannelAPI(this.chatterbox);
        this.rythmAPI = new RythmAPI(this.chatterbox);
        this.titleAPI = new TitleAPI(this.chatterbox);
    }

    /**
     * Gets the Channel API, which is used for channel functions.
     *
     * @return ChannelAPI
     */
    @NotNull
    public ChannelAPI getChannelAPI() {
        return this.channelAPI;
    }

    /**
     * Gets the instance of Chatterbox that this API interfaces with.
     *
     * @return Chatterbox
     */
    @NotNull
    public Chatterbox getChatterbox() {
        return this.chatterbox;
    }

    /**
     * Gets the Message API, which is used to make and process messages.
     *
     * @return MessageAPI
     */
    @NotNull
    public MessageAPI getMessageAPI() {
        return this.messageAPI;
    }

    /**
     * Gets the Player API, which has methods pertaining to players and their various functions in Chatterbox.
     *
     * @return PlayerAPI
     */
    @NotNull
    public PlayerAPI getPlayerAPI() {
        return this.playerAPI;
    }

    /**
     * Gets the Rythm API, which is used to format templates and add custom variables to the formatting engine.
     *
     * @return RythmAPI
     */
    @NotNull
    public RythmAPI getRythmAPI() {
        return this.rythmAPI;
    }

    /**
     * Gets the Title API, which is used to manage player titles.
     *
     * @return TitleAPI
     */
    @NotNull
    public TitleAPI getTitleAPI() {
        return this.titleAPI;
    }
}
