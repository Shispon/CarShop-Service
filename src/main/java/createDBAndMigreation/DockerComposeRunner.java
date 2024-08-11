package createDBAndMigreation;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class DockerComposeRunner {

    public static void runDockerCompose() {
        System.out.println("Current directory: " + System.getProperty("user.dir"));

        File directory = new File(System.getProperty("user.dir"));

        ProcessBuilder processBuilder = new ProcessBuilder("docker-compose", "up","-d");
        processBuilder.directory(directory);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to run docker-compose.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}