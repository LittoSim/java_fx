package ummisco.remoteGUI.gui.widgets;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.scene.control.Label;
import ummisco.remoteGUI.driver.common.FollowedVariable;
import ummisco.remoteGUI.driver.network.MQTTConnector;

public class LabelBox extends Label  implements Observer {

	private FollowedVariable  flVariable ;
	private String followedVar = "";
	public void setFollow(String lbl)
	{
		followedVar = lbl;
	}
	
	public String getAgentName()
	{
		return flVariable.getName();
	}

	public void setAgentName(String e)
	{
		this.flVariable = new FollowedVariable(e);
		this.flVariable.addObserver(this);
	}

	public String getFollow()
	{
		return followedVar;
	}
	public void registerConnection(MQTTConnector con)
	{
		con.registerVariable(this.flVariable);
		this.flVariable.addObserver(this);
	}

	
	
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof FollowedVariable)
		{
			FollowedVariable f = (FollowedVariable)o;
			List<Map<String,Object>> datas = f.popLastData();
			Map<String,Object> dte = datas.get(datas.size()- 1);
			Runnable rn = new Runnable() {

				@Override
				public void run() {
					setText(dte.get(getFollow()).toString());
				}
			};
			Platform.runLater(rn);
			
		}
		
	}

}
