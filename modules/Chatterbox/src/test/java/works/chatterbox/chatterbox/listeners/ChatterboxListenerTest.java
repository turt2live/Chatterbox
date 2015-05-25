package works.chatterbox.chatterbox.listeners;

import com.google.common.collect.Lists;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;
import org.bukkit.event.player.PlayerJoinEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import works.chatterbox.chatterbox.Chatterbox;
import works.chatterbox.chatterbox.TestHelpers;
import works.chatterbox.chatterbox.channels.Channel;
import works.chatterbox.chatterbox.wrappers.CPlayer;
import works.chatterbox.chatterbox.wrappers.UUIDCPlayer;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

@Ignore
public class ChatterboxListenerTest {

    private ChatterboxListener listener;
    private Chatterbox chatterbox;
    private UUIDCPlayer cp;

    @Before
    public void setUp() throws Exception {
        final UUID uuid = UUID.fromString("2bb57eb9-fb07-4d08-beb4-971a577db9e2");

        this.chatterbox = TestHelpers.makeChatterbox();
        final ConfigurationNode node = SimpleConfigurationNode.root();
        node.getNode("test").getNode(uuid.toString()).setValue(System.currentTimeMillis());
        when(this.chatterbox.getAPI().getChannelAPI().getMemberships()).thenReturn(node);
        when(this.chatterbox.getAPI().getChannelAPI().getAllChannelNames()).thenReturn(Lists.newArrayList("test"));
        final Channel channel = TestHelpers.makeChannel("test", "t");
        doNothing().when(channel).addMember(any(CPlayer.class));
        when(this.chatterbox.getAPI().getChannelAPI().getChannelByName(eq("test"))).thenReturn(channel);

        this.listener = new ChatterboxListener(this.chatterbox);

        this.cp = TestHelpers.makeUUIDCPlayer();
        doNothing().when(this.cp).joinChannel(any(Channel.class));
        when(this.cp.getUUID()).thenReturn(uuid);
        //noinspection ConstantConditions – will not return null
        when(this.cp.getPlayer().getUniqueId()).thenReturn(uuid);
    }

    @After
    public void tearDown() throws Exception {
        this.listener = null;
        this.chatterbox = null;
        this.cp = null;
    }

    @Test
    public void testJoinDefaultChannel() throws Exception {

    }

    @Test
    public void testJoinPermanentChannels() throws Exception {

    }

    @Test
    public void testJoinPreviousChannels() throws Exception {
        final PlayerJoinEvent event = new PlayerJoinEvent(this.cp.getPlayer(), "");
        // Should be in no channels
        assertSame(0, this.cp.getChannels().size());
        // Fire event
        this.listener.joinPreviousChannels(event);
        // Should now be in one channel
        assertSame(1, this.cp.getChannels().size());
        // Get the joined channel
        final Channel joined = this.cp.getChannels().iterator().next();
        // Verify that the addMember method was called on the channel
        verify(joined, times(1)).addMember(this.cp);
        // Make sure that the correct channel was joined
        assertEquals("test", joined.getName());
    }

    @Test
    public void testOnChat() throws Exception {

    }
}
