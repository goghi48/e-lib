package ru.doraemon.e_lib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.doraemon.e_lib.entity.Author;

import jakarta.validation.Valid;
import ru.doraemon.e_lib.entity.Book;
import ru.doraemon.e_lib.repository.AuthorRepository;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/author")
public class AuthorController {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping()
    public String index(Model model) {
        List<Author> authors = authorRepository.findAll();
        model.addAttribute("authors", authors);
        return "author/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author != null) {
            Set<Book> books = author.getBooks();
            model.addAttribute("author", author);
            model.addAttribute("books", books);
        }
        return "author/show";
    }

    @GetMapping("/new")
    public String newAuthor(@ModelAttribute("author") Author author) {
        return "author/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("author") @Valid Author author, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "author/new";
        }

        authorRepository.save(author);
        return "redirect:/author";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Author author = authorRepository.findById(id).orElse(null);
        model.addAttribute("author", author);
        return "author/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("author") @Valid Author author, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "author/edit";
        }

        author.setId(id);
        authorRepository.save(author);
        return "redirect:/author";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        authorRepository.deleteById(id);
        return "redirect:/author";
    }
}
