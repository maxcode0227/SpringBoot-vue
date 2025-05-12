package com.maxcode.buyer.controller;

import com.maxcode.buyer.entities.Order;
import com.maxcode.buyer.dao.OrderDao;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderDao orderDao;

    @Value("${com.boylegu.paginatio.max-per-page}")
    Integer maxPerPage;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOrders(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "uid", required = false) Long uid,
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(value = "status", required = false) String status) {
        
        Page<Order> pageable = new Page<>(page, maxPerPage);
        
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (uid != null) {
            queryWrapper.eq("uid", uid);
        }
        if (productId != null) {
            queryWrapper.eq("product_id", productId);
        }
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }
        
        Page<Order> result = orderDao.selectPage(pageable, queryWrapper);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getRecords());
        response.put("totalElements", result.getTotal());
        response.put("totalPages", result.getPages());
        response.put("currentPage", page);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        Order order = orderDao.selectById(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<?> getOrdersByUid(
            @PathVariable Long uid,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        return new ResponseEntity<>(
            orderDao.findByUid(uid, new Page<>(page, maxPerPage)), 
            HttpStatus.OK
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getOrdersByProductId(
            @PathVariable Long productId,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        return new ResponseEntity<>(
            orderDao.findByProductId(productId, new Page<>(page, maxPerPage)), 
            HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        orderDao.insert(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order existingOrder = orderDao.selectById(id);
        if (existingOrder == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        order.setId(id);
        orderDao.updateById(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        Order order = orderDao.selectById(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        orderDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
} 