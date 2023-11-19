package com.catcher.batch.resource;

import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.core.domain.CommandExecutor;
import com.catcher.batch.core.domain.command.RegisterRestaurantDataCommand;
import com.catcher.batch.core.dto.RestaurantApiResponse;
import com.catcher.batch.core.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    private final CatcherFeignService catcherFeignService;
    private final RestaurantService restaurantService;
    private final CommandExecutor commandExecutor;

    @PostMapping("/batch")
    public CommonResponse<Object> batchRestaurantData(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer count
    ) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("pageNo", page);
            params.put("numOfRows", count);
            RestaurantApiResponse restaurantApiResponse = catcherFeignService.parseService(params, RestaurantApiResponse.class);

            commandExecutor.run(new RegisterRestaurantDataCommand(restaurantService, restaurantApiResponse));
            return CommonResponse.success(201, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
}
