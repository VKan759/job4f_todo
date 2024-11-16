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
    public String getDone(Model model) {
        model.addAttribute("tasks", taskService.findAll().stream().filter(Task::isDone).toList());
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNew(Model model) {
        model.addAttribute("tasks", taskService.findAll().stream().filter(task -> !task.isDone()).toList());
        return "tasks/list";
    }

    @PostMapping("/create")
    public String create(Model model, Task task) {
        taskService.addTask(task);
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/create")
    public String create() {
        return "tasks/create";
    }

    @GetMapping("/done/{id}")
    public String setDone(@PathVariable int id) {
        log.info("Установка флага выполнено");
        Task byId = taskService.findById(id).orElse(null);
        byId.setDone(true);
        taskService.update(byId);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        log.info("deleting Task");
        taskService.delete(id);
        return "redirect:/tasks";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task) {
        taskService.update(task);
        return "redirect:/tasks";
    }
}
