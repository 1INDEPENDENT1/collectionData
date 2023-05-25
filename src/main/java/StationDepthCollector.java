import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StationDepthCollector {
    private final List<StationDepth> stationList = new ArrayList<>();
    private final TreeMap<String, StationDepth> stationDepthTreeMap = new TreeMap<>();
    public StationDepthCollector(){
    }

    private void setStations(String path) throws IOException {
        String jsonFile = Files.readString(Paths.get(path));
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonData = (ArrayNode) objectMapper.readTree(jsonFile);
        for(JsonNode station : jsonData){
            ObjectNode stationNode = (ObjectNode) station;
            stationList.add(new StationDepth(stationNode.get("station_name").toString().
                    substring(1, stationNode.get("station_name").toString().length() - 1),
                    stationNode.get("depth").toString().
                    substring(1, stationNode.get("depth").toString().length() - 1)));
        }
        //чтобы избежать дублирования строк пропускаю его через TreeMap и перевожу обратно в List
        for(StationDepth stationDepth : stationList){
            stationDepthTreeMap.put(stationDepth.toString(), stationDepth);
        }

        stationList.clear();
        for(Map.Entry<String, StationDepth> entry : stationDepthTreeMap.entrySet()){
            stationList.add(entry.getValue());
        }
    }

    public void setStationDepth(String path){
        try {
            setStations(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StationDepth> getStationList(){
        stationList.sort(Comparator.comparing(StationDepth::getName).thenComparing(StationDepth::getDepth));
        return stationList;
    }
}
