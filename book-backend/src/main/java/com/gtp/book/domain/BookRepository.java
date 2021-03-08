package com.gtp.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;
// @Repository 적어야 스프링 IOC에 빈으로 등록되는다...!!!
// JpaRepository를 extends하면 생략 가능함
// JpaRepository는 CRUD 함수를 들고 있음.
public interface BookRepository extends JpaRepository<Book, Long>{

}
