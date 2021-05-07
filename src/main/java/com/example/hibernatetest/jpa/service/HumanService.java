package com.example.hibernatetest.jpa.service;

import com.example.hibernatetest.jpa.entity.Human;
import com.example.hibernatetest.jpa.repository.HumanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class HumanService {
    private static final Logger log = LoggerFactory.getLogger(HumanService.class);

    private final HumanRepository repository;
    private final EntityManager entityManager;

    public HumanService(HumanRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Transactional
    public void increaseAge(int threadNumber, long humanId) {
        Human human = repository.findOneById(humanId);
        this.log("Selected non-locked instance: " + human);

        if (threadNumber == 0) {
            human = repository.findByIdLocked(humanId);
            this.log("[1] Selected locked instance: " + human);
            human.setAge(human.getAge() + 1);
            repository.save(human);
            this.pause(5000);
            return;
        }

        //lets wait till first thread will acquire lock
        this.pause(100);
        //this.entityManager.clear();
        human = repository.findByIdLocked(humanId);
        this.log("[2] Selected locked instance: " + human);
    }

    private void log(String s) {
        log.info("[" + Thread.currentThread().getId() + "] " + s);
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.warn("Interrupted", e);
        }
    }
}
