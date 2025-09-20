package com.example.demo.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "logging.level.root=OFF",
                "logging.level.org.springframework=OFF",
                "logging.level.org.hibernate=OFF",
                "spring.main.banner-mode=off"
        }
)
@ActiveProfiles("test")
public class CucumberTest {
}