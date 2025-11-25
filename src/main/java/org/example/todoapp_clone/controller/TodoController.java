package org.example.todoapp_clone.controller;

import org.example.todoapp_clone.dto.TodoDto;
import org.example.todoapp_clone.repository.TodoRepository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
// 이 컨트롤러 안에 있는 모든 메서드의 기본 URL 앞에 /todos가 자동으로 붙는다.
@RequestMapping("/todos")
public class TodoController {

    //private final TodoRepository todoRepository = new TodoRepository();
    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping
    public String todos(Model model) {
        // 제네릭(Generic) 문법, Java에서 List 같은 컬렉션이 어떤 타입의 데이터를 담는지 “명시”하는 문법.
        // TodoDto만(O) -> 컴파일 단계에서부터 타입을 안전하게 강제
        List<TodoDto> todos = todoRepository.findAll();
        model.addAttribute("todos", todoRepository.findAll());
        return "todos";
    }

    @GetMapping("/new")
    public String newTodo() {
        return "new";
    }

    @GetMapping("/create")
    public String create(
        @RequestParam String title,
        @RequestParam String content,
        Model model
    ) {
        TodoDto todoDto = new TodoDto(null, title, content, false);
        //TodoRepository todoRepository = new TodoRepository();

        TodoDto todo = todoRepository.save(todoDto);
        model.addAttribute("todo", todo);

        //return "create";
        return "redirect:/todos";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
//        TodoDto todo = todoRepository.findById(id);
        try {
            TodoDto todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("todo not found!!!"));
            // .orElseThrow()는 Optional 객체에서만 반환
            // @TodoRepoitory에서 findById가 Optional을 반환하게 변환하여야

            model.addAttribute("todo", todo);
            return "detail";

        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {
        // 삭제로직
        todoRepository.deleteById(id);
        return "redirect:/todos";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {

        try {
            TodoDto todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("todo not found!!!"));
            model.addAttribute("todo", todo);
            return "edit";
        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/{id}/update")
    public String update(
        @PathVariable Long id,
        @RequestParam String title,
        @RequestParam String content,
        @RequestParam(defaultValue = "false") Boolean completed,
        Model model) {

        try {
            TodoDto todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("todo not found!!!"));
            todo.setTitle(title);
            todo.setContent(content);
            todo.setCompleted(completed);

            todoRepository.save(todo);

            return "redirect:/todos/" + id;

        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {

        List<TodoDto> todos = todoRepository.findByTitleContaining(keyword);

        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/active")
    public String active(Model model) {
        List<TodoDto> todos = todoRepository.findByCompleted(false);
        model.addAttribute("todos", todos);
        return "active";
    }

    @GetMapping("/completed")
    public String completed(Model model) {
        List<TodoDto> todos = todoRepository.findByCompleted(true);
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, Model model){

        try {
            TodoDto todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("todo not found!!!!"));
            todo.setCompleted(!todo.isCompleted());
            todoRepository.save(todo);
            return "redirect:/todos/" + id;

        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }
}
