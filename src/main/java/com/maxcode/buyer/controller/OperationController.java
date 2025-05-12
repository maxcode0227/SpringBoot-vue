package com.maxcode.buyer.controller;

import com.maxcode.buyer.entities.Operation;
import com.maxcode.buyer.dao.OperationDao;
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
@RequestMapping("/api/operations")
public class OperationController {

    @Autowired
    private OperationDao operationDao;

    @Value("${com.boylegu.paginatio.max-per-page}")
    Integer maxPerPage;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOperations(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "uid", required = false) Long uid,
            @RequestParam(value = "type", required = false) String type) {
        
        Page<Operation> pageable = new Page<>(page, maxPerPage);
        
        QueryWrapper<Operation> queryWrapper = new QueryWrapper<>();
        if (uid != null) {
            queryWrapper.eq("uid", uid);
        }
        if (type != null && !type.isEmpty()) {
            queryWrapper.eq("type", type);
        }
        
        Page<Operation> result = operationDao.selectPage(pageable, queryWrapper);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getRecords());
        response.put("totalElements", result.getTotal());
        response.put("totalPages", result.getPages());
        response.put("currentPage", page);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOperation(@PathVariable Long id) {
        Operation operation = operationDao.selectById(id);
        if (operation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(operation, HttpStatus.OK);
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<?> getOperationsByUid(
            @PathVariable Long uid,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        return new ResponseEntity<>(
            operationDao.findByUid(uid, new Page<>(page, maxPerPage)), 
            HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<?> createOperation(@RequestBody Operation operation) {
        operationDao.insert(operation);
        return new ResponseEntity<>(operation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOperation(@PathVariable Long id, @RequestBody Operation operation) {
        Operation existingOperation = operationDao.selectById(id);
        if (existingOperation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        operation.setId(id);
        operationDao.updateById(operation);
        return new ResponseEntity<>(operation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOperation(@PathVariable Long id) {
        Operation operation = operationDao.selectById(id);
        if (operation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        operationDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
} 