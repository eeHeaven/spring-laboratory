package spring.aop.pointcut;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import spring.aop.annotation.ClassAop;
import spring.aop.annotation.MethodAop;
import spring.aop.member.MemberService;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Slf4j
    @Aspect
    static class ParameterAspect{
        @Pointcut("execution(* spring.aop.member..*(..))")
        private void allMember(){};

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1] {}, arg={}",joinPoint.getSignature(),arg1);
            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable { //String으로 받기도 가능
            log.info("[logArgs2]{}, arg = {}",joinPoint.getSignature(),arg);
            return joinPoint.proceed();
        }

        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj){
            log.info("[this]{}, obj={}",joinPoint.getSignature(),obj.getClass());
        }
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, MemberService obj){
            log.info("[target]{}, obj={}",joinPoint.getSignature(),obj.getClass());
        }
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation){
            log.info("[@target]{}, obj={}",joinPoint.getSignature(),annotation);
        }
        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation){
            log.info("[@within] {}, obj={}",joinPoint.getSignature(),annotation);
        }
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation){
            log.info("[@annotation]{}, obj={}",joinPoint.getSignature(),annotation.value());
        }


    }

    @Test
    void execute(){
        memberService.hello("hi");
    }
}
