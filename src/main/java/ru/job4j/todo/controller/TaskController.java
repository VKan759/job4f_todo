package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model, HttpSession session) {
        Optional<Task> byId = taskService.findById(id);
        if (byId.isEmpty()) {
            model.addAttribute("message", "Задача с указанным id не найдена");
            return "errors/404";
        }
        model.addAttribute("task", byId.get());
        return "tasks/one";
    }

    @GetMapping("/done")
    public String getDoneTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll().stream().filter(Task::isDone).toList());
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNewTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll().stream().filter(task -> !task.isDone()).toList());
        return "tasks/list";
    }

    @PostMapping("/create")
    public String createTask(Model model, Task task) {
        try {
            taskService.addTask(task);
            model.addAttribute("tasks", taskService.findAll());
            return "tasks/list";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/create")
    public String createTask() {
        return "tasks/create";
    }

    @GetMapping("/done/{id}")
    public String setDone(Model model, @PathVariable int id) {
        log.info("Установка флага выполнено");
        Task byId = taskService.findById(id).orElse(null);
        if (byId == null) {
            model.addAttribute("message", "Задача с указанным ID не существует");
            return "errors/404";
        }
        byId.setDone(true);
        taskService.update(byId);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }

    @PostMapping("/update")
    public String update(Model model, @ModelAttribute Task task) {
        boolean updated = taskService.update(task);
        if (!updated) {
            model.addAttribute("message", "Задача с указанным ID не существует");
            return "errors/404";
        }
        return "redirect:/tasks";
    }
}
