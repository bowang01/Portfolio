package com.portfolio.domain;

public enum InterviewCategory {
    TECHNICAL,
    BEHAVIORAL;

    public String displayName() {
        return switch (this) {
            case TECHNICAL -> "Technical";
            case BEHAVIORAL -> "Behavioral";
        };
    }
}
