package credit.core.model;

import java.util.Arrays;

public enum Level {
    NONE(10, 100), DOMAIN_VERIFIED(10, 100), QUALITY_DOMAIN_VERIFIED(20, 1000), EMAIL_VERIFIED_BASIC(12,
                                                                                                     500),
    EMAIL_VERIFIED_CORPORATE(
            33, 5000), INDIVIDUAL_VERIFIED(75, Integer.MAX_VALUE);

    private Integer value;

    private Integer maxAccountScore;

    Level(final Integer value, Integer maxAccountScore) {
        this.value = value;
        this.maxAccountScore = maxAccountScore;
    }

    public static Level getLevel(Integer level) {
        return Arrays.stream(values())
                .filter(e -> e.getValue()
                        .equals(level))
                .findFirst()
                .orElse(Level.NONE);
    }

    public Integer getValue() {
        return value;
    }

    public Integer getMaxAccountScore() {
        return maxAccountScore;
    }
}
