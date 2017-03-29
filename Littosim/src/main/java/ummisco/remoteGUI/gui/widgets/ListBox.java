package ummisco.remoteGUI.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import com.gluonhq.charm.glisten.control.CharmListCell;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.control.ListTile;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import ummisco.remoteGUI.driver.common.FollowedVariable;
import ummisco.remoteGUI.driver.network.MQTTConnector;

public class ListBox extends CharmListView<ListItem, ListItem> implements Observer {
	
	ListBoxCell<ListItem> data;
	private String agentName;
	private String titleVar = "mtitle" ;
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
    	EventHandler<MouseEvent> ev = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				System.out.println(event);
				
			}

		};
		this.setOnMouseClicked(ev);
    	skinProperty().addListener((obs, ov, nv) -> {
            for (Node node : getChildrenUnmodifiable()) {
                if (node instanceof ListView) {
                    ((ListView)node).getSelectionModel().selectedItemProperty().addListener(
                            (obs2, ov2, nv2) -> 
                            {
                            	if(nv2!=null)
                            	{
                            		fireEvent(new ValueChangedEvent(agentName,agentBox,((ListItem)nv2).getTitle()));
                            	}
                            });
                }
            }
        });
    
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
    	//fireEvent(new ValueChangedEvent(agentName,agentBox,"UPDATE"));
    }
    
    int oldSize = -1;
    
	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof FollowedVariable)
		{
			
			FollowedVariable f = (FollowedVariable)o;
			List<Map<String,Object>> datas = f.popLastData();
			
			
			Map<String,Object> mdt = datas.get(datas.size()-1);
			
			ArrayList<String > title =(ArrayList<String >) mdt.get(titleVar);
			ArrayList<String > SubDir = (ArrayList<String >) mdt.get(dataVar);
			
			
			
			List<String > displayedTitle = this.getTileInDisplay();
			
			Runnable rn = new Runnable() {
				@Override
				public void run() {
					int i = 0;
					for(String mky:title)
					{
						if(!displayedTitle.contains(mky))
						{
							//Syst
							all_data.add(new ListItem(mky,(String)SubDir.get(i)));
						}
						i = i + 1;
					}
					}
			};
			Platform.runLater(rn);
		}
			
	/*			Runnable rn = new Runnable() {
					@Override
					public void run() {
						System.out.println("Application FollowedVariable" +mdt);
						
						updateData((List<String>)mdt.get(titleVar),(List<String>)mdt.get(dataVar));
					}
				};
				Platform.runLater(rn);
			}
			*/
		
	}
	
	ArrayList<String> getTileInDisplay()
	{
		ArrayList<String> res = new ArrayList<>() ;
		for(ListItem l:this.all_data)
		{
			res.add(l.getTitle());
		}
		return res;
	}
	
		private  void updateData(List<String> title, List<String> subTitle)
	{
		all_data.clear();
		//all_data.add(e)
		System.out.println("fdsfd" + all_data+" " + title);
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
