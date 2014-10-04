package cs440.assignment1.model;

/**
 * @author Shahab Shekari
 * @author Felicia Yau
 * @author Jeff Mandell
 */
public enum BlockState {

    BLOCKED("BLOCKED"),
    UNBLOCKED("UNBLOCKED"),
    UNVISITED("UNVISITED"),
    VISITED("VISITED"),
    START("START"),
    TARGET("TARGET"),
    AGENT("AGENT"),
    BREADCRUMB("BREADCRUMB"),

    //currently not used
    TOP("TOP"),
    BOTTOM("BOTOTM"),
    LEFT("LEFT"),
    RIGHT("RIGHT");


    private final String text;

    private BlockState(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
