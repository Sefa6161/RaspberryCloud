package com.Projekt.RaspberryCloud.util;

public class PathUtils {

    /**
     * Normalizes a path string:
     * - Converts null to empty string
     * - Removes leading slashes
     * - Replaces multiple slashes with a single one
     *
     * @param path the input path (may be null)
     * @return normalized path string
     */
    public static String normalize(String path) {
        if (path == null) {
            return "";
        }

        // Remove leading slashes and collapse multiple slashes
        return path.replaceFirst("^/+", "")
                .replaceAll("/{2,}", "/");
    }
}