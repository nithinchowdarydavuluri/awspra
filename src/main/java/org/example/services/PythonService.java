
package org.example.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class PythonService {

    public String generateS3Report(
            String bucketName,
            String region) throws Exception {

        // Load Python script from Maven resources
        InputStream scriptStream =
                getClass().getClassLoader()
                        .getResourceAsStream(
                                "python/s3_cloudwatch_report.py"
                        );

        if (scriptStream == null) {
            throw new IOException("Python script not found");
        }

        File scriptFile = File.createTempFile(
                "s3-cloudwatch-", ".py"
        );

        try (scriptStream) {
            Files.copy(
                    scriptStream,
                    scriptFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
        }

        // Python executable can be configured using environment variable
        String pythonExecutable =
                System.getenv().getOrDefault(
                        "PYTHON_EXECUTABLE",
                        "python3"
                );

        ProcessBuilder builder = new ProcessBuilder(
                pythonExecutable,
                scriptFile.getAbsolutePath(),
                bucketName,
                region
        );

        builder.redirectErrorStream(true);

        Process process = builder.start();

        StringBuilder output = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     process.getInputStream()
                             ))) {

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();

        scriptFile.delete();

        if (exitCode != 0) {
            throw new RuntimeException(
                    "Python execution failed:\n" + output
            );
        }

        return output.toString();
    }
}
