package com.gluonapplication;

import java.util.function.Supplier;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.gluonapplication.views.GameControlPresenter;
import com.gluonapplication.views.GameControlView;
import com.gluonapplication.views.ConnectionView;
import com.gluonapplication.views.FloodOutbreakListPresenter;
import com.gluonapplication.views.FloodOutbreakView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.layout.Layer;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ummisco.remoteGUI.driver.network.MQTTConnector;

public class GluonApplication extends MobileApplication {

    public static final String PRIMARY_VIEW = HOME_VIEW;
    public static final String SECONDARY_VIEW = "Configuration";
    public static final String THIRD_VIEW = "Simulation";
    public static final String MENU_LAYER = "Side Menu";
    
    @Override
    public void init() {
    	try {
			MQTTConnector connection = new MQTTConnector("192.168.1.100", null, null, null);
			if(GameControlPresenter.getScope()==null)
				GameControlPresenter.setConnection(connection);
			else
				GameControlPresenter.initConnection();
			if(FloodOutbreakListPresenter.getScope()==null)
				FloodOutbreakListPresenter.setConnection(connection);
			else
				FloodOutbreakListPresenter.initConnection();
			System.out.println("connected !");
			
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("pb de connexion");
			
		}
		
		Supplier<View> v1 = new Supplier<View>() {

			@Override
			public View get() {
				// TODO Auto-generated method stub
				return (View) new GameControlView("Control de jeux").getView();
			}
		};
		
		
		Supplier<View> v2 = new Supplier<View>() {

			@Override
			public View get() {
				// TODO Auto-generated method stub
				return (View) new ConnectionView("Connection configuration").getView();
			}
		};
		
		Supplier<View> v4 = new Supplier<View>() {

			@Override
			public View get() {
				// TODO Auto-generated method stub
				return (View) new FloodOutbreakView("Submersions").getView();
			}
		};
		
		addViewFactory(PRIMARY_VIEW, v1);
		addViewFactory(THIRD_VIEW, v4);
		addViewFactory(SECONDARY_VIEW, v2);
		
		final NavigationDrawer drawer = new NavigationDrawer();

		NavigationDrawer.Header header = new NavigationDrawer.Header("Littosim",
				"Interface leader",
				new Avatar(21, new Image(GluonApplication.class.getResourceAsStream("/icon.png"))));
		drawer.setHeader(header);
		
		
		
		
		
		final Item primaryItem = new Item("Control", MaterialDesignIcon.HOME.graphic());
		final Item secondaryItem = new Item("Configuration", MaterialDesignIcon.DASHBOARD.graphic());
		final Item thirdItem = new Item("Submersions", MaterialDesignIcon.HISTORY.graphic());
		drawer.getItems().addAll(primaryItem,thirdItem, secondaryItem);
		
		ChangeListener<? super Node> nd1 = new ChangeListener<Node>() {

			@Override
			public void changed(ObservableValue<? extends Node> obs, Node oldItem, Node newItem) {
				hideLayer(MENU_LAYER);
				if(newItem.equals(primaryItem))
				{
					switchView(PRIMARY_VIEW);					
				}
				else if (newItem.equals(thirdItem))
				{
					switchView(THIRD_VIEW);			
				}
				else if (newItem.equals(secondaryItem))
				{
					switchView(SECONDARY_VIEW);			
				}
			}
		};
		drawer.selectedItemProperty().addListener(nd1);
		Supplier<Layer> v3 = new Supplier<Layer>() {

			@Override
			public Layer get() {
				// TODO Auto-generated method stub
				return  new SidePopupView(drawer);
			}
		};
		addLayerFactory(MENU_LAYER, v3);
    }

    @Override
    public void postInit(Scene scene) {
    	Swatch.GREEN.assignTo(scene);
    	this.setTitle("Littosim game leader");
        scene.getStylesheets().add(GluonApplication.class.getResource("views/floodOutbreakList.css").toExternalForm());
        scene.getStylesheets().add(GluonApplication.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(GluonApplication.class.getResourceAsStream("/icon.png")));
    }
}
