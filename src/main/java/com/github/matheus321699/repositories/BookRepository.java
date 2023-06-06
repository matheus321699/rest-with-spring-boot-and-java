package com.github.matheus321699.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.matheus321699.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

}
