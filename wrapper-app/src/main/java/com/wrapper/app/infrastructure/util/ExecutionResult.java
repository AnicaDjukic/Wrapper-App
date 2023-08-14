package com.wrapper.app.infrastructure.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExecutionResult {

    private String scriptOutput;
    private int exitCode;
}
