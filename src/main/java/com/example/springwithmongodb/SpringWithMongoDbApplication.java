package com.example.springwithmongodb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class SpringWithMongoDbApplication {
    String email = "jahmed@gmail.com";

    public static void main(String[] args) {
        SpringApplication.run(SpringWithMongoDbApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate) {
        return args -> {
            Address address = new Address("England", "London", "NE9");
            Student student = new Student("Jamila", "Ahmed", "jahmed@gmail.com", Gender.FEMALE, address, List.of("Computer Science", "Maths"), BigDecimal.TEN, LocalDateTime.now());

            //usingMongoTemplateAndQuery(repository, mongoTemplate, student);

            repository.findStudentByEmail(email).ifPresentOrElse(s -> {
                throw new IllegalArgumentException("Found many students with email " + email);
            }, () -> {
                System.out.println("inserting student " + student);
                repository.insert(student);
            });
        };
    }

    private void usingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, Student student) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 1) {
            throw new IllegalArgumentException("Found many students with email " + email);
        }

        if (students.isEmpty()) {
            System.out.println("inserting student " + student);
            repository.insert(student);
        } else {
            System.out.println(student + " Student already exists");
        }
    }
}