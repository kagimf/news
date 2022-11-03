package com.kcerrah.news.rest.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * Article object for JSON mapping
 * 
 * @author kcerrah
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Article {
	
	private String title;
	private String description;
	private String content;
	private String url;
	private String image;
	private Date publishedAt;
	private Source source;
}
