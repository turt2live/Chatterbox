package works.chatterbox.chatterbox;

import works.chatterbox.chatterbox.api.ChatterboxAPI;
import works.chatterbox.chatterbox.api.channel.ChannelAPI;
import works.chatterbox.chatterbox.api.player.PlayerAPI;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.wrappers.CPlayer;
import works.chatterbox.chatterbox.wrappers.UUIDCPlayer;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public final class TestHelpers {

    public static CPlayer makeCPlayer() {
        return mock(CPlayer.class);
    }

    public static Channel makeChannel(final String name, final String tag) {
        final Channel channel = mock(Channel.class);
        when(channel.getName()).thenReturn(name);
        when(channel.getTag()).thenReturn(tag);
        return channel;
    }

    public static Chatterbox makeChatterbox() {
        final Chatterbox chatterbox = mock(Chatterbox.class);
        final ChatterboxAPI chatterboxAPI = TestHelpers.makeChatterboxAPI();
        when(chatterboxAPI.getChatterbox()).thenReturn(chatterbox);
        when(chatterbox.getAPI()).thenReturn(chatterboxAPI);
        return chatterbox;
    }

    public static ChatterboxAPI makeChatterboxAPI() {
        final ChatterboxAPI chatterboxAPI = mock(ChatterboxAPI.class);
        final ChannelAPI channelAPI = mock(ChannelAPI.class);
        when(chatterboxAPI.getChannelAPI()).thenReturn(channelAPI);
        final PlayerAPI playerAPI = mock(PlayerAPI.class);
        when(chatterboxAPI.getPlayerAPI()).thenReturn(playerAPI);
        return chatterboxAPI;
    }

    public static UUIDCPlayer makeUUIDCPlayer() {
        return mock(UUIDCPlayer.class);
    }

}
