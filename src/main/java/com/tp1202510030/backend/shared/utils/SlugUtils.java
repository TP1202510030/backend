package com.tp1202510030.backend.shared.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SlugUtils {

    public static String toSlug(String... parts) {
        return Arrays.stream(parts)
                .map(s -> s.toLowerCase().replaceAll("\\s+", "_"))
                .collect(Collectors.joining("/"));
    }
}