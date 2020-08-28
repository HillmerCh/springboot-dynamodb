package org.hillmerch.library.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hillmerch.library.web.BookController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BookInfoNotFoundAdvice extends ResponseEntityExceptionHandler {

	/*@ResponseBody
	@ExceptionHandler(BookInfoNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String bookInfoNotFoundHandler(BookInfoNotFoundException ex, WebRequest request) {
		return ex.getMessage();
	}*/

	@ExceptionHandler(BookInfoNotFoundException.class)
	public ResponseEntity<Object> handleCityNotFoundException(
			BookInfoNotFoundException ex, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put( "timestamp", LocalDateTime.now());
		body.put("message", "City not found");

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
}
