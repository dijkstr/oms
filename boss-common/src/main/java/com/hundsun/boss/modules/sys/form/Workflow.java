package com.hundsun.boss.modules.sys.form;

import com.hundsun.boss.base.form.BaseForm;

public class Workflow extends BaseForm{

	private String key;
	
	private String name;
	
	private String description;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
