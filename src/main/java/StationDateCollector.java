import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class StationDateCollector {
    private final List<StationDate> stationDateList = new ArrayList<>();
    private final TreeMap<String, StationDate> stationDateTreeMap = new TreeMap<>();
    public StationDateCollector() {
    }

    private void readFile(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        for (String line : lines) {
            String[] stationList = line.split(",");
            if (!(stationList.length == 2)) {
                System.out.println("Строка \"" + Arrays.toString(stationList) + "\" имеет неверный формат");
                continue;
            }
            stationDateList.add(new StationDate(stationList[0], stationList[1]));
        }
        //чтобы избежать дублирования строк пропускаю его через TreeMap и перевожу обратно в List
        for (StationDate stationDate : stationDateList){
            stationDateTreeMap.put(stationDate.toString(), stationDate);
        }
        stationDateList.clear();
        for (Map.Entry<String, StationDate> entry : stationDateTreeMap.entrySet()){
            stationDateList.add(entry.getValue());
        }
    }

    public void setStationDateList(String path) {
        try {
            readFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StationDate> getStationDateList() {
        stationDateList.sort(Comparator.comparing(StationDate::getName)
                .thenComparing(StationDate::getLocalDate));
        return stationDateList;
    }
}