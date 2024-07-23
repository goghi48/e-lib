package ru.doraemon.e_lib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.doraemon.e_lib.entity.Author;
import ru.doraemon.e_lib.entity.Book;

import jakarta.validation.Valid;
import ru.doraemon.e_lib.entity.Person;
import ru.doraemon.e_lib.repository.AuthorRepository;
import ru.doraemon.e_lib.repository.BookRepository;
import ru.doraemon.e_lib.repository.PersonRepository;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookRepository bookRepository;
    private final PersonRepository personRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookController(BookRepository bookRepository, PersonRepository personRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping()
    public String index(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "book/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Book book = bookRepository.findById(id).orElse(null);
        Person person = book.getPerson();
        Author author = book.getAuthor();
        model.addAttribute("book", book);
        model.addAttribute("person", person);
        model.addAttribute("author", author);
        return "book/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book, Model model) {
        List<Person> people = personRepository.findAll();
        List<Author> authors = authorRepository.findAll();
        model.addAttribute("people", people);
        model.addAttribute("authors", authors);
        return "book/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @RequestParam(value = "personId", required = false) Integer personId,
                         @RequestParam(value = "authorId", required = false) Integer authorId, Model model) {
        if (bindingResult.hasErrors()) {
            List<Person> people = personRepository.findAll();
            List<Author> authors = authorRepository.findAll();
            model.addAttribute("people", people);
            model.addAttribute("authors", authors);
            return "book/new";
        }

        if (personId != null) {
            Person person = personRepository.findById(personId).orElse(null);
            if (person != null) {
                book.setPerson(person);
            }
        }
        if (authorId != null) {
            Author author = authorRepository.findById(authorId).orElse(null);
            if (author != null) {
                book.setAuthor(author);
            }
        }

        bookRepository.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Book book = bookRepository.findById(id).orElse(null);
        List<Person> people = personRepository.findAll();
        List<Author> authors = authorRepository.findAll();
        model.addAttribute("book", book);
        model.addAttribute("people", people);
        model.addAttribute("authors", authors);
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id, @RequestParam(value = "personId", required = false) Integer personId,
                         @RequestParam(value = "authorId", required = false) Integer authorId) {
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        if (personId != null) {
            Person person = personRepository.findById(personId).orElse(null);
            if (person != null) {
                book.setPerson(person);
            }
        }
        if (authorId != null) {
            Author author = authorRepository.findById(authorId).orElse(null);
            if (author != null) {
                book.setAuthor(author);
            }
        }
        book.setId(id);
        bookRepository.save(book);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookRepository.deleteById(id);
        return "redirect:/book";
    }
}
