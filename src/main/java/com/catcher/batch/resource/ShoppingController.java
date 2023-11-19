package com.catcher.batch.resource;

import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.common.service.CatcherFeignService;
import com.catcher.batch.core.domain.CommandExecutor;
import com.catcher.batch.core.domain.command.RegisterShoppingDataCommand;
import com.catcher.batch.core.dto.ShoppingApiResponse;
import com.catcher.batch.core.service.ShoppingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shopping")
public class ShoppingController {
    private final CatcherFeignService catcherFeignService;
    private final ShoppingService shoppingService;
    private final CommandExecutor commandExecutor;

    @PostMapping("/batch")
    public CommonResponse<Object> batchShoppingData(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer count
    ) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageNo", page);
        params.put("numOfRows", count);
        ShoppingApiResponse shoppingApiResponse = catcherFeignService.parseService(params, ShoppingApiResponse.class);

        commandExecutor.run(new RegisterShoppingDataCommand(shoppingService, shoppingApiResponse));
        return CommonResponse.success(201, null);
    }
}
