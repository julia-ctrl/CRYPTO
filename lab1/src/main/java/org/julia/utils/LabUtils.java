package org.julia.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LabUtils {
    public static String resourceToString(String path) throws URISyntaxException, IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(LabUtils.class.getResource(path).toURI()));
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
