package ummisco.remoteGUI.gui.widgets;

public class ListItem implements Comparable<ListItem>{

	private static int MAX_ID = 0;
	int id = 0;
	String title;
	String subTitle;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public ListItem(String title, String subTitle) {
		this();
		this.title = title;
		this.subTitle = subTitle;
	}
	public ListItem(String title) {
		this();
		this.title = title;
		this.subTitle = null;
	}
	
	public ListItem()
	{
		super();
		this.id=MAX_ID++;
	}
	@Override
	public int compareTo(ListItem o) {
		return Integer.compare(id, o.id);
	}


}
