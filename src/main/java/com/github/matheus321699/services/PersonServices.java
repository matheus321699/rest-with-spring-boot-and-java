package com.github.matheus321699.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import com.github.matheus321699.controllers.PersonController;
import com.github.matheus321699.data.vo.v1.PersonVO;
import com.github.matheus321699.data.vo.v2.PersonVOV2;
import com.github.matheus321699.exceptions.RequiredObjectIsNullException;
import com.github.matheus321699.exceptions.ResourceNotFoundException;
import com.github.matheus321699.mapper.DozerMapper;
import com.github.matheus321699.mapper.custom.PersonMapper;
import com.github.matheus321699.model.Person;
import com.github.matheus321699.repositories.PersonRepository;

/*  */
@Service 
public class PersonServices {
		
//	private final AtomicLong counter = new AtomicLong();
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	PersonMapper mapper;
	
	public List<PersonVO> findAll() {
		
		logger.info("Finding one person!");
		
		var persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
		persons.stream()
				.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		return persons;
	}
	
	public PersonVO finById(Long id) {
		
		logger.info("Finding one person!");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public PersonVO create(PersonVO person) {
		
		if (person == null) throw new RequiredObjectIsNullException();
		logger.info("Creating one person!");
		var entity = DozerMapper.parseObject(person, Person.class);
		var vo =  DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;

	}
	
	public PersonVOV2 createV2(PersonVOV2 person) {
		
		logger.info("Creating one person with V2!");
		var entity = mapper.convertVoToEntity(person);
		var vo =  mapper.convertEntityToVo(repository.save(entity));
		return vo;
		
	}
	
	public PersonVO update(PersonVO person) {
		
		if (person == null) throw new RequiredObjectIsNullException();
		
		logger.info("Updating one person!");
		
		var entity = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		/* Retornando um mock. Um mock é uma estrutura de código, normalmente temporaria,
		 * que sustenta o desenvolvimento do código até que outros componentes estejam prontos.
		 * A medida que o projeto vai evoluindo eles são removidos. Também existem mocks permanentes
		 * utilizados em testes automatizados.*/
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		var vo =  DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person!");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		repository.delete(entity);
		
	}
	
/*	
	private PersonVO mockPerson(int i) {
		
		logger.info("Finding all people!");
		
		PersonVO person = new PersonVO();
//		person.setId(counter.incrementAndGet());
		person.setFirstName("PersonVO name " + i);
		person.setLastName("Last name " + i);
		person.setAdress("Some address in Brasil " + i);
		person.setGender("Male");
		return person;
	}
*/
	
}
