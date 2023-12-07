package com.catcher.batch.resource;

import com.catcher.batch.core.service.ApiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/festival")
public class FestivalController {
    private final ApiService apiService;

    FestivalController(@Qualifier("festival") ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/batch")
    public ResponseEntity<Void> startBatch() {
        apiService.getData();

        return ResponseEntity.ok(null);
    }
}
