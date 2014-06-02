package com.excilys.formation.webproject.binding;

import org.springframework.stereotype.Component;

import com.excilys.formation.webproject.core.PageWrapper;

@Component
public class Decoder {

	public void fromCode(PageWrapper pageWrapper) {
		
		String orderNumber = pageWrapper.getOrderNumber();
		
		String fieldOrder = "cpu.name";
		String order = "DESC";
		
		switch (orderNumber){
		case "0":
			order ="ASC";
		case "1":
			break;
		case "2":
			order ="ASC";
		case "3":
			fieldOrder = "cpu.introduced";
			break;
		case "4":
			order ="ASC";
		case "5":
			fieldOrder = "cpu.discontinued";
			break;
		case "6":
			order ="ASC";
		case "7":
			fieldOrder = "cpu.company";
			break;
		default :
			fieldOrder = "cpu.id";
			break;
		}
		pageWrapper.setFieldOrder(fieldOrder);
		pageWrapper.setOrder(order);
	}
	
	public void toCode(PageWrapper pageWrapper) {
		String fieldOrder = pageWrapper.getFieldOrder();
		String order = pageWrapper.getOrder();
		String orderNumber = null;
		
		switch (fieldOrder){
		case "cpu.name":
			if (order.equals("ASC")) orderNumber = "0";
			else orderNumber = "1";
			break;
		case "cpu.introduced":
			if (order.equals("ASC")) orderNumber = "2";
			else orderNumber = "3";
			break;
		case "cpu.discontinued":
			if (order.equals("ASC")) orderNumber = "4";
			else orderNumber = "5";
			break;
		case "cpu.company":
			if (order.equals("ASC")) orderNumber = "6";
			else orderNumber = "7";
			break;
		default :
			order = "0";
			break;
		}
		pageWrapper.setOrderNumber(orderNumber);
	}
}
