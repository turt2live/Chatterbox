package works.chatterbox.chatterbox.api;

import org.jetbrains.annotations.NotNull;
import works.chatterbox.chatterbox.Chatterbox;

public interface ChatterboxAPI {

    /**
     * Gets the Channel API, which is used for channel functions.
     *
     * @return ChannelAPI
     */
    @NotNull
    ChannelAPI getChannelAPI();

    /**
     * Gets the instance of Chatterbox that this API interfaces with.
     *
     * @return Chatterbox
     */
    @NotNull
    Chatterbox getChatterbox();

    /**
     * Gets the Message API, which is used to make and process messages.
     *
     * @return MessageAPI
     */
    @NotNull
    MessageAPI getMessageAPI();

    /**
     * Gets the Messaging API, which is used in processing private messages.
     *
     * @return MessagingAPI
     */
    @NotNull
    MessagingAPI getMessagingAPI();

    /**
     * Gets the Player API, which has methods pertaining to players and their various functions in Chatterbox.
     *
     * @return PlayerAPI
     */
    @NotNull
    PlayerAPI getPlayerAPI();

    /**
     * Gets the Rythm API, which is used to format templates and add custom variables to the formatting engine.
     *
     * @return RythmAPI
     */
    @NotNull
    RythmAPI getRythmAPI();

    /**
     * Gets the Title API, which is used to manage player titles.
     *
     * @return TitleAPI
     */
    @NotNull
    TitleAPI getTitleAPI();
}
