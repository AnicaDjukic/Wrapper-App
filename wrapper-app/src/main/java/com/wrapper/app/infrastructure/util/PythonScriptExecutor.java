package com.wrapper.app.infrastructure.util;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class PythonScriptExecutor {

    public ExecutionResult executeScriptAndGetOutput(String jsonFilePath, String pythonScriptPath, String virtualEnvPython) throws IOException, InterruptedException {
        Process process = executeScript(jsonFilePath, pythonScriptPath, virtualEnvPython);
        String scriptOutput = readScriptOutput(process);
        int exitCode = getExitCode(process);
        return new ExecutionResult(scriptOutput, exitCode);
    }

    private Process executeScript(String jsonFilePath, String pythonScriptPath, String virtualEnvPython) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                virtualEnvPython, pythonScriptPath, jsonFilePath
        );
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    private String readScriptOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder outputBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            outputBuilder.append(line);
        }
        String jsonOutput = outputBuilder.toString();
        reader.close();
        return jsonOutput;
    }

    private int getExitCode(Process process) throws InterruptedException {
        return process.waitFor();
    }
}
