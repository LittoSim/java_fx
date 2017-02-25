
package com.gluonapplication.views;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.gluonapplication.GluonApplication;
import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ummisco.remoteGUI.driver.network.MQTTConnector;

public class ConnectionPresenter {

    @FXML
    private View secondary;
    
    @FXML
    private TextField hote;
    
    @FXML
    private TextField port;
    
    @FXML
    private TextField password;
    
    @FXML
    private TextField user;


    @FXML
    public void initialize() {
        secondary.setShowTransitionFactory(BounceInRightTransition::new);
        secondary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        MobileApplication.getInstance().showLayer(GluonApplication.MENU_LAYER)));
               appBar.setTitleText("Connection");
               
               // add prompt text to the textfields
               hote.setPromptText(GameControlPresenter.getConnection()==null?"localhost":GameControlPresenter.getConnection().SERVER_URL);
               port.setPromptText(GameControlPresenter.getConnection()==null?"1883":GameControlPresenter.getConnection().SERVER_PORT);
               password.setPromptText(GameControlPresenter.getConnection()==null?"password":GameControlPresenter.getConnection().PASSWORD);
               user.setPromptText(GameControlPresenter.getConnection()==null?"admin":GameControlPresenter.getConnection().LOGIN);
               
               BooleanBinding binding = new BooleanBinding() {
               	{
               		super.bind(user.textProperty(), port.textProperty(), hote.textProperty(), password.textProperty());
               	}
					@Override
					protected boolean computeValue() {
						// TODO Auto-generated method stub
						return (user.getText().isEmpty() || password.getText().isEmpty() || hote.getText().isEmpty() || port.getText().isEmpty());
					}
				};
              
				Button saveBtn = MaterialDesignIcon.SAVE.button(e -> {
					{
						try {
							GameControlPresenter.setConnection(new MQTTConnector(hote.getText(), port.getText(), user.getText(), password.getText()));
							GameControlPresenter.initConnection();
							Alert alert = new Alert(AlertType.INFORMATION, "Connection is working");
						        alert.showAndWait();
						} catch (MqttException e1) {
							// TODO Auto-generated catch block
							Alert alert = new Alert(AlertType.ERROR, "connection is not working ");
					        alert.showAndWait();
							e1.printStackTrace();
						}
					}
				});
				saveBtn.disableProperty().bind(binding);
                appBar.getActionItems().add(saveBtn);
              
            }
        });
    }
}
