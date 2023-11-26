package cn.jxust.supermarket.server;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

public record ApiResponse(boolean success, Object data, RuntimeException exception) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Contract("_ -> new")
    public static @NotNull ApiResponse errorResponse(RuntimeException e) {
        return new ApiResponse(false, null, e);
    }
}
