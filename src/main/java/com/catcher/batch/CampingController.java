package com.catcher.batch;

import com.catcher.batch.core.dto.CampingApiResponse;
import com.catcher.batch.service.CampingApiService;
import com.catcher.batch.service.RestApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/camping")
public class CampingController {
    private final CampingApiService campingApiService;
    private final RestApiService restApiService;

    @GetMapping("/webclient")
    public Mono<ResponseEntity<CampingApiResponse>> testWebClient() {
        return campingApiService.getOpenApi()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/resttemplate")
    public ResponseEntity<CampingApiResponse> testRestTemplate() {
        CampingApiResponse response = restApiService.getOpenApi();
        return ResponseEntity.ok(response);
    }
}
