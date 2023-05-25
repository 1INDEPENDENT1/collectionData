import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public class JsonFileCreator {
    private static ObjectMapper mapper = new ObjectMapper();
    //Находит пути файлов двух форматов и распределяет их по спискам
    private final FileFinder fileFinder;
    //получает файлы формата .csv и обрабатывает данные, после заносит их в список
    private final StationDateCollector stationDateCollector = new StationDateCollector();
    //получает файлы формата .json и обрабатывает данные, после заносит их в список
    private final StationDepthCollector stationDepthCollector = new StationDepthCollector();
    //извлекает даные с сайта, распределяет на два списка(данные о линиях и станциях)
    private final SubwayParsing subwayParsing;
    private final File linesFile = new File("lines.json");
    private final File stationsFile = new File("stations.json");

    public JsonFileCreator(String url, String path) throws IOException {
        fileFinder = new FileFinder(path);
        subwayParsing =  new SubwayParsing(url);
        setCsvList();
        setStationDepthList();
    }
    //формирует первый файл lines.json
    public void setListStationOnLine() throws IOException {
        ObjectNode lineNumberNode = mapper.createObjectNode();
        String numberStation = null;
        ArrayNode listStationsNode = mapper.createArrayNode();
        for (Station station : subwayParsing.getStations()) {
            if (Objects.equals(numberStation, station.getNumberLine())) {
                listStationsNode.add(station.getName());
            } else if (numberStation != null) {
                lineNumberNode.put(numberStation, mapper.createArrayNode().addAll(listStationsNode));
                numberStation = station.getNumberLine();
                listStationsNode.removeAll();
            } else {
                numberStation = station.getNumberLine();
                listStationsNode.add(station.getName());
            }
        }
        ArrayNode listLineNode = mapper.createArrayNode();
        for (Line line : subwayParsing.getLines()) {
            listLineNode.add(mapper.createObjectNode().
                    put("Number", line.getNumber()).
                    put("name", line.getName()));
        }
        ObjectNode stationsNode = mapper.createObjectNode();
        stationsNode.put("Stations", lineNumberNode);
        stationsNode.put("Lines", listLineNode);
        mapper.writerWithDefaultPrettyPrinter().writeValue(linesFile, stationsNode);
    }
    //формирует второй файл stations.json
    public void setLinesInfo() throws Exception{
        ArrayNode stationInfoList = mapper.createArrayNode();

        for(Station station : subwayParsing.getStations()){
            stationInfoList.add(mapper.createObjectNode().
                    put("name", station.getName()).
                    put("line", convertNumToName(station)).
                    put("isConnection", station.isConnection).
                    put("date", setStationDate(station)).
                    put("depth", setStationDepth(station)));
        }
        ObjectNode stationsInfoNode = mapper.createObjectNode();
        stationsInfoNode.put("stations", stationInfoList);
        mapper.writerWithDefaultPrettyPrinter().writeValue(stationsFile, stationsInfoNode);
    }
    //соотносит данные класса Stations и StationDate по имени
    private String setStationDate(Station station){
        String stationDate = null;
        for(StationDate date : stationDateCollector.getStationDateList()){
            if(changeRuChar(station.getName()).equals(changeRuChar(date.getName()))){
                stationDate = date.getDate();
                stationDateCollector.getStationDateList().remove(date);
                break;
            }
        }
        return stationDate;
    }
    //соотносит данные класса Stations и StationDepth по имени
    private String setStationDepth(Station station){
        String stationDepth = null;
        for(StationDepth depth : stationDepthCollector.getStationList()){
            if(changeRuChar(station.getName()).equals(changeRuChar(depth.getName()))){
                stationDepth = depth.getDepth();
                stationDepthCollector.getStationList().remove(depth);
                break;
            }
        }
        return stationDepth;
    }
    //берет список путей json файлов
    private List<StationDepth> setStationDepthList(){
     for(String path : fileFinder.getJsonFileList()){
         stationDepthCollector.setStationDepth(path);
     }
     return stationDepthCollector.getStationList();
    }
    //сопоставляет назвния линий с их порядковым номером
    private String convertNumToName(Station station){
        String stationLine = null;
        for (Line line : subwayParsing.getLines()){
            if(line.getNumber() == station.getNumberLine()){
                stationLine = line.getName();
                break;
            }
        }
        return stationLine;
    }
    //берет список путей csv файлов
    private void setCsvList(){
        for(String path : fileFinder.getCsvFileList()){
            stationDateCollector.setStationDateList(path);
        }
    }
    //приравнивает символ "ё" к "е" и убирает заглавные буквы
    private String changeRuChar(String string){
       string = string.toLowerCase().replace('ё', 'е');
      return string;
    }
}