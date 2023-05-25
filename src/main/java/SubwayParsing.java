import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubwayParsing {
    private final Document doc;
    private final List<Line> linesList = new ArrayList<>();
    private final List<Station> stationList = new ArrayList<>();
    public SubwayParsing(String url) throws IOException {
        this.doc = Jsoup.connect(url).get();
        setLines();
        setStations();
    }

    private void setLines() {
        Elements lines = doc.select("span.js-metro-line");
        for (Element line : lines) {
            String strLine = line.toString();
            String lineName = findName(strLine.substring(strLine.lastIndexOf('"')));
            int startLineNumber = strLine.indexOf("ln-") + 3;
            int endLineNumber = strLine.indexOf("data") - 2;
            String lineNumber = strLine.substring(startLineNumber, endLineNumber);
            linesList.add(new Line(lineName, lineNumber));
        }
    }

    private void setStations() {
        Elements stations = doc.select("p.single-station");
        int lineNum = -1;
        for (Element station : stations) {
            String strStation = station.toString();
            int startOrdinalNum = strStation.indexOf("\"num\">") + 6;
            int endOrdinalNum = strStation.indexOf(".");
            int ordinalNum = Integer.parseInt(strStation.substring(startOrdinalNum, endOrdinalNum));
            if (ordinalNum == 1) lineNum += 1;
            String numberLine = linesList.get(lineNum).getNumber();
            String name = findName(strStation.substring(strStation.indexOf("name")));
            boolean hasConnect = isConnected(strStation);
            stationList.add(new Station(name, numberLine, hasConnect));
        }
    }

    public List<Line> getLines() {
        return linesList;
    }

    public List<Station> getStations() {
        return stationList;
    }

    private static String findName(String text) {
        String regex = "[А-Яа-я-Ёё0-9 ]+";
        Matcher matcher = Pattern.compile(regex).matcher(text);
        String name = null;
        if (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            name = text.substring(start, end);

        }
        return name;
    }

    private static Boolean isConnected(String line){
        String regex = "переход на станцию";
        Matcher matcher = Pattern.compile(regex).matcher(line);
        return matcher.find();
    }
}
