package com.maxcode.buyer.dao;

import com.maxcode.buyer.entities.Account;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AccountDaoTest {

    @Autowired
    private AccountDao accountDao;

    @Test
    public void testFindById() {
        // 创建测试账户
        Account account = new Account();
        account.setUsername("testUser");
        account.setLoginPassword(DigestUtils.md5DigestAsHex("password123".getBytes()));
        account.setVipLevel(1);
        accountDao.insert(account);

        // 测试查询
        Account found = accountDao.findById(account.getId());
        assertNotNull(found);
        assertEquals("testUser", found.getUsername());
        assertEquals(1, found.getVipLevel());
    }

    @Test
    public void testFindAll() {
        // 创建测试数据
        for (int i = 0; i < 5; i++) {
            Account account = new Account();
            account.setUsername("user" + i);
            account.setLoginPassword(DigestUtils.md5DigestAsHex(("password" + i).getBytes()));
            account.setVipLevel(i % 2 + 1);
            accountDao.insert(account);
        }

        // 测试分页查询
        Page<Account> page = new Page<>(1, 3);
        IPage<Account> result = accountDao.findAll(page);

        assertNotNull(result);
        assertEquals(3, result.getRecords().size());
        assertTrue(result.getTotal() >= 5);
    }

    @Test
    public void testFindByUsernameLike() {
        // 创建测试数据
        Account account1 = new Account();
        account1.setUsername("testUser1");
        account1.setVipLevel(1);
        accountDao.insert(account1);

        Account account2 = new Account();
        account2.setUsername("testUser2");
        account2.setVipLevel(2);
        accountDao.insert(account2);

        // 测试模糊查询
        Page<Account> page = new Page<>(1, 10);
        IPage<Account> result = accountDao.findByUsernameLike("testUser", page);

        assertNotNull(result);
        assertTrue(result.getRecords().size() >= 2);
        assertTrue(result.getRecords().stream()
                .allMatch(a -> a.getUsername().contains("testUser")));
    }

    @Test
    public void testFindByVipLevel() {
        // 创建测试数据
        Account account1 = new Account();
        account1.setUsername("vipUser1");
        account1.setVipLevel(2);
        accountDao.insert(account1);

        Account account2 = new Account();
        account2.setUsername("vipUser2");
        account2.setVipLevel(2);
        accountDao.insert(account2);

        // 测试VIP等级查询
        Page<Account> page = new Page<>(1, 10);
        IPage<Account> result = accountDao.findByVipLevel(2, page);

        assertNotNull(result);
        assertTrue(result.getRecords().size() >= 2);
        assertTrue(result.getRecords().stream()
                .allMatch(a -> a.getVipLevel() == 2));
    }

    @Test
    public void testFindByUsernameAndPassword() {
        // 创建测试账户
        String password = "testPassword123";
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        Account account = new Account();
        account.setUsername("loginUser");
        account.setLoginPassword(encryptedPassword);
        account.setVipLevel(1);
        accountDao.insert(account);

        // 测试登录
        Account found = accountDao.findByUsernameAndPassword("loginUser", encryptedPassword);
        assertNotNull(found);
        assertEquals("loginUser", found.getUsername());
        assertEquals(encryptedPassword, found.getLoginPassword());

        // 测试错误密码
        Account notFound = accountDao.findByUsernameAndPassword("loginUser", 
            DigestUtils.md5DigestAsHex("wrongPassword".getBytes()));
        assertNull(notFound);
    }
} 