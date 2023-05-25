import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileFinder {
    private final List<String> jsonFileList = new ArrayList<>();
    private final List<String> csvFileList = new ArrayList<>();

    public FileFinder(String path) {
        File folder = new File(path);
        fileFind(folder);
    }

    private void fileFind(File folder) {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                fileFind(file);
            } else {
                String jsonRegex = "(.json)";
                Matcher jsonMatcher = Pattern.compile(jsonRegex).matcher(file.getName());
                String csvRegex = "(.csv)";
                Matcher csvMatcher = Pattern.compile(csvRegex).matcher(file.getName());
                if (jsonMatcher.find()) {
                    jsonFileList.add(file.getAbsolutePath());
                }else if(csvMatcher.find()){
                    csvFileList.add(file.getAbsolutePath());
                }
            }
        }
    }

    public List<String> getJsonFileList() {
        return jsonFileList;
    }

    public List<String> getCsvFileList() {
        return csvFileList;
    }
}
