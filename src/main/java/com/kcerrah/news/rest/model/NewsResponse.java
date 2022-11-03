package com.kcerrah.news.rest.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * NewsResponse object for JSON Mapping
 * 
 * @author kcerrah
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewsResponse {
	
	private int totalArticles;
	private List<Article> articles;

}
