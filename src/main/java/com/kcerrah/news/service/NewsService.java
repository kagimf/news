package com.kcerrah.news.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.kcerrah.news.exception.ArticleNotFoundException;
import com.kcerrah.news.exception.IllegalArticleCountException;
import com.kcerrah.news.rest.model.Article;
import com.kcerrah.news.rest.model.NewsResponse;
import com.kcerrah.news.rest.model.SearchType;

import lombok.AllArgsConstructor;

/**
 * 
 * Service layer of News Api
 * 
 * @author kcerrah
 *
 */
@AllArgsConstructor
@Service
public class NewsService {
	
	private static final String API_KEY = "62ad4a5dfddc24a5912fc49912b01dc5";
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Calls and retrieves response from /top-headlines endpoint of GNews Api
	 * @param n Maximum number of articles
	 * @return List of articles of size n
	 * @throws IllegalArticleCountException
	 */
	@Cacheable(value = "topHeadlines", key = "#n")
	public List<Article> getTopHeadlines(int n) throws IllegalArticleCountException{
		//n can be 10 at maximum because free subscription allows that
		if(n > 10) {
			throw new IllegalArticleCountException("Article count cannot be more than 10!");
		}
		if(n < 0) {
			throw new IllegalArticleCountException("Article count cannot be a negative number!");
		}
		String url = "https://gnews.io/api/v4/top-headlines";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
		        .queryParam("token", API_KEY)
		        .queryParam("max", n);
		NewsResponse response = restTemplate.getForObject(builder.build().toUriString(), NewsResponse.class);
		return response.getArticles();
	}
	
	/**
	 * Calls and retrieves response from /search endpoint of GNews Api
	 * @param searchParam Keyword to be searched
	 * @param searchType Type of the search operation
	 * @return List of articles that contains keyword searchParam
	 * @throws ArticleNotFoundException
	 */
	@Cacheable(value = "searchResults", key = "#searchParam + #searchType")
	public List<Article> getSearchResults(String searchParam, SearchType searchType) throws ArticleNotFoundException {
		String url = "https://gnews.io/api/v4/search";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
		        .queryParam("q", "\"" + searchParam + "\"")
		        .queryParam("token", API_KEY);
		if(searchType.equals(SearchType.BY_TITLE)) {
			builder.queryParam("in", "title");
		}
		NewsResponse response = restTemplate.getForObject(builder.build().toUriString(), NewsResponse.class);
		List<Article> result = new ArrayList<>();
		if(searchType.equals(SearchType.BY_TITLE)) {
			for(Article a : response.getArticles()) {
				if(a.getTitle().equals(searchParam)) {
					result.add(a);
					return result;
				}
			}
			throw new ArticleNotFoundException("Search parameter does not match any found article's title.");
		}else if(searchType.equals(SearchType.BY_AUTHOR)){
			for(Article a : response.getArticles()) {
				if(a.getSource().getName().equals(searchParam)) {
					result.add(a);
				}
			}
			if(result.size() > 0) {
				return result;
			}
			throw new ArticleNotFoundException("Search parameter does not match any found article's author.");
		}else if(searchType.equals(SearchType.REGULAR)){
			if(response.getArticles().size() > 0) {
				return response.getArticles();
			}
		}
		throw new ArticleNotFoundException("Search parameter does not match any found article.");
	}
}
