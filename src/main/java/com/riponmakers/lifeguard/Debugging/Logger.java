package com.riponmakers.lifeguard.Debugging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final File logFile;
    public Logger() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm");
        LocalDateTime now = LocalDateTime.now();

        String path = "/home/ubuntu/lifeGuard/logging/" + dtf.format(now) + ".txt";

        // Use relative path for Unix systems
        logFile = new File(path);

        try {
            logFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile.getAbsolutePath()));
            writer.write("Log: " + logFile.getName());

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logLine(String newLine) {
        logLines(new String[] { newLine });
    }

    public void logLines(String[] newLines) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile.getAbsolutePath(), true));

            for(var line : newLines) {
                writer.append(line + "\n");
            }
            writer.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
