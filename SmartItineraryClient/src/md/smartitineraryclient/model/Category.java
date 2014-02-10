package md.smartitineraryclient.model;

public class Category {
	private String category;
	private boolean selected = false;

	public Category(String category, boolean selected) {
		this.category = category;
		this.setSelected(selected);
	}

	public String getCategory() {
		return category;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
