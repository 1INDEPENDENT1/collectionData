public class StationDepth {
    private final String name;
    private final String depth;

    public StationDepth(String name, String depth) {
        this.name = name;
        this.depth = depth;
    }

    public String getName() {
        return name;
    }

    public String getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        return "StationDepth{" +
                "name='" + name + '\'' +
                ", depth='" + depth + '\'' +
                '}';
    }
}
