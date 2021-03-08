package com.gtp.book.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

//단위 테스트 (DB 관련된 Bean이 IOC에 등록되면 됨)

@Slf4j
@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY) // Replace.ANY 가짜 디비로 테스트, Replace.NONE 실제 DB로 테스트 
@DataJpaTest // Repository들을 다 IOC에 등록해둠.
public class BookRepositoryUnitTest {

	@Autowired
	private BookRepository bookRepository;
	
	@Test
	public void save_테스트() {
		// given
		Book book = new Book(null,"책제목1","책저자1");
		
		// when
		Book bookEntity = bookRepository.save(book);
		
		// then
		assertEquals("책제목1", bookEntity.getTitle());
	}
	
	@Test
	public void findAll_테스트() {
		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(null,"스프링부트 따라하기", "코스"));
		books.add(new Book(null,"리액트 따라하기", "코스"));
		bookRepository.saveAll(books);
		
		// when
		List<Book> bookEntitys = bookRepository.findAll();
		
		// then
		log.info("bookEntitys : " + bookEntitys.size());
		assertNotEquals(0, bookEntitys.size());
		assertEquals(2, bookEntitys.size());
		
	}
	
}
