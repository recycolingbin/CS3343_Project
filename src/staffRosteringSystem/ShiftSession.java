package staffRosteringSystem;

public enum ShiftSession {
    MORNING("06:00", "14:00", 1),
    AFTERNOON("14:00", "22:00", 2),
    NIGHT("22:00", "06:00", 3);

    private final String startTime;
    private final String endTime;
    private final int order;

    ShiftSession(String startTime, String endTime, int order) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.order = order;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }


    // For file serialization
    @Override
    public String toString() {
        return name();
    }

    // Parse string to enum, handle invalid cases
    public static ShiftSession fromString(String session) {
        if (session == null || session.trim().isEmpty()) {
            return null;
        }
        try {
            return ShiftSession.valueOf(session.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}