package com.ansh.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Order {

    @JsonProperty("order_id")
    private int orderId;

    private String status;
}
