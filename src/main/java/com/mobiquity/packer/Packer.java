package com.mobiquity.packer;

import com.mobiquity.entity.Item;
import com.mobiquity.exception.APIException;
import com.mobiquity.service.ParkerService;
import com.mobiquity.service.ParkerServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Packer {

    public static void main(String[] args) throws APIException {
         System.out.println(pack(Packer.class.getResource("/testcases.txt").getPath()));
    }

    /**
     * @param filePath - Input file testCases
     * @return - Output string (items’ index numbers are separated by comma)
     */
    public static String pack(String filePath) throws APIException {
        String[] content = null;
        String result = "";
        Path path = Paths.get(filePath);

        ParkerService parkerService = new ParkerServiceImpl();

        try {
            result = Files.readString(path).lines()
                    .map(line -> {
                        AbstractMap.SimpleImmutableEntry<Integer, Item[]> entry = readItems(line);
                        return parkerService.pack(entry.getValue(), entry.getKey());
                    })
                    .collect(Collectors.joining("\n"));
            ;
        } catch (IOException e) {
            throw new APIException(e.getMessage());
        }

        return result;
    }

    /**
     * Clean up testcases
     * @param line
     * @return
     */
    private static AbstractMap.SimpleImmutableEntry<Integer, Item[]> readItems(String line) {
        int capacity = 0;
        Item[] objects = new Item[0];
        String[] lineArr = line.split(":");

        // set the capacity value
        capacity = Integer.valueOf(lineArr[0].trim());

        // set Items array
        var itemStream = Arrays.stream(lineArr[1].trim().split(" "))
            .map(i -> i.replaceAll("[(|)]", "")) // strip parenthesis
            .map(i -> i.replace("€", "")); // strip currency sign

        objects = itemStream.map(i -> {
                String[] entry = i.split(",");
                return new Item(Integer.valueOf(entry[2]), Float.valueOf(entry[1]), Integer.valueOf(entry[0]) - 1);
            }).toArray(Item[]::new);

        return new AbstractMap.SimpleImmutableEntry<>(capacity, objects);
    }

}
