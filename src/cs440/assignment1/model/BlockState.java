package cs440.assignment1.model;

/**
 * Created by Shahab Shekari on 9/24/14.
 */
public enum BlockState {

    BLOCKED("BLOCKED"),
    UNBLOCKED("UNBLOCKED"),
    UNVISITED("UNVISITED"),
    VISITED("VISITED"),
    START("START"),
    TARGET("TARGET"),
    AGENT("AGENT"),
    BREADCRUMB("BREADCRUMB");


    private final String text;

    private BlockState(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
