package com.catcher.batch.core.service;

import com.catcher.batch.core.database.CatcherItemRepository;
import com.catcher.batch.core.domain.entity.CatcherItem;
import com.catcher.batch.core.domain.entity.Category;
import com.catcher.batch.datasource.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExhibitionService {
    private final CatcherItemRepository catcherItemRepository;
    private final CategoryRepository categoryRepository;

    public List<ObjectNode> exhibitionCrawling() {
        List<CatcherItem> catcherItems = new ArrayList<>();
        Category category = categoryRepository.findByName("exhibition")
                .orElseGet(() -> categoryRepository.save(Category.create("exhibition")));

        //TODO 드라이버 경로 상황에 맞게 설정, 그에 따른 크롬 및 gradle 의존성 버전도 알맞게 수정
        System.setProperty("webdriver.chrome.driver", "C:/Users/dong/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        //TODO 파라미터 받아서 월 조절하는 것으로 변경
        driver.get("https://www.akei.or.kr/bbs/board.php?bo_table=schedule");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

        int page = 1;

        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> exhibition_list = new ArrayList<>();

        String[] columns = {"eng_name", "abbreviation_name", "host", "period", "location", "field", "homepage"};

        while (true) {
            WebElement exhibitList = driver.findElement(By.cssSelector(".exhibit_list"));
            List<WebElement> contentElements = exhibitList.findElements(By.cssSelector(".content_sc_li"));

            for (WebElement contentElement : contentElements) {
                WebElement tbody = contentElement.findElement(By.tagName("tbody"));
                List<WebElement> tr_elements = tbody.findElements(By.tagName("tr"));

                List<String> data = new ArrayList<>();
                ObjectNode json = mapper.createObjectNode();

                for (WebElement td_element : tr_elements) {
                    WebElement element = td_element.findElement(By.tagName("td"));
                    data.add(element.getAttribute("innerText"));
                }
                for (int i = 0; i < columns.length; i++) {
                    json.put(columns[i], data.get(i));
                }
                exhibition_list.add(json);
            }

            page++;

            boolean nextPageExists = isNextPageExists(driver, page);
            if (!nextPageExists) {
                break;
            }

            // 다음 페이지 2,3,4...
            WebElement nextPage = driver.findElement(By.xpath("//a[@class='pg_page' and text()='" + page + "']"));

            nextPage.click();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        }
        //TODO driver.quit 에러 안나는 방법 서치
//        driver.quit();

        for (ObjectNode exhibitionInfo : exhibition_list) {
            CatcherItem catcherItem = CatcherItem.builder()
                    .category(category)
                    .title(exhibitionInfo.get("eng_name").asText())
                    .itemHashValue(UUID.randomUUID().toString())
                    .description(exhibitionInfo.get("period").asText())
                    .resourceUrl(exhibitionInfo.get("homepage").asText())
                    .build();

            catcherItems.add(catcherItem);
        }
        catcherItemRepository.saveAll(catcherItems);

        return exhibition_list;
    }

    // 전시회 사이트 다음페이지 여부 검사 근데 어차피 다른 페이지에는 못써서 안에 넣어야할듯?
    private static boolean isNextPageExists(WebDriver driver, int nextPage) {
        try {
            driver.findElement(By.xpath("//a[@class='pg_page' and text()='" + nextPage + "']"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}