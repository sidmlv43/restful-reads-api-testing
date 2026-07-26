package com.restfulReads.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class FileDataFactory {

    private static final String IMG_PATH = "images/";

    private FileDataFactory() {

    }


    public static List<File> getBookImages(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be more than equals to 1");
        }

        if (count > 5) {
            throw new IllegalArgumentException("API support maximum 5 photos");
        }

        List<File> images = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            File file = getImage(String.format("book_image_%s.jpg", i));
            images.add(file);
        }

        return images;
    }


    public static File getInvalidImageFile() {
        return getImage("invalid_image.pdf");
    }

    private static File getImage(String filename) {

        var resource =
                FileDataFactory.class
                        .getClassLoader()
                        .getResource(
                                IMG_PATH + filename
                        );

        if (resource == null) {
            throw new RuntimeException(
                    "Could not find image: "
                            + IMG_PATH + filename
            );
        }

        return new File(resource.getFile());
    }
}
