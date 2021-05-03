package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PackerTest {

    @Test
    void get_successful_result_test() throws APIException {

        assertThat(Packer.pack(Packer.class.getResource("/testcases.txt").getPath())).isEqualTo("4\n-\n2,7\n8,9");
    }

    @Test
    void excess_max_weight_exception_test() throws APIException {
        String sampleFileContent = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)\n" +
                "8 : (1,15.3,€34)\n" +
                "175 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)\n" +
                "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)";

        try (MockedStatic<Files> file = Mockito.mockStatic(Files.class)) {
            file.when(() -> Files.readString(Mockito.any(Path.class)))
                .thenReturn(sampleFileContent);

            assertThrows(APIException.class, () -> {
                Packer.pack("/filename_in_resource_folder.txt");
            });
        }
    }

    @Test
    void excess_item_weight_exception_test() throws APIException {
        String sampleFileContent = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)\n" +
                "8 : (1,150.3,€34)\n" +
                "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)\n" +
                "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)";

        try (MockedStatic<Files> file = Mockito.mockStatic(Files.class)) {
            file.when(() -> Files.readString(Mockito.any(Path.class)))
                .thenReturn(sampleFileContent);

            assertThrows(APIException.class, () -> {
                Packer.pack("/filename_in_resource_folder.txt");
            });
        }
    }

    @Test
    void excess_item_cost_exception_test() throws APIException {
        String sampleFileContent = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)\n" +
                "8 : (1,50.3,€134)\n" +
                "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)\n" +
                "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)";

        try (MockedStatic<Files> file = Mockito.mockStatic(Files.class)) {
            file.when(() -> Files.readString(Mockito.any(Path.class)))
                .thenReturn(sampleFileContent);

            assertThrows(APIException.class, () -> {
                Packer.pack("/filename_in_resource_folder.txt");
            });
        }
    }

    @Test
    void excess_item_to_select_exception_test() throws APIException {
        String sampleFileContent = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)\n" +
                "8 : (1,50.3,€34)\n" +
                "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78) (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)\n" +
                "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)";

        try (MockedStatic<Files> file = Mockito.mockStatic(Files.class)) {
            file.when(() -> Files.readString(Mockito.any(Path.class)))
                .thenReturn(sampleFileContent);

            assertThrows(APIException.class, () -> {
                Packer.pack("/filename_in_resource_folder.txt");
            });
        }
    }

    @Test
    void index_out_of_bound_exception_test() throws APIException {
        String sampleFileContent = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)\n" +
                "8 : (1,€34)\n" +
                "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)\n" +
                "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)";

        try (MockedStatic<Files> file = Mockito.mockStatic(Files.class)) {
            file.when(() -> Files.readString(Mockito.any(Path.class)))
                .thenReturn(sampleFileContent);

            assertThrows(APIException.class, () -> {
                Packer.pack("/filename_in_resource_folder.txt");
            });
        }
    }

}
