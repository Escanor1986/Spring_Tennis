package com.escanor1986.tennis.web;

import com.escanor1986.tennis.models.TestData;
import com.escanor1986.tennis.service.TestDataService;
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

    @GetMapping
    public List<TestData> getAllTestData() {
        return testDataService.getAllTestData();
    }
}
