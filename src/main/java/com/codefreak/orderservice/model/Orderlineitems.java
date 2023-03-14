package com.codefreak.orderservice.model;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.transaction.Transactional;
import javax.persistence.CascadeType;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

	@Entity
	@Table(name= "t_order_line_items")
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	@Transactional
	public class Orderlineitems {

		@Id	
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
		
		private String skuCode;
	
		private BigDecimal price;
		
		private Integer Quantity;
}
