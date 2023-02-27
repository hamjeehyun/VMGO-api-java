package vmgo.store.criteria;

public enum ChallengeSearchKeys {
    FEATURED("featured"),
    ACTIVE("active"),
    TAGS("tag");

    private final String value;

    ChallengeSearchKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
