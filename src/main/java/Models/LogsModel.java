package Models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogsModel {
    private List<String>logsList = new ArrayList();

    public List getLogsList() {
        return logsList;
    }

    public void setLogsList(String logsList) {
        this.logsList.add(logsList);
    }
    public void inputFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Logs.txt"))) {
            for (String log : logsList) {
                writer.write(log);
                writer.newLine();
            }
            System.out.println("Logs have been written to Logs.txt successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
