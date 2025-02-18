package com.escanor1986.tennis.web;

import com.escanor1986.tennis.models.TestData;
import com.escanor1986.tennis.service.TestDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/testdata")
public class TestDataController {
    private final TestDataService testDataService;

    public TestDataController(TestDataService testDataService) {
        this.testDataService = testDataService;
    }

    //! ✅ Listing des données de test
    @Operation(summary = "Finds test data", description = "Retrieves a list of test data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Test data list", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = TestData.class))) })
    })

    @GetMapping
    public List<TestData> getAllTestData() {
        return testDataService.getAllTestData();
    }
}
