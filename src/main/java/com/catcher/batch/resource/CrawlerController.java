package com.catcher.batch.resource;

import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.service.ExhibitionService;
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

    private final ExhibitionService exhibitionService;

    @GetMapping("/exhibition")
    public ResponseEntity<List<CatcherItem>> getExhibitionData() {
        List<CatcherItem> exhibitionCrawlingResponse = exhibitionService.exhibitionCrawling();
        return ResponseEntity.ok(exhibitionCrawlingResponse);
    }
}
