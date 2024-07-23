package ru.doraemon.e_lib.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.doraemon.e_lib.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
