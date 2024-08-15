package models;

public class Menu {

	private String Title;
	private String Instructions;
	private String[] MenuItems;
	private String Underlying;
	
	public Menu() {
		
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String[] getMenuItems() {
		return MenuItems;
	}

	public void setMenuItems(String[] menuItems) {
		MenuItems = menuItems;
	}

	public String getUnderlying() {
		return Underlying;
	}

	public void setUnderlying(String underlying) {
		Underlying = underlying;
	}

	public String getInstructions() {
		return Instructions;
	}

	public void setInstructions(String instructions) {
		Instructions = instructions;
	}
	
}
