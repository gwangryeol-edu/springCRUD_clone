package org.example.todoapp_clone;

import org.example.todoapp_clone.dto.TodoDto;
import org.example.todoapp_clone.repository.TodoRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodoappCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoappCloneApplication.class, args);

    }

    @Bean
    public CommandLineRunner init(ApplicationArguments args, TodoRepository todoRepository){
        return args1 -> {
            todoRepository.save(new TodoDto(null, "study", "JAVA", false));
            todoRepository.save(new TodoDto(null, "cook", "kimbob", false));
            todoRepository.save(new TodoDto(null, "workout", "run", false));
        };
    }
}
