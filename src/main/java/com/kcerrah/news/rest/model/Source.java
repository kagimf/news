package com.kcerrah.news.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * Source object for JSON Mapping
 * 
 * @author kcerrah
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Source {
	
	private String name;
	private String url;

}
