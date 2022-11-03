package com.kcerrah.news.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcerrah.news.NewsApiApplication;
import com.kcerrah.news.exception.ArticleNotFoundException;
import com.kcerrah.news.exception.IllegalArticleCountException;
import com.kcerrah.news.rest.model.Article;
import com.kcerrah.news.rest.model.NewsResponse;
import com.kcerrah.news.rest.model.SearchType;

/**
 * 
 * Tests for service layer of News Api
 * 
 * @author User
 *
 */
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = NewsApiApplication.class)
public class NewsServiceTests {

	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private NewsService newsService;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private static final String API_KEY = "62ad4a5dfddc24a5912fc49912b01dc5";
	
	/**
	 * Test for getHeadlines
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@Test
	public void givenTopHeadlines_whenGetTopHeadlines() throws JsonMappingException, JsonProcessingException {
		int n = 5;
		String url = "https://gnews.io/api/v4/top-headlines";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
		        .queryParam("token", API_KEY)
		        .queryParam("max", n);
		NewsResponse response = objectMapper.readValue(loadFile("response/top_headlines_response_from_external_api.json"), NewsResponse.class);
		when(restTemplate.getForObject(builder.build().toUriString(), NewsResponse.class)).thenReturn(response);
		
		List<Article> result = newsService.getTopHeadlines(n);
		
		assertEquals(result.size(), n);
	}
	
	/**
	 * Test to verify IllegalArticleCountException is thrown when n value is bigger than 10
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@Test
	public void givenTopHeadlines_whenGetTopHeadlinesWithNValueMoreThan10() throws JsonMappingException, JsonProcessingException {
		Exception exception = assertThrows(IllegalArticleCountException.class, () -> {
			newsService.getTopHeadlines(15);
	    });

	    assertEquals(exception.getMessage(), "403 FORBIDDEN \"Article count cannot be more than 10!\"");
	}
	
	/**
	 * Test to verify IllegalArticleCountException is thrown when n value is negative
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@Test
	public void givenTopHeadlines_whenGetTopHeadlinesWithNegativeNValue() throws JsonMappingException, JsonProcessingException {
		Exception exception = assertThrows(IllegalArticleCountException.class, () -> {
			newsService.getTopHeadlines(-5);
	    });

	    assertEquals(exception.getMessage(), "403 FORBIDDEN \"Article count cannot be a negative number!\"");
	}
	
	/**
	 * Test for getSearchReslts with title search
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@Test
	public void givenSearchResults_whenGetSearchResultsWithTitleSearch() throws JsonMappingException, JsonProcessingException {
		String title = "Next Time, Barry Keoghan Wants to Play the Joker for More Than Four Minutes";
		SearchType searchType = SearchType.BY_TITLE;
		String url = prepareUrl(title, searchType);
		NewsResponse response = objectMapper.readValue(loadFile("response/search_results_with_title_search_response_from_external_api.json"), NewsResponse.class);
		when(restTemplate.getForObject(url, NewsResponse.class)).thenReturn(response);
		
		List<Article> result = newsService.getSearchResults(title, searchType);
		
		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getTitle(), title);
	}
	
	/**
	 * Test to verify ArticleNotFoundException when title is not found in any articles
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@Test
	public void givenSearchResults_whenGetSearchResultsWithTitleSearchWithMeaninglessTitle() throws JsonMappingException, JsonProcessingException {
		String title = "meaninglessTitle";
		SearchType searchType = SearchType.BY_TITLE;
		String url = prepareUrl(title, searchType);
		NewsResponse response = objectMapper.readValue(loadFile("response/empty_response_from_external_api.json"), NewsResponse.class);
		when(restTemplate.getForObject(url, NewsResponse.class)).thenReturn(response);
		
		Exception exception = assertThrows(ArticleNotFoundException.class, () -> {
			newsService.getSearchResults(title, searchType);
	    });

	    assertEquals(exception.getMessage(), "404 NOT_FOUND \"Search parameter does not match any found article's title.\"");
	}
	
	/**
	 * Test for getSearchReslts with suthor search
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@Test
	public void givenSearchResults_whenGetSearchResultsWithAuthorSearch() throws JsonMappingException, JsonProcessingException {
		String author = "Newsweek";
		SearchType searchType = SearchType.BY_AUTHOR;
		String url = prepareUrl(author, searchType);
		NewsResponse response = objectMapper.readValue(loadFile("response/search_results_with_author_search_response_from_external_api.json"), NewsResponse.class);
		when(restTemplate.getForObject(url, NewsResponse.class)).thenReturn(response);
		
		List<Article> result = newsService.getSearchResults(author, searchType);
		
		assertTrue(result.size() > 0);
		assertEquals(result.get(0).getSource().getName(), author);
	}
	
	/**
	 * Test to verify ArticleNotFoundException when author is not found in any articles
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@Test
	public void givenSearchResults_whenGetSearchResultsWithAuthorSearchWithMeaninglessAuthor() throws JsonMappingException, JsonProcessingException {
		String author = "meaninglessAuthor";
		SearchType searchType = SearchType.BY_AUTHOR;
		String url = prepareUrl(author, searchType);
		NewsResponse response = objectMapper.readValue(loadFile("response/empty_response_from_external_api.json"), NewsResponse.class);
		when(restTemplate.getForObject(url, NewsResponse.class)).thenReturn(response);
		
		Exception exception = assertThrows(ArticleNotFoundException.class, () -> {
			newsService.getSearchResults(author, searchType);
	    });

	    assertEquals(exception.getMessage(), "404 NOT_FOUND \"Search parameter does not match any found article's author.\"");
	}
	
	/**
	 * Test for getSearchReslts with regular search
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@Test
	public void givenSearchResults_whenGetSearchResultsWithRegularSearch() throws JsonMappingException, JsonProcessingException {
		String searchParam = "Next";
		SearchType searchType = SearchType.REGULAR;
		String url = prepareUrl(searchParam, searchType);
		NewsResponse response = objectMapper.readValue(loadFile("response/search_results_with_regular_search_response_from_external_api.json"), NewsResponse.class);
		when(restTemplate.getForObject(url, NewsResponse.class)).thenReturn(response);
		
		List<Article> result = newsService.getSearchResults(searchParam, searchType);
		
		assertTrue(result.size() > 0);
	}
	
	/**
	 * Test to verify ArticleNotFoundException when keyword is not found in any articles
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@Test
	public void givenSearchResults_whenGetSearchResultsWithRegularSearchWithMeaninglessSearchKey() throws JsonMappingException, JsonProcessingException {
		String searchParam = "meaninglessSearchKey";
		SearchType searchType = SearchType.REGULAR;
		String url = prepareUrl(searchParam, searchType);
		NewsResponse response = objectMapper.readValue(loadFile("response/empty_response_from_external_api.json"), NewsResponse.class);
		when(restTemplate.getForObject(url, NewsResponse.class)).thenReturn(response);
		
		Exception exception = assertThrows(ArticleNotFoundException.class, () -> {
			newsService.getSearchResults(searchParam, searchType);
	    });

	    assertEquals(exception.getMessage(), "404 NOT_FOUND \"Search parameter does not match any found article.\"");
	}
	
	/**
	 * Helper method to load a file from resources folder
	 * @param fileName Name of the file that will be loaded
	 * @return The loaded file as a string
	 */
	private String loadFile(String fileName) {
		try {
			return FileUtils.readFileToString(Paths.get("src/test/resources/" + fileName).toFile(), Charset.defaultCharset());
		} catch(IOException e) {
			
		}
		return "";
	}
	
	/**
	 * Helper method to prepare request url for getSearchResults tests
	 * @param searchParam Keyword for search operation
	 * @param searchType Type of search operation
	 * @return Prepared url as a string
	 */
	private String prepareUrl(String searchParam, SearchType searchType) {
		String url = "https://gnews.io/api/v4/search";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
		        .queryParam("q", "\"" + searchParam + "\"")
		        .queryParam("token", API_KEY);
		if(searchType.equals(SearchType.BY_TITLE)) {
			builder.queryParam("in", "title");
		}
		return builder.build().toUriString();
	}
	
}
