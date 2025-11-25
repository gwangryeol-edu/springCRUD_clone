package org.example.todoapp_clone.service;

import java.util.List;
import org.example.todoapp_clone.dto.TodoDto;
import org.example.todoapp_clone.repository.TodoRepository;
import org.springframework.stereotype.Service;

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
