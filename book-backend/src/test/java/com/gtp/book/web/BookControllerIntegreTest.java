package com.gtp.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtp.book.domain.Book;
import com.gtp.book.domain.BookRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 통합 테스트(모든Bean들을 똑같이 IOC에 올리고 테스트 하는 것)
 * WebEnvironment.MOCK = 실제 톰켓을 올리는게 아니라, 다른 톰켓으로 테스트
 * WebEnvironment.RANDOM_POR = 실제 톰켓으로 테스트
 * @AutoConfigureMockMvc MockMvc를 IOC에 등록해줌.
 * @Transactional은 각각의 테스트함수가 종료될때마다 트랜잭션을 rollback 해주는 어노테이션!! 
 */

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)  
public class BookControllerIntegreTest {

	@Autowired 
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		//entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1").executeUpdate(); //MYSQL사용시 
	}
	
	// BDDMockito 패턴 given, when, then
		@Test
		public void save_테스트() throws Exception {
			// given (테스트를 하기 위한 준비
			Book book = new Book(null,"스프링 따라하기","코스");
			String content = new ObjectMapper().writeValueAsString(book);
			
			// when (테스트 실행)
			ResultActions resultAction = mockMvc.perform(post("/book")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(content)
					.accept(MediaType.APPLICATION_JSON_UTF8));
			
			// then (검증)
			resultAction
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("스프링 따라하기"))
				.andDo(MockMvcResultHandlers.print());
		}
		
		@Test
		public void findAll_테스트() throws Exception {
			// given
			List<Book> books = new ArrayList<>();
			books.add(new Book(1L,"스프링부트 따라하기", "코스"));
			books.add(new Book(2L,"리액트 따라하기", "코스"));
			books.add(new Book(3L,"JUnit 따라하기", "코스"));
			bookRepository.saveAll(books);
			
			// when
			ResultActions resultAction = mockMvc.perform(get("/book")
					.accept(MediaType.APPLICATION_JSON_UTF8));
			
			// then
			resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(3)))
				.andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기"))
				.andDo(MockMvcResultHandlers.print());
		}
		
		@Test
		public void findById_테스트() throws Exception {
			// given
			Long id = 2L;
			
			List<Book> books = new ArrayList<>();
			books.add(new Book(1L,"스프링부트 따라하기", "코스"));
			books.add(new Book(2L,"리액트 따라하기", "코스"));
			books.add(new Book(3L,"JUnit 따라하기", "코스"));
			bookRepository.saveAll(books);
			
			
			// when
			ResultActions resultAction = mockMvc.perform(get("/book/{id}",id)
					.accept(MediaType.APPLICATION_JSON_UTF8));
			
			// then
			resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("리액트 따라하기"))
				.andDo(MockMvcResultHandlers.print());
		}
		
		@Test
		public void update_테스트() throws Exception {
			// given
			Long id = 3L;
			
			List<Book> books = new ArrayList<>();
			books.add(new Book(1L,"스프링부트 따라하기", "코스"));
			books.add(new Book(2L,"리액트 따라하기", "코스"));
			books.add(new Book(3L,"JUnit 따라하기", "코스"));
			bookRepository.saveAll(books);
			
			Book book = new Book(null,"C++ 따라하기","코스");
			String content = new ObjectMapper().writeValueAsString(book);
			
			// when
			ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(content)
					.accept(MediaType.APPLICATION_JSON_UTF8));
			
			// then
			resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(3L))
				.andExpect(jsonPath("$.title").value("C++ 따라하기"))
				.andDo(MockMvcResultHandlers.print());
		}
		
		@Test
		public void delete_테스트() throws Exception {
			// given
			Long id = 1L;
			
			List<Book> books = new ArrayList<>();
			books.add(new Book(1L,"스프링부트 따라하기", "코스"));
			books.add(new Book(2L,"리액트 따라하기", "코스"));
			books.add(new Book(3L,"JUnit 따라하기", "코스"));
			bookRepository.saveAll(books);
			
			
			// when
			ResultActions resultAction = mockMvc.perform(delete("/book/{id}",id)
					.accept(MediaType.TEXT_PLAIN));
			
			// then
			resultAction
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());
			
			MvcResult requestResult = resultAction.andReturn();
			String result = requestResult.getResponse().getContentAsString();
			
			assertEquals("ok", result);
		}
		
		
}
