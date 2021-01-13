package com.hiveelpay.boot.dao;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.hiveelpay.boot.PayBootAppliaction;
import com.hiveelpay.dal.dao.mapper.CreditCardMapper;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayBootAppliaction.class)
public class CreditCardMapperTest {
    @Autowired
    private CreditCardMapper creditCardMapper;

    @Test
    public void updateCardsToExpiredTest() {
        List<String> ids = Lists.newArrayList();
        ids.add("6jxg9s");
//        creditCardMapper.updateCardsToExpired(ids);
    }
}
