package works.chatterbox.chatterbox.titles;

public class Prefix extends Title {

    private final String title;

    public Prefix(final String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
    }
}
