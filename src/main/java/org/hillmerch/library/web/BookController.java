package org.hillmerch.library.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hillmerch.library.exception.BookInfoNotFoundException;
import org.hillmerch.library.data.model.BookInfo;
import org.hillmerch.library.data.repositories.BookInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookController {

	private static final Logger logger = LoggerFactory.getLogger( BookController.class);

	@Autowired
	BookInfoRepository bookInfoRepository;

	@PostMapping("/books")
	public BookInfo newBookInfo(@RequestBody BookInfo bookInfo) {
		logger.info("Adding the book " + bookInfo);
		return bookInfoRepository.save( bookInfo );
	}

	@GetMapping("/books/{id}")
	@ResponseBody
	public BookInfo getBookInfo(@PathVariable final String id) {
		logger.info("Finding the book with id " + id);
		return bookInfoRepository.findById(id)
				.orElseThrow(() -> new BookInfoNotFoundException( id));
	}
	@GetMapping("/books")
	@ResponseBody
	public Collection<BookInfo> allBookInfos() {
		logger.info("Searching all books");
		return StreamSupport.stream( bookInfoRepository.findAll().spliterator(), false)
				.collect( Collectors.toList());

	}
}
