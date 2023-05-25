public class Main {
    public static void main(String[] args) throws Exception {
        JsonFileCreator jsonFileCreator = new JsonFileCreator(
                "https://skillbox-java.github.io/", "/home/independent/data");
        jsonFileCreator.setListStationOnLine();
        jsonFileCreator.setLinesInfo();
    }
}
