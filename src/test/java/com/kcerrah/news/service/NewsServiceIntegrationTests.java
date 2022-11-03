package com.kcerrah.news.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kcerrah.news.NewsApiApplication;
import com.kcerrah.news.rest.model.Article;
import com.kcerrah.news.rest.model.SearchType;

/**
 * 
 * Tests for caching mechanism of service layer of News Api
 * 1 second of delay added to each test case to ensure tests are not failing because of the free subscription
 * 
 * @author User
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewsApiApplication.class)
public class NewsServiceIntegrationTests {

	@Autowired
    private CacheManager cacheManager;
	
	@Autowired
	private NewsService newsService;
	
	/*
	 * Test to validate caching in getTopHeadLines
	 */
	@Test
    public void givenTopHeadlinesThatShouldBeCached_whenGetTopHeadlines_thenResultShouldBePutInCache() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		List<Article> topHeadlines = newsService.getTopHeadlines(5);
		
        assertEquals(topHeadlines, getCachedTopHeadlines(5));
    }
	
	/*
	 * Test to validate caching in getSearchResults with title search
	 */
	@Test
	public void givenSearchResultsThatShouldBeCached_whenGetSearchResultsWithTitleSearch_thenResultShouldBePutInCache() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		List<Article> searchResults = newsService.getSearchResults("Next Time, Barry Keoghan Wants to Play the Joker for More Than Four Minutes", SearchType.BY_TITLE);
		
        assertEquals(searchResults, getCachedSearchResults("Next Time, Barry Keoghan Wants to Play the Joker for More Than Four Minutes", SearchType.BY_TITLE));
    }
	
	/*
	 * Test to validate caching in getSearchResults with author search
	 */
	@Test
	public void givenSearchResultsThatShouldBeCached_whenGetSearchResultsWithAuthorSearch_thenResultShouldBePutInCache() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		List<Article> searchResults = newsService.getSearchResults("Newsweek", SearchType.BY_AUTHOR);
		
        assertEquals(searchResults, getCachedSearchResults("Newsweek", SearchType.BY_AUTHOR));
    }
	
	/*
	 * Test to validate caching in getSearchResults with regular search
	 */
	@Test
	public void givenSearchResultsThatShouldBeCached_whenGetSearchResultsWithRegularSearch_thenResultShouldBePutInCache() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		List<Article> searchResults = newsService.getSearchResults("Next", SearchType.REGULAR);
		
        assertEquals(searchResults, getCachedSearchResults("Next", SearchType.REGULAR));
    }
	
	/**
	 * Helper method to retrieve data from cache with identifier topHeadlines and key n
	 * @param n Key for value that will be retrieved from cache
	 * @return List of articles value with key n
	 */
	private List<Article> getCachedTopHeadlines(int n) {
        return cacheManager.getCache("topHeadlines").get(n, List.class);
    }
	
	/**
	 * Helper method to retrieve data from cache with identifier searchResults and key searchParam + searchType
	 * @param searchParam First part of the key for value that will be retrieved from cache
	 * @param searchType Second part of the key for value that will be retrieved from cache
	 * @return List of articles value with key searchParam + searchType
	 */
	private List<Article> getCachedSearchResults(String searchParam, SearchType searchType) {
        return cacheManager.getCache("searchResults").get(searchParam + searchType, List.class);
    }
}
