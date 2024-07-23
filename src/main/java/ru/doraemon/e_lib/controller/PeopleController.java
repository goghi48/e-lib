package ru.doraemon.e_lib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.doraemon.e_lib.entity.Book;
import ru.doraemon.e_lib.entity.Person;
import ru.doraemon.e_lib.repository.BookRepository;
import ru.doraemon.e_lib.repository.PersonRepository;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;

    @Autowired
    public PeopleController(PersonRepository personRepository, BookRepository bookRepository) {
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping()
    public String index(Model model) {
        List<Person> people = personRepository.findAll();
        model.addAttribute("people", people);
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Person person = personRepository.findById(id).orElse(null);
        if (person != null) {
            Set<Book> books = person.getBooks();
            model.addAttribute("person", person);
            model.addAttribute("books", books);
        }
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/new";
        }

        personRepository.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Person person = personRepository.findById(id).orElse(null);
        if (person != null) {
            Set<Book> books = person.getBooks();
            model.addAttribute("person", person);
            model.addAttribute("books", books);
        }
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id, @RequestParam(value = "bookIds", required = false) Set<Integer> selectedBookIds) {
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }

        Person existingPerson = personRepository.findById(id).orElse(null);
        if (existingPerson != null) {
            Set<Book> currentBooks = existingPerson.getBooks();
            Set<Book> booksToRemove = new HashSet<>();

            for (Book book : currentBooks) {
                if (selectedBookIds == null || !selectedBookIds.contains(book.getId())) {
                    booksToRemove.add(book);
                }
            }

            for (Book book : booksToRemove) {
                existingPerson.removeBook(book);
            }

            personRepository.save(existingPerson);
        }

        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        Person person = personRepository.findById(id).orElse(null);
        if (person != null) {
            for (Book book : person.getBooks()) {
                book.setPerson(null);
                bookRepository.save(book);
            }
            personRepository.deleteById(id);
        }
        return "redirect:/people";
    }
}