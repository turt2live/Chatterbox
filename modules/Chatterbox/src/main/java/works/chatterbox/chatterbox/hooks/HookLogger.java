package works.chatterbox.chatterbox.hooks;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class HookLogger extends Logger {

    private String hookName;

    public HookLogger(final ChatterboxHook context) {
        super(context.getClass().getCanonicalName(), null);
        this.hookName = context.getDescriptor().getName();
        this.setParent(context.getChatterbox().getLogger());
        this.setLevel(Level.ALL);
    }

    public void log(@NotNull final LogRecord logRecord) {
        Preconditions.checkNotNull(logRecord, "logRecord was null");
        logRecord.setMessage(this.hookName + logRecord.getMessage());
        super.log(logRecord);
    }
}
