package spring.aop.order;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import spring.aop.order.aop.AspectV1;
import spring.aop.order.aop.AspectV2;
import spring.aop.order.aop.AspectV3;
import spring.aop.order.aop.AspectV4;

@Slf4j
@SpringBootTest
@Import(AspectV4.class)
public class AopTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void aopInfo(){
        log.info("isAopProxy, orderService = {}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy, orderRepository = {}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void success(){
        orderService.orderItem("test1");
    }
    @Test
    void exception(){
        Assertions.assertThatThrownBy(()-> orderService.orderItem("ex"))
                .isInstanceOf(IllegalStateException.class);
    }


}
