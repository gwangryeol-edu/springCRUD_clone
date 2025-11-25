package org.example.todoapp_clone.controller;

import org.example.todoapp_clone.dto.TodoDto;
import org.example.todoapp_clone.service.TodoService;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
// 이 컨트롤러 안에 있는 모든 메서드의 기본 URL 앞에 /todos가 자동으로 붙는다.
@RequestMapping("/todos")
public class TodoController {

    //private final TodoRepository todoRepository = new TodoRepository();
    //private final TodoRepository todoRepository;

//    public TodoController(TodoRepository todoRepository) {
//        this.todoRepository = todoRepository;
//    }
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public String todos(Model model) {
        // 제네릭(Generic) 문법, Java에서 List 같은 컬렉션이 어떤 타입의 데이터를 담는지 “명시”하는 문법.
        // TodoDto만(O) -> 컴파일 단계에서부터 타입을 안전하게 강제
        // List<TodoDto> todos = todoRepository.findAll();
        List<TodoDto> todos = todoService.getAllTodos();
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/new")
    public String newTodo(Model model){
        model.addAttribute("todo", new TodoDto());
        return "form";
    }

    // @GetMapping("/create")
    // Get-> 조회만 하는 요청, Post-> 데이터 생성/변경
    @PostMapping
    public String create(
//        @RequestParam String title,
//        @RequestParam String content,
        @ModelAttribute TodoDto todo,
        RedirectAttributes redirectAttributes
//        Model model
    ) {
//        TodoDto todoDto = new TodoDto(null, title, content, false);
        //TodoRepository todoRepository = new TodoRepository();

//        TodoDto todo = todoRepository.save(todoDto);
//        todoRepository.save(todo);
//        model.addAttribute("todo", todo);
        todoService.createTodo(todo);

        // FlashAttribute가 하는 일: reduct 시 1번만 살아있는 데이터를 전달하는 방식
        // session에 잠깐 저장 -> redirect 후 바로 삭제
        redirectAttributes.addFlashAttribute("message", "할 일이 생성되었습니다");
        //redirect는 기본적으로 새로운 요청 새로고침이기 때문에
        //Model에 넣은 값은 사라짐 -> Model로는 데이터를 전달할 수 없음

        // Flash Attribute로 해결
        //	1.	Redirect 직전, Flash 저장소(임시 세션)에 message 저장
        //	2.	redirect:/todos 로 이동
        //	3.	/todos 요청에서 Model에 자동 포함됨
        //	4.	즉시 Flash 저장소에서 삭제됨 (1회성)
        // => 화면 상단에 띄우는 일회성 알림 메시지에 알맞음

        //return "create";
        return "redirect:/todos";
    }

    @GetMapping("/{id}")
    public String detail(
        @PathVariable Long id, Model model) {
//        TodoDto todo = todoRepository.findById(id);
        try {
//            TodoDto todo = todoRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("todo not found!!!"));
            // .orElseThrow()는 Optional 객체에서만 반환
            // @TodoRepoitory에서 findById가 Optional을 반환하게 변환하여야
            TodoDto todo = todoService.getTodoById(id);

            model.addAttribute("todo", todo);
            return "detail";

        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes,
        Model model) {
        // 삭제로직
//        todoRepository.deleteById(id);
        todoService.deleteTodoById(id);
        redirectAttributes.addFlashAttribute("message", "할일이 삭제되었습니다.");
        redirectAttributes.addFlashAttribute("status", "delete");
        return "redirect:/todos";
    }

    @GetMapping("/{id}/update")
    public String edit(@PathVariable Long id, Model model) {

        try {
//            TodoDto todo = todoRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("todo not found!!!"));
            TodoDto todo = todoService.getTodoById(id);
            model.addAttribute("todo", todo);
            return "form";
        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @PostMapping("/{id}/update")
    public String update(
        @PathVariable Long id,
//        @RequestParam String title,
//        @RequestParam String content,
//        @RequestParam(defaultValue = "false") Boolean completed,



//        HTML form → Java 객체(TodoDto) 로 자동 변환해주는 기능.
        // @RequestParam 이런 걸 하나하나 적을 필요 없음
        @ModelAttribute TodoDto todo,
        RedirectAttributes redirectAttributes,
        Model model) {

        try {
//            TodoDto todo = todoRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("todo not found!!!"));
//            todo.setTitle(title);
//            todo.setContent(content);
//            todo.setCompleted(completed);
//            todo.setId(id);
//            todoRepository.save(todo);

            todoService.updateTodoById(id, todo);

            redirectAttributes.addFlashAttribute("message", "할 일이 수정되었습니다.");

            return "redirect:/todos/" + id;

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", "없는 할 일입니다.");
            return "redirect:/todos";
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {

//        List<TodoDto> todos = todoRepository.findByTitleContaining(keyword);
        List<TodoDto> todos = todoService.searchTodos(keyword);

        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/active")
    public String active(Model model) {
//        List<TodoDto> todos = todoRepository.findByCompleted(false);
        List<TodoDto> todos = todoService.getTodosByCompleted(false);
        model.addAttribute("todos", todos);
        return "active";
    }

    @GetMapping("/completed")
    public String completed(Model model) {
//        List<TodoDto> todos = todoRepository.findByCompleted(true);
        List<TodoDto> todos = todoService.getTodosByCompleted(false);
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, Model model){

        try {
//
            todoService.toggleCompleted(id);
            return "redirect:/todos/" + id;

        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }
}
