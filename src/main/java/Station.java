public class Station {
    private final String numberLine;
    private final String name;
    boolean isConnection;

    public Station(String name, String numberLine, boolean hasConnect) {
        this.name = name;
        this.numberLine = numberLine;
        this.isConnection = hasConnect;
    }

    public String getNumberLine() {
        return numberLine;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Station{" +
                "numberLine='" + numberLine + '\'' +
                ", name='" + name + '\'' +
                ", hasConnect=" + isConnection +
                '}';
    }
}
