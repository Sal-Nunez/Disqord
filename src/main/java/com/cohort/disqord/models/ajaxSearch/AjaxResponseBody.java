package com.cohort.disqord.models.ajaxSearch;

import java.util.List;

import com.cohort.disqord.models.User;

public class AjaxResponseBody {
	String msg;
	List<User> result;
	
	
	public AjaxResponseBody() {}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<User> getResult() {
		return result;
	}
	public void setResult(List<User> result) {
		this.result = result;
	}
	
}
