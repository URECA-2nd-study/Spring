package com.spring.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService2 {

    public void testRequiredB(){
        throw new RuntimeException("Exception in B");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW) // REQUIRED_NEW 전파 옵션
    public void testRequiredNewB() {
        try{
            throw new RuntimeException("Exception in B");
        } catch (Exception e){
            log.error("error");
        }
    }

    @Transactional(propagation = Propagation.MANDATORY) // 기존 트랜잭션이 필요함
    public void testMandatorydB() {
        System.out.println("Executing methodB");
    }
}
