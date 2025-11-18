package org.example.todoapp_clone.controller;

import org.example.todoapp_clone.dto.TodoDto;
import org.example.todoapp_clone.repository.TodoRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TodoController {

    @GetMapping("/todos")
    public String todos() {
        return "todos";
    }

    @GetMapping("/todos/new")
    public String newTodo() {
        return "new";
    }

    @GetMapping("todos/create")
    public String create(
            @RequestParam String title,
            @RequestParam String content,
            Model model
    ) {
        TodoDto todoDto = new TodoDto(null, title, content, false);
        TodoRepository todoRepository = new TodoRepository();

        TodoDto todo = todoRepository.save(todoDto);
        model.addAttribute("todo", todo);

        return "create";
    }
}
