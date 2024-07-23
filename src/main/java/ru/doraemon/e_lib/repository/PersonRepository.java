package ru.doraemon.e_lib.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.doraemon.e_lib.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}
