package vmgo.store.criteria;

public enum ChallengeVideoSearchKeys {
    CHALLENGE("challenge");

    private final String value;

    ChallengeVideoSearchKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
