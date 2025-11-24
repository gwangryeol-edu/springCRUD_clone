package org.example.todoapp_clone.repository;

import java.util.Optional;
import org.example.todoapp_clone.dto.TodoDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TodoRepository {
    private final Map<Long, TodoDto> storage = new ConcurrentHashMap<>();
    private Long nextId = 1L;

    public TodoDto save(TodoDto todo) {
        if (todo.getId() == null) {
            todo.setId(nextId++);
        }
        storage.put(todo.getId(), todo);

        return todo;
    }

    public List<TodoDto> findAll() {
        return new ArrayList<>(storage.values());
    }

    // Optional : 값이 있을수도 있고(null이 아닐수도) 없을 수도 있음(null 일수도
    // null 때문에 생기는 오류를 줄이기 위해 만든 안전한 껍데기(wrapper)
    //
    public Optional findById(Long id) {
//        return storage.get(id);
        return Optional.ofNullable(storage.get(id));
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public List<TodoDto> findByTitleContaining(String keyword) {
        // .stream(): 데이터 흐름으로 표현, for문 대신 더 쉽게 데이터 처리하려고 쓰는 것!
        return storage.values().stream()
            // .contain: 문자열이나 리스트 안에 “특정 값이 들어있는지” 확인
            .filter((todo)->todo.getTitle().contains(keyword))
            .toList();
    }
}

//  원시 null 체크
//TodoDto todo = storage.get(id);
//if (todo == null) {
//    throw new IllegalArgumentException("없음");
//}
//    return todo;

//  optional null 체크
//return Optional.ofNullable(storage.get(id));

