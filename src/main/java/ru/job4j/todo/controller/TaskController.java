package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
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
        model.addAttribute("tasks", taskService.findTaskByStatus(true));
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNewTasks(Model model) {
        model.addAttribute("tasks", taskService.findTaskByStatus(false));
        return "tasks/list";
    }

    @PostMapping("/create")
    public String createTask(Task task, @SessionAttribute User user) {
        task.setUser(user);
        taskService.addTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/create")
    public String createTask() {
        return "tasks/create";
    }

    @GetMapping("/done/{id}")
    public String setDone(Model model, @PathVariable int id) {
        log.info("Установка флага выполнено");
        boolean done = taskService.setDoneById(id);
        if (!done) {
            model.addAttribute("message", "Не удалось установить значение \"Выполнено\"");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        boolean delete = taskService.delete(id);
        if (!delete) {
            model.addAttribute("message", "Не удалось удалить задачу");
            return "errors/404";
        }
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
