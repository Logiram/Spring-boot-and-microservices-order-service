package com.codefreak.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.kafka.core.KafkaTemplate;

import com.codefreak.orderservice.dto.InventoryResponse;
import com.codefreak.orderservice.dto.OrderRequest;
import com.codefreak.orderservice.dto.Orderlineitemsdto;
import com.codefreak.orderservice.event.OrderPlacedEvent;
import com.codefreak.orderservice.model.Order;
import com.codefreak.orderservice.model.Orderlineitems;
import com.codefreak.orderservice.repository.OrderRepository;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	private static final String INVENTORY_SERVICE_URI = "http://inventory-service:8081/api/inventory";
	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
	public String placeOrder(OrderRequest orderRequest){
		
		System.out.println("came inside service");
		Order order= new Order();
		order.setOrderNumber(UUID.randomUUID().toString());

		System.out.println(order.getOrderNumber());
		List <Orderlineitems> orderlineitems= orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToDto).collect(Collectors.toList());
		order.setOrderlineitems(orderlineitems);

		List<String>skuCodes=order.getOrderlineitems().stream().map(Orderlineitems::getSkuCode).collect(Collectors.toList());
		//Call inventory service and place order if it is in stock
		InventoryResponse[] inventoryresponseArray=webClientBuilder.build().get().uri(INVENTORY_SERVICE_URI,uriBuilder ->uriBuilder.queryParam("skuCode", skuCodes).build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class).block();
		
		boolean allProductsInStock = Arrays.stream(inventoryresponseArray).allMatch(InventoryResponse::isInStock);
		if (allProductsInStock){
		orderRepository.save(order);
		 kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
		return "Order Placed Successfully";
		}
		
		else{
			throw new IllegalArgumentException("product is not in stock. Kindly check later");
		}
		
	}
	
	private Orderlineitems mapToDto(Orderlineitemsdto orderlineitemsdto){
		Orderlineitems orderlineitems=new Orderlineitems();
		orderlineitems.setPrice(orderlineitemsdto.getPrice());
		orderlineitems.setQuantity(orderlineitemsdto.getQuantity());
		orderlineitems.setSkuCode(orderlineitemsdto.getSkuCode());
		
		return orderlineitems;
		
	}
	
}
