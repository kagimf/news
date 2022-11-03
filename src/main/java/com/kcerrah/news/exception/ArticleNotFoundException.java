package com.kcerrah.news.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 
 * Exception that is thrown when an article is not found
 * 
 * @author kcerrah
 *
 */
public class ArticleNotFoundException extends ResponseStatusException {

	/**
	 * Constructor with message info
	 * @param errorMessage Message of the exception
	 */
	public ArticleNotFoundException(String errorMessage) {
        super(HttpStatus.NOT_FOUND, errorMessage);
    }
}
