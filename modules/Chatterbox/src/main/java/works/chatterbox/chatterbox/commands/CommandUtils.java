package works.chatterbox.chatterbox.commands;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class CommandUtils {

    private final static Joiner spaceJoiner = Joiner.on(' ');

    public static Joiner getSpaceJoiner() {
        return CommandUtils.spaceJoiner;
    }

    public static String joinFrom(@NotNull final String[] args, final int start) {
        Preconditions.checkArgument(start < args.length, "start was not a valid index");
        // I was going to use Arrays.copyOfRange, but I have the chance to do some good in the world
        final StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            sb.append(args[i]);
            if (i != args.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

}
