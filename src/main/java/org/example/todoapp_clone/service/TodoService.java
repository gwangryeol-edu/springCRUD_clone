package org.example.todoapp_clone.service;

import java.util.List;
import org.example.todoapp_clone.dto.TodoDto;
import org.example.todoapp_clone.repository.TodoRepository;
import org.springframework.stereotype.Service;

// controller에서 service를 분리한 이유
// 핵심: Spring MVC 아키텍쳐의 핵심원리, Controller -> Service -> Repository
// 1. controller는 웹 요청 처리만, service는 비즈니스 로직 담당
// 2. 중복 로직을 service에서 재사용할 수 있음(getTOdoById는 여러 곳에서 사용됨)
// 3. 테스트하기 훨씬 쉬워짐(순수 자바 코드로 테스트가능
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoDto> getAllTodos(){
        return todoRepository.findAll();
    }

    public TodoDto getTodoById(Long id) {
        return todoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found : id " + id));
    }

    public void deleteTodoById(Long id) {
        getTodoById(id);
        todoRepository.deleteById(id);
    }

    public TodoDto updateTodoById(Long id, TodoDto newTodo) {
        TodoDto originTodo = getTodoById(id);

        originTodo.setTitle(newTodo.getTitle());
        originTodo.setContent(newTodo.getContent());
        originTodo.setCompleted(newTodo.isCompleted());

        return todoRepository.save(originTodo);
    }

    public TodoDto createTodo(TodoDto todo) {
        return todoRepository.save(todo);
    }

    public List<TodoDto> searchTodos(String keyword) {
        return todoRepository.findByTitleContaining(keyword);
    }

    public List<TodoDto> getTodosByCompleted(boolean completed) {
        return todoRepository.findByCompleted(completed);
    }

    public TodoDto toggleCompleted(Long id) {
        TodoDto todo = getTodoById(id);
        todo.setCompleted(!todo.isCompleted());
        return todoRepository.save(todo);
    }

}
