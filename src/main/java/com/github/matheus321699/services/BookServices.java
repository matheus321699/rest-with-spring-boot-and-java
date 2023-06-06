package com.github.matheus321699.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.matheus321699.controllers.BookController;
import com.github.matheus321699.controllers.PersonController;
import com.github.matheus321699.data.vo.v1.BookVO;
import com.github.matheus321699.exceptions.RequiredObjectIsNullException;
import com.github.matheus321699.exceptions.ResourceNotFoundException;
import com.github.matheus321699.mapper.DozerMapper;
import com.github.matheus321699.model.Book;
import com.github.matheus321699.repositories.BookRepository;

@Service
public class BookServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	BookRepository repository;
	
	public List<BookVO> findAll() {
		logger.info("Finding all book!");
		
		var persons = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
		persons.stream()
		.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		return persons;
	}
	
	public BookVO findById(Long id) {
		
		logger.info("Finding one book!");
		
		var entity = repository.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
	
		var vo = DozerMapper.parseObject(entity, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return vo;

	}

	public BookVO create(BookVO book) {
		if (book == null) throw new RequiredObjectIsNullException();
		logger.info("Creating one book!");
		var entity = DozerMapper.parseObject(book, Book.class);
		var vo =  DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public BookVO update(BookVO book) {
		if (book == null) throw new RequiredObjectIsNullException();
		
		logger.info("Updating one book!");
		
		var entity = repository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		
		var vo =  DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public void delete(Long id) {
		logger.info("Deleting one book!");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		repository.delete(entity);	
	}

	

}
