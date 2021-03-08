package com.gtp.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gtp.book.domain.Book;
import com.gtp.book.domain.BookRepository;

/**
 * 단위 테스트 (Service와 관련된 애들만 메모리에 띄우면 됨.) 
 * BookRepository => 가짜 객체로 만들 수 있음.
 *
 */



@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

	// 1. 서비스 Bean 필요 <--- 2번으로 생성된 객체를 @InjectMocks를 이용해서 가짜 객체 주입 
	@InjectMocks // BookService 객체가 만들어질 때 BookServiceUnitTest 파일에 @Mock로 등록된 모든 애들을 주입받는다.
	private BookService bookService;
	
	// 2. 가짜 bookRepository 생성
	@Mock
	private BookRepository bookRepository;
	
	
	@Test
	public void 저장하기_테스트() {
		// BODMocikto 방식
		// given
		Book book = new Book();
		book.setTitle("책제목1");
		book.setAuthor("책저자1");
		
		// stub-동작 지정
		when(bookRepository.save(book)).thenReturn(book);
		
		// test execute
		Book bookEntity = bookService.저장하기(book);
		
		// then
		assertEquals(bookEntity, book);
	}
	
	
	
	
}
