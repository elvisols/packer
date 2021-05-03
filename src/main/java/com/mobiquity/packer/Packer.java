package com.mobiquity.packer;

import com.mobiquity.entity.Item;
import com.mobiquity.exception.APIException;
import com.mobiquity.service.ParkerService;
import com.mobiquity.service.ParkerServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.mobiquity.util.Helper.wrap;

public class Packer {

    private static int capacity;
    private final static int MAX_WEIGHT = 100;
    private final static int MAX_COST = 100;
    private final static int MAX_ITEM = 100;

    public static void main(String[] args) throws APIException {
         System.out.println(pack(Packer.class.getResource("/testcases.txt").getPath()));
    }

    /**
     * @param filePath - Input file testCases
     * @return - Output string (items’ index numbers are separated by comma)
     */
    public static String pack(String filePath) throws APIException {
        String result = "";
        Path path = Paths.get(filePath);

        ParkerService parkerService = new ParkerServiceImpl();

        try {
            result = Files.readString(path).lines()
                    .map(wrap(line -> {
                        AbstractMap.SimpleImmutableEntry<Integer, Item[]> entry = readItems(line);

                        return parkerService.pack(entry.getValue(), entry.getKey());
                    }))
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new APIException(e.getCause().getMessage());
        }

        return result;
    }

    /**
     * Clean up testcases
     * @param line
     * @return
     */
    private static AbstractMap.SimpleImmutableEntry<Integer, Item[]> readItems(String line) throws APIException {
        capacity = 0;
        Item[] items;
        String[] lineArr = line.split(":");

        // set the capacity value
        capacity = Integer.valueOf(lineArr[0].trim());

        // set Items array
        var itemStream = Arrays.stream(lineArr[1].trim().split(" "))
            .map(i -> i.replaceAll("[(|)]", "")) // strip parenthesis
            .map(i -> i.replace("€", "")); // strip currency sign

        items = itemStream.map(wrap(i -> {
                String[] entry = i.split(",");

                // validate item value and weight on the fly
                if(Float.valueOf(entry[1]) > MAX_WEIGHT || Float.valueOf(entry[2]) > MAX_COST)
                    throw new APIException(String.format("Error: Max weight/cost per item exceeded. Acceptable values must be less than or equal to %d/%d.", MAX_WEIGHT, MAX_COST));

                return new Item(Integer.valueOf(entry[2]), Float.valueOf(entry[1]), Integer.valueOf(entry[0]) - 1);
            })).toArray(Item[]::new);

        AbstractMap.SimpleImmutableEntry<Integer, Item[]> transformedEntry = new AbstractMap.SimpleImmutableEntry<>(capacity, items);

        // sanitize input data according to additional constraints.
        sanitizeInputs(transformedEntry);

        return transformedEntry;
    }

    private static void sanitizeInputs(AbstractMap.SimpleImmutableEntry<Integer, Item[]> entry) throws APIException {
        // validate Max weight
        if(entry.getKey() > MAX_WEIGHT)
            throw new APIException(String.format("Error: Max package weight too large. Acceptable value must be less than or equal to %d.", MAX_WEIGHT));

        // validate Max items to pick from
        if(entry.getValue().length > MAX_ITEM)
            throw new APIException(String.format("Error: Max items to picked from is exceeded. Acceptable value must be less than or equal to %d.", MAX_ITEM));
    }

}
