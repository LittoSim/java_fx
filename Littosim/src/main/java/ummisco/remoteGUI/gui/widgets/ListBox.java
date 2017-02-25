package ummisco.remoteGUI.gui.widgets;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.gluonhq.charm.glisten.control.CharmListCell;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.control.ListTile;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import ummisco.remoteGUI.driver.common.FollowedVariable;
import ummisco.remoteGUI.driver.network.MQTTConnector;

public class ListBox extends CharmListView implements Observer {
	
	ListBoxCell<ListItem> data;
	private String agentName;
	private String titleVar = "title" ;
	private String dataVar = "mfile" ;
	private String agentBox="listView";
	
	private FollowedVariable  flVariable ;
	private ObservableList<ListItem>  all_data;
	MQTTConnector connection;
	
	class ListBoxCell<T extends ListItem> extends CharmListCell<T>
	{
		public ListBoxCell() {
			super();
		}
		@Override 
		public void updateItem(T item, boolean empty) {
	        super.updateItem(item, empty); 
	        if(item != null && !empty){
	        	 ListTile tile = new ListTile();
	             tile.textProperty().addAll(item.getTitle(),item.getSubTitle());
	             setText(null);
	             setGraphic(tile);  	
	        }
			else {
	            setText(null);
	            setGraphic(null);
	        }
		}
	}
	
	public ListBox() {
		super();
		all_data = FXCollections.observableArrayList();
		this.setItems(all_data);
    	this.setCellFactory(p->new ListBoxCell<ListItem>());
    	skinProperty().addListener((obs, ov, nv) -> {
            for (Node node : getChildrenUnmodifiable()) {
                if (node instanceof ListView) {
                    ((ListView)node).getSelectionModel().selectedItemProperty().addListener(
                            (obs2, ov2, nv2) -> 
                            {
                            	if(nv2!=null)
                            	{
                            		fireEvent(new ValueChangedEvent(agentName,agentBox,((ListItem)nv2).getTitle()));
                                 		System.out.println("choix " + nv2);
                            	}
                            });
                }
            }
        });
    	askUpdate();
    //	this.setEventHandler(eventType, eventHandler);getSelectionModel().selectedItemProperty().addListener(
    //		    (obs, ov, nv) -> System.out.println("Selected: " + nv));
	}

    private  EventHandler<? super ValueChangedEvent> changedEventHandler = null;

    public final void setOnValueChanged(EventHandler<? super ValueChangedEvent> value) 
    {
    	this.addEventHandler(ValueChangedEvent.VALUE_CHANGED, value);
        changedEventHandler = value;
    }
    
    public final  EventHandler<? super ValueChangedEvent> getOnValueChanged() {
        return changedEventHandler;

    }
	
	
    public void askUpdate()
    {
    	fireEvent(new ValueChangedEvent(agentName,agentBox,"UPDATE"));
    }
    
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof FollowedVariable)
		{
			FollowedVariable f = (FollowedVariable)o;
			List<Map<String,Object>> datas = f.popLastData();
			
			Map<String,Object> mdt = datas.get(datas.size()-1);
			Runnable rn = new Runnable() {

				@Override
				public void run() {
					updateData((List<String>)mdt.get(titleVar),(List<String>)mdt.get(dataVar));
				}
			};
			Platform.runLater(rn);
			
		}
	}
	
	private  void updateData(List<String> title, List<String> subTitle)
	{
		all_data.clear();
		for(int i = 0 ; i<title.size();i++)
		{
			ListItem tmp = new ListItem(title.get(i),subTitle.get(i));
			all_data.add((ListItem )tmp);
		}
		setItems(all_data);    	
	}
	public void setAgentName(String lbl)
	{
		this.agentName= lbl;
	}
	
	public String getAgentName()
	{
		return this.agentName;
	}

	public void setFollow(String lbl)
	{
		this.flVariable = new FollowedVariable(lbl);
		this.flVariable.addObserver(this);
	}

	public String getFollow()
	{
		return flVariable.getName();
	}
	
	
	public void setTitleVar(String lbl)
	{
		this.flVariable = new FollowedVariable(lbl);
		this.flVariable.addObserver(this);
	}

	public String getTitleVar()
	{
		return flVariable.getName();
	}
	
	public void setDataVar(String lbl)
	{
		this.flVariable = new FollowedVariable(lbl);
		this.flVariable.addObserver(this);
	}

	public String getDataVar()
	{
		return flVariable.getName();
	}
	
	public void setAgentBox(String n)
	{
		this.agentBox = n;
	}

	public String getAgentBox()
	{
		return this.agentBox ;
	}

	public void registerConnection(MQTTConnector con)
	{
		con.registerVariable(this.flVariable);
		this.flVariable.addObserver(this);
		this.connection = con;
	}	
}
