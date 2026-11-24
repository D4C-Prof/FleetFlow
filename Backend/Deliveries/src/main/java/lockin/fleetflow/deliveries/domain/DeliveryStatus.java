package lockin.fleetflow.deliveries.domain;

public enum DeliveryStatus {
    PENDING,
    IN_PROGRESS,
    DELIVERED,
    FAILED,
    RESCHEDULED;

    /** Statuses a delivery may move to from this one (forward only). */
    public boolean canMoveTo(DeliveryStatus next) {
        return switch (this) {
            case PENDING -> next == IN_PROGRESS;
            case IN_PROGRESS -> next == DELIVERED || next == FAILED;
            case FAILED -> next == RESCHEDULED;
            case DELIVERED, RESCHEDULED -> false;
        };
    }

    /** A finished delivery no longer blocks the route from completing. */
    public boolean isFinished() {
        return this == DELIVERED || this == FAILED || this == RESCHEDULED;
    }
}
