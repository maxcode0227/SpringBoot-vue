package com.maxcode.buyer.controller.pagination;

import java.util.HashMap;
import java.util.Map;

import com.maxcode.buyer.dao.PersonsRepository;
import com.maxcode.buyer.entities.Persons;
import org.springframework.stereotype.Component;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/*
    Resolve due to @Autowired lead to NullPointerException problem

    Description：
    1. It's limited to general class to invoke spring bean Object.
    2. And This makes the sub package easy to scan by spring boot.

                                                      ———— @Boyle.gu
*/
@Component
class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        if (SpringUtil.applicationContext == null) {

            SpringUtil.applicationContext = applicationContext;

        }
    }

    public static ApplicationContext getApplicationContext() {

        return applicationContext;

    }

    public static Object getBean(String name) {

        return getApplicationContext().getBean(name);

    }

    public static <T> T getBean(Class<T> clazz) {

        return getApplicationContext().getBean(clazz);

    }

    public static <T> T getBean(String name, Class<T> clazz) {

        return getApplicationContext().getBean(name, clazz);

    }
}


interface Types {

    public IPage<Persons> query();

    public Integer getCount();

    public Integer getPageNumber();

    public Long getTotal();

    public Object getContent();
}

class BasePaginationInfo {

    public Page<Persons> pageable;

    public PersonsRepository instance = SpringUtil.getBean(PersonsRepository.class);

    public String sex, email;

    public BasePaginationInfo(String sexName, String emailName, int pageNum, int pageSize) {

        this.pageable = new Page<>(pageNum, pageSize);

        this.sex = sexName;

        this.email = emailName;
    }
}

class AllType extends BasePaginationInfo implements Types {


    public AllType(String sexName, String emailName, int pageNum, int pageSize) {

        super(sexName, emailName, pageNum, pageSize);

    }

    public IPage<Persons> query() {

        return this.instance.selectPage(

                this.pageable,
                null

        );
    }

    public Integer getCount() {
        return (int)this.query().getSize();
    }

    public Integer getPageNumber() {

        return (int)this.query().getCurrent();

    }

    public Long getTotal() {
        return this.query().getTotal();
    }

    public Object getContent() {
        return this.query().getRecords();
    }
}

class SexEmailType extends BasePaginationInfo implements Types {

    public SexEmailType(String sexName, String emailName, int pageNum, int pageSize) {

        super(sexName, emailName, pageNum, pageSize);

    }

    public IPage<Persons> query() {

        return this.instance.findBySexAndEmailContains(

                this.sex,

                this.email,

                this.pageable
        );
    }

    public Integer getCount() {
        return (int)this.query().getSize();
    }

    public Integer getPageNumber() {

        return (int)this.query().getCurrent();

    }

    public Long getTotal() {
        return this.query().getTotal();
    }

    public Object getContent() {
        return this.query().getRecords();
    }


}

class SexType extends BasePaginationInfo implements Types {

    public SexType(String sexName, String emailName, int pageNum, int pageSize) {

        super(sexName, emailName, pageNum, pageSize);
    }

    public IPage<Persons> query() {

        return this.instance.findBySex(

                this.sex,

                this.pageable
        );
    }

    public Integer getCount() {
        return (int)this.query().getSize();
    }

    public Integer getPageNumber() {

        return (int)this.query().getCurrent();

    }

    public Long getTotal() {
        return this.query().getTotal();
    }

    public Object getContent() {
        return this.query().getRecords();
    }
}


public class PaginationFormatting {

    private PaginationMultiTypeValuesHelper multiValue = new PaginationMultiTypeValuesHelper();

    private Map<String, PaginationMultiTypeValuesHelper> results = new HashMap<>();

    public Map<String, PaginationMultiTypeValuesHelper> filterQuery(
            QueryWrapper<Persons> queryWrapper, 
            Page<Persons> page) {
        // 实现分页查询逻辑
        // 返回分页结果
        Map<String, PaginationMultiTypeValuesHelper> result = new HashMap<>();
        // ... 实现分页逻辑 ...
        return result;
    }

    public Map<String, PaginationMultiTypeValuesHelper> selectPage(QueryWrapper<Persons> queryWrapper, Page<Persons> page) {
        // 创建返回结果Map
        Map<String, PaginationMultiTypeValuesHelper> resultMap = new HashMap<>();
        
        // 创建分页结果对象
        PaginationMultiTypeValuesHelper paginationHelper = new PaginationMultiTypeValuesHelper();
        
        // 设置分页信息
        paginationHelper.setTotal(page.getTotal());
        paginationHelper.setCurrentPage((int)page.getCurrent());
        paginationHelper.setTotalPage((int)page.getPages());
        paginationHelper.setResults(page.getRecords());
        
        // 将分页结果放入Map
        resultMap.put("pagination", paginationHelper);
        
        return resultMap;
    }
}