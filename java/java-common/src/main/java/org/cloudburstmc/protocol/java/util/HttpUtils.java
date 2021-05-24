package org.cloudburstmc.protocol.java.util;

import com.google.gson.JsonElement;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static org.cloudburstmc.protocol.java.Java.GSON;

@UtilityClass
public class HttpUtils {

    public CompletableFuture<JsonElement> createPostRequest(URI uri, String data, String type) {
        return createPostRequest(uri, data, type, 15_000);
    }

    public CompletableFuture<JsonElement> createPostRequest(URI uri, String data, String type, int timeout) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] buffer = data.getBytes(StandardCharsets.UTF_8);
                HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection(Proxy.NO_PROXY);
                connection.setRequestProperty("Content-Type", type + "; charset=utf-8");
                connection.setRequestProperty("Content-Length", String.valueOf(buffer.length));
                connection.setUseCaches(false);
                connection.setDoOutput(true);
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);

                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(buffer);
                }
                try (InputStream inputStream = connection.getResponseCode() != HttpURLConnection.HTTP_OK ? connection.getErrorStream() : connection.getInputStream()) {
                    return inputStream == null ? null : GSON.fromJson(new InputStreamReader(inputStream), JsonElement.class);
                }
            } catch (IOException ex) {
                throw new RuntimeException("Failed to create post request", ex);
            }
        });
    }

    public CompletableFuture<JsonElement> createGetRequest(URI uri) {
        return createGetRequest(uri, 15_000);
    }

    public CompletableFuture<JsonElement> createGetRequest(URI uri, int timeout) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection(Proxy.NO_PROXY);
                connection.setUseCaches(false);
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                try (InputStream inputStream = connection.getResponseCode() != HttpURLConnection.HTTP_OK ? connection.getErrorStream() : connection.getInputStream()) {
                    return inputStream == null ? null : GSON.fromJson(new InputStreamReader(inputStream), JsonElement.class);
                }
            } catch (IOException ex) {
                throw new RuntimeException("Failed to create get request", ex);
            }
        });
    }
}
