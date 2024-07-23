package ru.doraemon.e_lib.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.doraemon.e_lib.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
