package com.cohort.disqord.models.ajaxSearch;

import javax.validation.constraints.NotBlank;

public class SearchCriteria {
	@NotBlank(message = "Search cant be empty")
	String input;
	
	public SearchCriteria() {}
	
	public String getInput() {
		return input;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
}
