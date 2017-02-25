package com.gluonapplication.views;

import com.airhacks.afterburner.views.FXMLView;
import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class FloodOutbreakView extends FXMLView {
	 
	private final String name;

	public FloodOutbreakView(String name) {
	        this.name = name;
	    }
	 
	public View getView() {
	        try {
	            View view = FXMLLoader.load(ConnectionView.class.getResource("floodOutbreakList.fxml"));
	            view.setName(name);
	            return view;
	        } catch (IOException e) {
	            System.out.println("IOException: " + e);
	            return new View(name);
	        }
	    }
}
