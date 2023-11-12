package com.catcher.batch.resource;

import com.catcher.batch.common.service.CatcherCrawlerService;
import com.catcher.batch.core.dto.FestivalApiResponse;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/crawler")
public class CrawlerController {

    private final CatcherCrawlerService catcherCrawlerService;

    @GetMapping("/exhibition")
    public ResponseEntity<List<ObjectNode>> getExhibitionData() {
        List<ObjectNode> exhibitionCrawlingResponse = catcherCrawlerService.exhibitionCrawling();
        return ResponseEntity.ok(exhibitionCrawlingResponse);
    }
}
