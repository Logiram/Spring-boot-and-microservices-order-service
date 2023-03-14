package com.codefreak.orderservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orderlineitemsdto {
	//private Long id;
	private String skuCode;
	private BigDecimal price;
	private int quantity;
}
