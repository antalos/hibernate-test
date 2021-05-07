package com.example.hibernatetest.controller;

import com.example.hibernatetest.jpa.entity.Human;
import com.example.hibernatetest.jpa.repository.HumanRepository;
import com.example.hibernatetest.jpa.runnable.AsyncCaller;
import com.example.hibernatetest.jpa.service.HumanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;

@RestController
@RequestMapping(path = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    private final HumanRepository repository;

    private final HumanService service;

    public TestController(HumanRepository repository, HumanService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping(path = "test")
    public String test() {
        this.initData();
        this.runThreads();
        return "GOT_IT";
    }

    private void runThreads() {
        Semaphore semaphore = new Semaphore(0);
        new Thread(new AsyncCaller(semaphore, 0, service)).start();
        new Thread(new AsyncCaller(semaphore, 1, service)).start();
        semaphore.release(2);
    }

    private void initData() {
        Human human = repository.findOneById(1L);
        if (human == null) {
            log.info("Creating record");
            human = new Human();
        }

        human.setAge(10);
        repository.save(human);
    }
}

