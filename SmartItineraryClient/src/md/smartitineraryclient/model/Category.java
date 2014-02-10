package md.smartitineraryclient.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
	private String category;
	private List<String> subCategories;

	public Category(String category, List<String> subCategories) {
		this.category = category;
		this.subCategories = subCategories;
	}
	
	public Category() {
		category = "";
		subCategories = new ArrayList<String>();
	}

	public String getCategory() {
		return category;
	}

	public List<String> getSubCategories() {
		return subCategories;
	}
}
