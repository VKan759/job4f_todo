package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("categories", categoryService.findAll());
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
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
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
    public String createTask(@ModelAttribute Task task, @SessionAttribute User user,
                             @RequestParam List<Integer> categoryIds) {
        task.setUser(user);
        taskService.addTask(task, categoryIds);
        return "redirect:/tasks";
    }

    @GetMapping("/create")
    public String createTask(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
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
    public String update(Model model, @ModelAttribute Task task, @RequestParam List<Integer> categoryIds) {
        boolean updated = taskService.update(task, categoryIds);
        if (!updated) {
            model.addAttribute("message", "Задача с указанным ID не существует");
            return "errors/404";
        }
        return "redirect:/tasks";
    }
}
