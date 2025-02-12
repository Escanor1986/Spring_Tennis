package com.escanor1986.tennis.service;

import com.escanor1986.tennis.models.TestData;
import com.escanor1986.tennis.repository.TestDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestDataService {
    private final TestDataRepository testDataRepository;

    public TestDataService(TestDataRepository testDataRepository) {
        this.testDataRepository = testDataRepository;
    }

    public List<TestData> getAllTestData() {
        return testDataRepository.findAll();
    }
}
