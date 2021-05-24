package org.cloudburstmc.protocol.java.data.auth.mojang;

import lombok.Value;

@Value
public class ErrorResponse {
    String error;
    String errorMessage;
    String cause;
}
