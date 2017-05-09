package com.gluonapplication.views;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.gluonapplication.GluonApplication;
import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import ummisco.remoteGUI.driver.network.MQTTConnector;
import ummisco.remoteGUI.gui.widgets.LineChartBox;
import ummisco.remoteGUI.gui.widgets.ListBox;
import ummisco.remoteGUI.gui.widgets.ListItem;
import ummisco.remoteGUI.gui.widgets.PieChartBox;
import ummisco.remoteGUI.gui.widgets.SliderBox;
import ummisco.remoteGUI.gui.widgets.ValueChangedEvent;

public class FloodOutbreakListPresenter {

	
	private static FloodOutbreakListPresenter scope;
	
	private static MQTTConnector connection = null;
	@FXML
	View floodOutbreakList;
	@FXML
	private ListBox listView;
	
	@FXML
	private Button lancerSimulation;

	
	
	@FXML
	private void valueChanged(ValueChangedEvent evt)
	{
		if(this.connection!=null)
		{
			try {
				this.connection.sendMessage(evt.getAgentName(), evt.getValue());
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
    void startVisualisation(ActionEvent evt)
	{
		sendMessage("START_VISUALISATION");
		
	}

	public static void initConnection()
	{
		scope.initializeConnection();
	}
	
	public void initializeConnection()
	{
		if(connection!=null&&!connection.isConnected()){
    		this.listView.registerConnection(connection);
      	}
		else
		{
			System.out.println("non connecte");
		}
	}
	
	@FXML
	public void initialize() {
		floodOutbreakList.setShowTransitionFactory(BounceInRightTransition::new);
		floodOutbreakList.showingProperty().addListener((obs, oldValue, newValue) -> {
	            if (newValue) {
	                AppBar appBar = MobileApplication.getInstance().getAppBar();
	                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
	                        MobileApplication.getInstance().showLayer(GluonApplication.MENU_LAYER)));
	               appBar.setTitleText("Liste des simulations");
	            }
		});
		//initConnection();
    	FloodOutbreakListPresenter.scope = this;
		initializeConnection();

    }
	
	public static FloodOutbreakListPresenter getScope() {
		return scope;
	}


	public static void setConnection(MQTTConnector b)
	{
		connection=b;
	}
	
	protected static MQTTConnector getConnection(){
		return connection;
	}


}
