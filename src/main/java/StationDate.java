import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StationDate {
    private final String name;
    private final String date;
    public StationDate(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public LocalDate getLocalDate(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, dateTimeFormatter);
    }

    @Override
    public String toString() {
        return "StationDate{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
