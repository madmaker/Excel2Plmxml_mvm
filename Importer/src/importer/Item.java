package importer;

import java.util.ArrayList;
import java.util.Hashtable;

public class Item {
	
	private Hashtable<String, String> attrs = new Hashtable<String, String>();
	private ArrayList<String> occIds = new ArrayList<String>();
	private String id;
	private String productId = "";
	private String productRevId = "";
	private String productRevisionViewId = "";
	private String productInsId = "";
	
	public Item(String id){
		this.id=id;
	}
	
	public boolean setProp(String prop, String val){
		boolean res=attrs.containsKey(prop);
		attrs.put(prop,val);
		return res;
	}
	
	public String getProp(String prop){
		if (attrs.containsKey(prop)) return attrs.get(prop); 
				else return "";
	}
	
	public String getID(){ return id; }
	
	public void setProductId(int id){ productId="id"+id; }
	 
	public String getProductId(){ return productId; }
	
	public void setProductRevId(int id){ productRevId="id"+id; }
	
	public String getProductRevId(){ return productRevId; }
	
	public void setProductRevisionViewId(int id) { productRevisionViewId="id"+id; }
	 
	public String getProductRevisionViewId() { return productRevisionViewId;}
	
	public void setProductInsId(int id){ productInsId="id"+id; }
	 
	public String getProductInsId(){ return productInsId; }
	
	public void addOccId(int id){
		occIds.add(String.valueOf(id));
	}
	
	public ArrayList<String> getOccIds(){
		return occIds;
	}
}
