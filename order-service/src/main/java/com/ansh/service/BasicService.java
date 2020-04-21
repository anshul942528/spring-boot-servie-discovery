package com.ansh.service;

import com.ansh.model.OrderDetail;
import com.ansh.model.OrderInfo;
import com.ansh.model.ResponseDTO;
import com.ansh.model.UserOrders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BasicService {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseDTO getOrderInfo(int userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        UserOrders userOrders = restTemplate.getForObject("http://user-order-service/order/{userId}", UserOrders.class, map);

        List<OrderInfo> list = userOrders.getOrderList().stream()
                .map(order -> {
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("orderId", order.getOrderId());
                    OrderDetail orderDetail = restTemplate.getForObject("http://order-details-service/info/{orderId}", OrderDetail.class, map1);
                    return new OrderInfo(orderDetail.getPrice(), orderDetail.getZipCode(), orderDetail.getItemName(), order.getOrderId(), order.getStatus());
                })
                .collect(Collectors.toList());
        responseDTO.setOrderInfoList(list);
        return responseDTO;
    }
}
