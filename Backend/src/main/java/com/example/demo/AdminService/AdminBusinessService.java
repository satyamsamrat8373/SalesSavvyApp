package com.example.demo.AdminService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.OrderStatus;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class AdminBusinessService {
		    private OrderRepository orderRepository;
		    private OrderItemRepository orderItemRepository;
		    private ProductRepository productRepository;
		
		    public AdminBusinessService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
		        this.orderRepository = orderRepository;
		        this.orderItemRepository = orderItemRepository;
		        this.productRepository = productRepository;
		    }

		    public Map<String, Object> calculateMonthlyBusiness(int month, int year) {
		    	if(year < 2000 || year > 2025) {
		    		throw new IllegalArgumentException("Invalid Year " + year);
		    	}
		    	
		        List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByMonthAndYear(month, year);
		        double totalBusiness = 0.0;
		        Map<String, Integer> categorySales = new HashMap<>();
		
		        for (Order order : successfulOrders) {
		            totalBusiness += order.getTotalAmount().doubleValue();
		            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
		            
		            for (OrderItem item : orderItems) {
		                String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
		                categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
		            }
		        }
		        Map<String, Object> businessReport = new HashMap<>();
		        
		        businessReport.put("totalBusiness", totalBusiness);
		        businessReport.put("categorySales", categorySales);
		        
		        return businessReport;
		    }
		
		    public Map<String, Object> calculateDailyBusiness(LocalDate date) {
		    	if(date == null) {
		    		throw new IllegalArgumentException("Invalid date. Date can't be null! ");
		    	}
		    	
		        List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByDate(date);
		        double totalBusiness = 0.0;
		        Map<String, Integer> categorySales = new HashMap<>();
		
		        for (Order order : successfulOrders) {
		            totalBusiness += order.getTotalAmount().doubleValue();
		            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
		            
		            for (OrderItem item : orderItems) {
		                String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
		                categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
		            }
		        }
		        Map<String, Object> businessReport = new HashMap<>();
		        
		        businessReport.put("totalBusiness", totalBusiness);
		        businessReport.put("categorySales", categorySales);
		        
		        return businessReport;
		    }
		
		    public Map<String, Object> calculateYearlyBusiness(int year) {
		    	if(year < 2000 || year > 2025) {
		    		throw new IllegalArgumentException("Invalid Year " + year);
		    	}
		    	
		        List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByYear(year);
		        double totalBusiness = 0.0;
		        Map<String, Integer> categorySales = new HashMap<>();
		
		        for (Order order : successfulOrders) {
		            totalBusiness += order.getTotalAmount().doubleValue();
		            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
		            
		            for (OrderItem item : orderItems) {
		                String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
		                categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
		            }
		        }
		        Map<String, Object> businessReport = new HashMap<>();
		        
		        businessReport.put("totalBusiness", totalBusiness);
		        businessReport.put("categorySales", categorySales);
		        
		        return businessReport;
		    }

		    public Map<String, Object> calculateOverallBusiness() {
		    	
		    	BigDecimal totalOverAllBusiness = orderRepository.calculateOverallBusiness();
		    	
		    	List<Order> successfulOrders = orderRepository.findAllByStatus(OrderStatus.SUCCESS);
		        
		        Map<String, Integer> categorySales = new HashMap<>();
		
		        for (Order order : successfulOrders) {
		           
		            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
		            
		            for (OrderItem item : orderItems) {
		                String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
		                categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
		            }
		        }
		        Map<String, Object> businessReport = new HashMap<>();
		        
		        businessReport.put("totalBusiness", totalOverAllBusiness.doubleValue());
		        businessReport.put("categorySales", categorySales);
		        
		        return businessReport;
		    }

//		    private Map<String, Object> calculateBusinessMetrics(List<Order> orders) {
//		        double totalRevenue = 0.0;
//		        Map<String, Integer> categorySales = new HashMap<>();
//		
//		        for (Order order : orders) {
//		            totalRevenue += order.getTotalAmount().doubleValue();
//		            List<OrderItem> items = orderItemRepository.findByOrderId(order.getOrderId());
//		            
//		            for (OrderItem item : items) {
//		                String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
//		                categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
//		            }
//		        }
//		
//		        Map<String, Object> metrics = new HashMap<>();
//		        metrics.put("totalRevenue", totalRevenue);
//		        metrics.put("categorySales", categorySales);
//		        return metrics;
//		    }
}
