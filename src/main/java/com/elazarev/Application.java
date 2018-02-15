package com.elazarev;

import com.elazarev.domain.*;

import com.elazarev.repository.RoleRepository;
import com.elazarev.repository.UserRepository;
import com.elazarev.service.UserService;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


/**
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 13.02.18
 */
@SpringBootApplication
@Component
public class Application {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void otot() {

        Page<User> page = userService.doWork(1);
        page.forEach(user -> System.out.println(user));
    }

}
