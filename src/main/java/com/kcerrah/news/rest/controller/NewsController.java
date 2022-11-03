package com.kcerrah.news.rest.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kcerrah.news.exception.ArticleNotFoundException;
import com.kcerrah.news.exception.IllegalArticleCountException;
import com.kcerrah.news.rest.model.Article;
import com.kcerrah.news.rest.model.SearchType;
import com.kcerrah.news.service.NewsService;

import lombok.AllArgsConstructor;

/**
 * 
 * Web layer of News Api
 * 
 * @author User
 *
 */
@RestController
@AllArgsConstructor
@RequestMapping(NewsController.PATH_NEWSCONTROLLER)
public class NewsController {
	
	static final String PATH_NEWSCONTROLLER = "/news";
	
	@Autowired
	private NewsService newsService;
	
	/**
	 * Retrieves news
	 * @param articleCount Count of article that is requested
	 * @return List of articles of size articleCound
	 * @throws IllegalArticleCountException
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Article> getNews(@RequestParam int articleCount) throws IllegalArticleCountException {
		return newsService.getTopHeadlines(articleCount);
	}
	
	/**
	 * Searches a given title in articles
	 * @param searchParam Title of an article
	 * @return An article with title searchParam if exists
	 * @throws ArticleNotFoundException
	 */
	@GetMapping(path = "/findByTitle", produces = MediaType.APPLICATION_JSON_VALUE)
	public Article getArticleByTitle(@RequestParam String searchParam) throws ArticleNotFoundException {
		return newsService.getSearchResults(searchParam, SearchType.BY_TITLE).get(0);
	}
	
	/**
	 * Searches a given title in articles
	 * @param searchParam Author of one or more articles
	 * @return List of articles with author searchParam if any exists
	 * @throws ArticleNotFoundException
	 */
	@GetMapping(path = "/findByAuthor", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Article> getArticlesByAuthor(@RequestParam String searchParam) throws ArticleNotFoundException {
		return newsService.getSearchResults(searchParam, SearchType.BY_AUTHOR);
	}
	
	/**
	 * Searches a given keyword in articles
	 * @param searchParam A keyword that might present in one or more articles
	 * @return List of articles that contains searchParam if any exists
	 * @throws ArticleNotFoundException
	 */
	@GetMapping(path = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Article> getArticles(@RequestParam String searchParam) throws ArticleNotFoundException {
		return newsService.getSearchResults(searchParam, SearchType.REGULAR);
	}
}
