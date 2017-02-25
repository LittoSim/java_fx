package ummisco.remoteGUI.gui.widgets;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class ButtonBox extends Button {

	private String agent_name;
	private String agent_action;
	
	public ButtonBox()
	{
		EventHandler<ActionEvent> evt = new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	System.out.println("coudu");
		    	fireEvent(new ValueChangedEvent(agent_name,agent_action,"done"));
		    }
		};
		this.setOnAction(evt);
	}
	
	public void setAgent_name(String lbl)
	{
		this.agent_name= lbl;
	}
	
	public String getAgent_name()
	{
		return this.agent_name;
	}

	public String getAgent_action() {
		return agent_action;
	}

	public void setAgent_action(String agent_action) {
		this.agent_action = agent_action;
	}
}
