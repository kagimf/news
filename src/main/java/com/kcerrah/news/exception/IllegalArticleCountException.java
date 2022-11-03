package com.kcerrah.news.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 
 * Exception that is thrown when an illegal article count value comes from user
 * 
 * @author kcerrah
 *
 */
public class IllegalArticleCountException extends ResponseStatusException {
	
	/**
	 * Constructor with message info
	 * @param errorMessage Message of the exception
	 */
	public IllegalArticleCountException(String errorMessage) {
        super(HttpStatus.FORBIDDEN, errorMessage);
    }
}
