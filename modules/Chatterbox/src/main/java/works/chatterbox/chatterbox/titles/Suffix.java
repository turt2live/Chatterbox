package works.chatterbox.chatterbox.titles;

public class Suffix extends Title {

    private final String title;

    public Suffix(final String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
    }
}
