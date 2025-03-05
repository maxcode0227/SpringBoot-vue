package com.maxcode.buyer.controller;

import com.maxcode.buyer.entities.Persons;

import com.maxcode.buyer.dao.PersonsRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.maxcode.buyer.controller.pagination.PaginationMultiTypeValuesHelper;
import com.maxcode.buyer.controller.pagination.PaginationFormatting;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/api/persons")
public class MainController {

    @Autowired
    private PersonsRepository personsRepository;

    @Value(("${com.boylegu.paginatio.max-per-page}"))
    Integer maxPerPage;

    @RequestMapping(value = "/sex", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSexAll() {

        /*
         * @api {GET} /api/persons/sex Get all sexList
         * @apiName GetAllSexList
         * @apiGroup Info Manage
         * @apiVersion 1.0.0
         * @apiExample {httpie} Example usage:
         *
         *     http /api/persons/sex
         *
         * @apiSuccess {String} label
         * @apiSuccess {String} value
         */

        ArrayList<Map<String, String>> results = new ArrayList<>();

        for (Object value : personsRepository.findSex()) {

            Map<String, String> sex = new HashMap<>();

            sex.put("label", value.toString());

            sex.put("value", value.toString());

            results.add(sex);
        }

        ResponseEntity<ArrayList<Map<String, String>>> responseEntity = new ResponseEntity<>(results,
                HttpStatus.OK);

        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, PaginationMultiTypeValuesHelper> getPersonsAll(
            @RequestParam(value = "page", required = false) Integer pages,
            @RequestParam("sex") String sex,
            @RequestParam("email") String email
    ) {

        /*
         *   @api {GET} /api/persons   Get all or a part of person info
         *   @apiName GetAllInfoList
         *   @apiGroup Info Manage
         *   @apiVersion 1.0.0
         *
         *   @apiExample {httpie} Example usage: (support combinatorial search)
         *
         *       All person：
         *       http /api/persons
         *
         *       You can according to 'sex | email' or 'sex & email'
         *       http /api/persons?sex=xxx&email=xx
         *       http /api/persons?sex=xxx
         *       http /api/persons?email=xx
         *
         *   @apiParam {String} sex
         *   @apiParam {String} email
         *
         *   @apiSuccess {String} create_datetime
         *   @apiSuccess {String} email
         *   @apiSuccess {String} id
         *   @apiSuccess {String} phone
         *   @apiSuccess {String} sex
         *   @apiSuccess {String} username
         *   @apiSuccess {String} zone
         */

        if (pages == null) {
            pages = 1;
        }

        // 使用MyBatis-Plus的分页
        Page<Persons> pageable = new Page<>(pages - 1, maxPerPage);
        
        // 构建查询条件
        QueryWrapper<Persons> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        
        if (sex != null && !sex.isEmpty()) {
            queryWrapper.eq("sex", sex);
        }
        if (email != null && !email.isEmpty()) {
            queryWrapper.eq("email", email);
        }

        PaginationFormatting paginInstance = new PaginationFormatting();
        return paginInstance.filterQuery(queryWrapper, pageable);
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ResponseEntity<Persons> getUserDetail(@PathVariable Long id) {

        /*
        *    @api {GET} /api/persons/detail/:id  details info
        *    @apiName GetPersonDetails
        *    @apiGroup Info Manage
        *    @apiVersion 1.0.0
        *
        *    @apiExample {httpie} Example usage:
        *
        *        http GET http://127.0.0.1:8000/api/persons/detail/1
        *
        *    @apiSuccess {String} email
        *    @apiSuccess {String} id
        *    @apiSuccess {String} phone
        *    @apiSuccess {String} sex
        *    @apiSuccess {String} username
        *    @apiSuccess {String} zone
        */

        Persons user = personsRepository.selectById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.PUT)
    public Persons updateUser(@PathVariable Long id, @RequestBody Persons data) {

        /*
         *  @api {PUT} /api/persons/detail/:id  update person info
         *  @apiName PutPersonDetails
         *  @apiGroup Info Manage
         *  @apiVersion 1.0.0
         *
         *  @apiParam {String} phone
         *  @apiParam {String} zone
         *
         *  @apiSuccess {String} create_datetime
         *  @apiSuccess {String} email
         *  @apiSuccess {String} id
         *  @apiSuccess {String} phone
         *  @apiSuccess {String} sex
         *  @apiSuccess {String} username
         *  @apiSuccess {String} zone

        */
        Persons user = personsRepository.selectById(id);

        if (user != null) {
            user.setPhone(data.getPhone());
            user.setZone(data.getZone());
            personsRepository.updateById(user);
        }

        return user;
    }

}