package com.gluonapplication.views;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.gluonapplication.GluonApplication;
import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.SwipeEvent;
import javafx.util.Duration;
import ummisco.remoteGUI.driver.network.MQTTConnector;
import ummisco.remoteGUI.gui.widgets.LabelBox;
import ummisco.remoteGUI.gui.widgets.LineChartBox;
import ummisco.remoteGUI.gui.widgets.PieChartBox;
import ummisco.remoteGUI.gui.widgets.SliderBox;
import ummisco.remoteGUI.gui.widgets.ValueChangedEvent;

public class GameControlPresenter {

	
	private static GameControlPresenter scope;
	
	public static GameControlPresenter getScope() {
		return scope;
	}

	
	private static MQTTConnector connection = null;
	@FXML
	View gamecontrol;
	
	@FXML
	private Label durationLabel;
	@FXML
	private Button newRound;
	
	@FXML
	private Button lockButton;
	@FXML
	private Button lowButton;
	@FXML
	private Button highButton;
	@FXML
	private LabelBox roundLabel;
	
	private boolean lockedGUI = false;
    
	private Timeline timeline = null;
	private Calendar startDate = null;
	private String oldRound="";
	private long lastUpdate = 0;;
	
	@FXML
	private void valueChanged(ValueChangedEvent evt)
	{
		if(this.connection!=null)
		{
			 try {
					this.connection.sendMessage(evt.getAgentAttributeName(), evt.getValue());
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		 
	}
	
	void sendMessage(String command)
	{
		if(this.connection!=null)
		{
			 try {
					this.connection.sendMessage("littosim_command", command);
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	
	@FXML
    void newRound(ActionEvent evt)
	{
		long upd = Calendar.getInstance().getTimeInMillis();
		if(!this.roundLabel.getText().equals(oldRound) || lastUpdate+10000< upd)
		{
			startTimer();
			oldRound = this.roundLabel.getText();
			lastUpdate = upd;
			sendMessage("NEW_ROUND");
		}
		
	}

	@FXML
	void lockPlayers(ActionEvent evt)
	{
		if(lockedGUI)
		{
			sendMessage("LOCK_USERS");
			lockedGUI = false;
			lockButton.setText("Vérrouiller joueurs");
		}
		else
		{
			sendMessage("UNLOCK_USERS");
			lockedGUI = true;
			lockButton.setText("Dévérrouiller joueurs");			
		}
	}
	@FXML
	void highFlooding(ActionEvent evt)
	{
		sendMessage("HIGH_FLOODING");
	}
	@FXML
	void lowFlooding(ActionEvent evt)
	{
		sendMessage("LOW_FLOODING");
	}
	
	
	private void startTimer()
	{
		if(this.timeline == null)
		{
			this.timeline = new Timeline(new KeyFrame(
			        Duration.millis(1000),
			        ae -> modifyDate()));
			this.timeline.play();
		}
		this.startDate = Calendar.getInstance();

		//send command to server
		
	}
	
	private void modifyDate()
	{
		Calendar currentDate = Calendar.getInstance();
		long duration = currentDate.getTimeInMillis() - startDate.getTimeInMillis();
		Calendar dur =Calendar.getInstance();
		dur.setTimeInMillis(duration);
		duration = (long)(duration / 1000);
		
		long minutes = duration / 60;
		String sec = ""+duration % 60;
		sec = (duration % 60)<10?"0"+sec:sec;
		durationLabel.setText(minutes+ " min "+ sec + "s");
		timeline.playFromStart();
	}
	
	public static void initConnection()
	{
		scope.initializeConnection();
	}
	
	public void initializeConnection()
	{	
		if(connection!=null&&!connection.isConnected()){
			this.roundLabel.registerConnection(connection);
		}
		
  	}
	
	
	@FXML
	public void initialize() {
		gamecontrol.setShowTransitionFactory(BounceInRightTransition::new);
		gamecontrol.showingProperty().addListener((obs, oldValue, newValue) -> {
	            if (newValue) {
	                AppBar appBar = MobileApplication.getInstance().getAppBar();
	                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
	                        MobileApplication.getInstance().showLayer(GluonApplication.MENU_LAYER)));
	               appBar.setTitleText("Contrôle de jeu");
	            }
		});
		EventHandler<SwipeEvent> evt =new EventHandler<SwipeEvent>() {
	        @Override public void handle(SwipeEvent event) {
	        	
	            event.consume();
	        }
	};
		gamecontrol.setOnSwipeLeft(evt);
    	GameControlPresenter.scope = this;
		initializeConnection();

    }
	public static void setConnection(MQTTConnector b)
	{
		connection=b;
	}
	
	protected static MQTTConnector getConnection(){
		return connection;
	}


}
