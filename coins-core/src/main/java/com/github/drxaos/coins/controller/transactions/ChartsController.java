package com.github.drxaos.coins.controller.transactions;

import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.controller.*;
import com.github.drxaos.coins.service.chart.ChartService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@PublishingContext("/api/v1/charts")
public class ChartsController extends AbstractRestController {

    @Inject
    ChartService chartService;

    @Autowire
    @Publish(method = AbstractRestPublisher.Method.GET, path = "/stock")
    public final RestHandler<Void, List<List<Object>>> get = new RestHandler<Void, List<List<Object>>>() {

        @Override
        public List<List<Object>> handle() throws Exception {
            return chartService.makeStockChartData(transport.loggedInUser());
        }

    };
}
