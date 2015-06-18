package works.chatterbox.chatterbox.commands;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class CommandUtils {

    private final static Joiner spaceJoiner = Joiner.on(' ');

    @NotNull
    public static Joiner getSpaceJoiner() {
        return CommandUtils.spaceJoiner;
    }

    @NotNull
    public static String joinFrom(@NotNull final String[] args, final int start, @NotNull final String joinOn) {
        Preconditions.checkNotNull(args, "args was null");
        Preconditions.checkArgument(start < args.length && start >= 0, "start was not a valid index");
        Preconditions.checkNotNull(joinOn, "joinOn was null");
        // I was going to use Arrays.copyOfRange, but I have the chance to do some good in the world
        final StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            sb.append(args[i]);
            if (i != args.length - 1) {
                sb.append(joinOn);
            }
        }
        return sb.toString();
    }

    @NotNull
    public static String joinFrom(@NotNull final String[] args, final int start) {
        return CommandUtils.joinFrom(args, start, " ");
    }

    @NotNull
    public static String joinUntil(@NotNull final String[] args, final int end, final String joinOn) {
        Preconditions.checkNotNull(args, "args was null");
        Preconditions.checkArgument(end <= args.length && end >= 0, "end was not a valid index");
        Preconditions.checkNotNull(joinOn, "joinOn was null");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < end; i++) {
            sb.append(args[i]);
            if (i != end - 1) {
                sb.append(joinOn);
            }
        }
        return sb.toString();
    }

    @NotNull
    public static String joinUntil(@NotNull final String[] args, final int end) {
        return CommandUtils.joinUntil(args, end, " ");
    }

}
