package com.mobiquity.packer;

import com.mobiquity.exception.APIException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Packer {
    public static void main(String[] args) throws IOException {

        System.out.println(pack(Packer.class.getResource("/testcases.txt").getPath()));
    }

    public static String pack(String filePath) throws APIException {
        String[] content = null;
        Path path = Paths.get(filePath);

        try {
            content = Files.readString(path).split("\\n"); // Default = StandardCharsets.UTF_8
        } catch (IOException e) {
            throw new APIException(e.getMessage());
        }

        return content[1];
    }
}
