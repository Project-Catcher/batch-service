package com.catcher.batch.resource;

import com.catcher.batch.core.dto.FestivalApiResponse;
import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.common.service.CatcherJsonService;
import com.catcher.batch.core.service.FestivalServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/festival")
public class FestivalController {
    private final CatcherJsonService catcherJsonService;
    private final CatcherFeignService catcherFeignService;
    private final FestivalServiceProxy festivalServiceProxy;

    @GetMapping("/webclient-batch")
    public Mono<ResponseEntity<FestivalApiResponse>> getMovieDataByWebClient(@RequestParam(defaultValue = "1") Integer page,
                                                                             @RequestParam(defaultValue = "5") Integer count) {

        // TODO : Mapping Param 수정
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageNo", page);
        params.put("numOfRows", count);
        params.put("type", "json");

        Mono<FestivalApiResponse> stringMono = catcherJsonService.parseService(params, FestivalApiResponse.class);

        return stringMono
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/feign-batch")
    public ResponseEntity<FestivalApiResponse> getMovieDataByFeign(@RequestParam(defaultValue = "1") Integer page,
                                                                   @RequestParam(defaultValue = "5") Integer count) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageNo", page);
        params.put("numOfRows", count);
        params.put("type", "json");
        FestivalApiResponse festivalApiResponse = catcherFeignService.parseService(params, FestivalApiResponse.class);

        return ResponseEntity.ok(festivalApiResponse);
    }

    //TODO : 이 부분은 배치로 옮겨야함.
    @GetMapping("/batch")
    public ResponseEntity<Void> startBatch() {
        festivalServiceProxy.handleBatch();

        return ResponseEntity.ok(null);
    }
}
