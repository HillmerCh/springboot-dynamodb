package org.hillmerch.library.data.repositories;

import java.util.Optional;

import org.hillmerch.library.data.model.BookInfo;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface BookInfoRepository  extends CrudRepository<BookInfo, String> {
	Optional<BookInfo> findById(String id);
}
