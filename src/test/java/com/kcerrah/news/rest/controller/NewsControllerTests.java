package com.kcerrah.news.rest.controller;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcerrah.news.rest.model.Article;
import com.kcerrah.news.rest.model.SearchType;
import com.kcerrah.news.service.NewsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 
 * Tests for web layer of News Api
 * 
 * @author User
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(NewsController.class)
public class NewsControllerTests {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private NewsService newsService;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Test for /news endpoint
	 * @throws Exception 
	 */
	@Test
	public void givenTopHeadlines_whenGetNews() throws Exception {
		List<Article> response = objectMapper.readValue(loadFile("response/top_headlines_response.json"), new TypeReference<List<Article>>(){});
		when(newsService.getTopHeadlines(5)).thenReturn(response);
		
		mvc.perform(get("/news").param("articleCount", "5")
			      .accept(MediaType.APPLICATION_JSON_VALUE))
			      .andDo(print())
			      .andExpect(status().isOk())
			      .andExpect(content().json(loadFile("response/expected_top_headlines_response.json")));
	}

	/**
	 * Test for /news/findByTitle endpoint
	 * @throws Exception 
	 */
	@Test
	public void givenSearchResults_whenGetArticleByTitle() throws Exception {
		String title = "Next Time, Barry Keoghan Wants to Play the Joker for More Than Four Minutes";
		List<Article> response = objectMapper.readValue(loadFile("response/search_results_with_title_search_response.json"), new TypeReference<List<Article>>(){});
		when(newsService.getSearchResults(title, SearchType.BY_TITLE)).thenReturn(response);
		
		mvc.perform(get("/news/findByTitle").param("searchParam", title)
			      .accept(MediaType.APPLICATION_JSON_VALUE))
			      .andDo(print())
			      .andExpect(status().isOk())
			      .andExpect(content().json(loadFile("response/expected_search_results_with_title_search_response.json")));
	}
	
	/**
	 * Test for /news/findByAuthor endpoint
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */

	@Test
	public void givenSearchResults_whenGetArticlesByAuthor() throws Exception {
		String author = "Newsweek";
		List<Article> response = objectMapper.readValue(loadFile("response/search_results_with_author_search_response.json"), new TypeReference<List<Article>>(){});
		when(newsService.getSearchResults(author, SearchType.BY_AUTHOR)).thenReturn(response);
		
		mvc.perform(get("/news/findByAuthor").param("searchParam", author)
			      .accept(MediaType.APPLICATION_JSON_VALUE))
			      .andDo(print())
			      .andExpect(status().isOk())
			      .andExpect(content().json(loadFile("response/expected_search_results_with_author_search_response.json")));
	}

	/**
	 * Test for /news/find endpoint
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	
	@Test
	public void givenSearchResults_whenGetArticles() throws Exception {
		String searchParam = "Next";
		List<Article> response = objectMapper.readValue(loadFile("response/search_results_with_regular_search_response.json"), new TypeReference<List<Article>>(){});
		when(newsService.getSearchResults(searchParam, SearchType.REGULAR)).thenReturn(response);
		
		mvc.perform(get("/news/find").param("searchParam", searchParam)
			      .accept(MediaType.APPLICATION_JSON_VALUE))
			      .andDo(print())
			      .andExpect(status().isOk())
			      .andExpect(content().json(loadFile("response/expected_search_results_with_regular_search_response.json")));
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
}
