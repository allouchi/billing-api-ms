package com.sbatec.authentserver.exceptions;

public record ToolResponse<T>(
        boolean success,
        T data,
        ToolError error
) {
}

record ToolError(
        String code,
        String message
) {
}
