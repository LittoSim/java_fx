package com.gluonapplication;

import com.gluonapplication.views.PrimaryView;
import com.gluonapplication.views.SecondaryView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GluonApplication extends MobileApplication {

    public static final String PRIMARY_VIEW = HOME_VIEW;
    public static final String SECONDARY_VIEW = "Secondary View";
    public static final String MENU_LAYER = "Side Menu";
    
    @Override
    public void init() {
        addViewFactory(PRIMARY_VIEW, () -> new PrimaryView(PRIMARY_VIEW).getView());
        addViewFactory(SECONDARY_VIEW, () -> new SecondaryView(SECONDARY_VIEW).getView());
        
        NavigationDrawer drawer = new NavigationDrawer();
        
        NavigationDrawer.Header header = new NavigationDrawer.Header("Gluon Mobile",
                "Multi View Project",
                new Avatar(21, new Image(GluonApplication.class.getResourceAsStream("/icon.png"))));
        drawer.setHeader(header);
        
        final Item primaryItem = new Item("Primary", MaterialDesignIcon.HOME.graphic());
        final Item secondaryItem = new Item("Secondary", MaterialDesignIcon.DASHBOARD.graphic());
        drawer.getItems().addAll(primaryItem, secondaryItem);
        
        drawer.selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            hideLayer(MENU_LAYER);
            switchView(newItem.equals(primaryItem) ? PRIMARY_VIEW : SECONDARY_VIEW);
        });
        
        addLayerFactory(MENU_LAYER, () -> new SidePopupView(drawer));
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        scene.getStylesheets().add(GluonApplication.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(GluonApplication.class.getResourceAsStream("/icon.png")));
    }
}