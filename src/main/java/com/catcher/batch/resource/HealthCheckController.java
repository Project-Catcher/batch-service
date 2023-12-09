package com.catcher.batch.resource;

import com.catcher.batch.core.database.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthCheckController {

    private final LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok().body("ok");
    }

    /* db 접근 체크용 */
    @GetMapping("/db")
    public ResponseEntity<String> healthCheckWithDB() {
        final var result = locationRepository.findByAreaCode("11"); // 임의의 DB 쿼리
        log.info("result.isPresent() : {}", result.isPresent());
        return ResponseEntity.ok().body("ok");
    }
}
