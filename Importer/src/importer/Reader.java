package importer;

import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Reader {
	static File file;
	static Hashtable<String, Item> items = new Hashtable<String, Item>();
	static ArrayList<String> designations = new ArrayList<String>();
	static ArrayList<String> errors = new ArrayList<String>();
	static Vector<Item> roots=new Vector<Item>();
	private static XSSFWorkbook wb;
	private static XSSFSheet sheet;
	private static int designationColumn = -1;
	private static int nameColumn = -1;
	private static int quantityColumn = -1;
	private static int typeColumn = -1;
	private static int positionColumn = -1;
	private static boolean isConstantType = true;
	private static String rootDesignation;
	private static String rootName;
	private static Item rootItem;
	private static Hashtable<String,String> typeTable;
	
	public void setDesignationColumn(int col){
		designationColumn = col;
	}
	
	public void setNameColumn(int col){
		nameColumn = col;
	}
	
	public void setQuantityColumn(int col){
		quantityColumn = col;
	}
	
	public void setTypeColumn(int col){
		typeColumn = col;
	}
	
	public void setPosColumn(int col){
		positionColumn = col;
	}
	
	public void setRootData(String designation, String name){
		rootDesignation = designation;
		rootName = name;
	}
	
	public void setIsVariableType(){
		isConstantType = false;
	}
	
	public void setTypeTable(Hashtable<String,String> table){
		typeTable = table;
	}
	
	public Reader(){
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		Reader reader = new Reader();
		MainWindow mw;
		if (args.length>0)	mw = new MainWindow(reader, args[0]);
		else mw=new MainWindow(reader);
		mw.setTitle("������ ������������� ������");
		mw.setVisible(true);
		while (mw.isVisible()){
			Thread.sleep(1000);	
		};
		file = mw.getFile();
		mw.dispose();
		if(file != null){
			readExcel();
		}
	}
	
	ArrayList<String> getColumnNames(File file) throws Exception{
		wb = new XSSFWorkbook(new FileInputStream(file));
	    sheet = wb.getSheetAt(0);
		ArrayList<String> names = new ArrayList<String>();
		int i = 0;
		String name = "";
		while(!(name = getGeneralValue(sheet.getRow(0),i)).equals("")){
			names.add(name);
			i++;
		}
		return names;
	}
	
	static public void readExcel() {
		try {
			wb = new XSSFWorkbook(new FileInputStream(file));
		    sheet = wb.getSheetAt(0);
		    Iterator<Row> rowIterator = sheet.iterator();
		    
		    if (rowIterator.hasNext()) {
		    	Row row = rowIterator.next();
		    	rootItem = new Item(rootDesignation);
		    	rootItem.setProp("������������", rootName);
		    	rootItem.setProp("�����������", rootDesignation);
		    	rootItem.setProp("����������", "1");
		    	rootItem.setProp("���", "Pm8_CompanyPart");
		    	rootItem.setProp("������", "Assembly");
		    }
		    
		    while (rowIterator.hasNext()){
		    	Row row=rowIterator.next(); 
		    	String id = getGeneralValue(row, designationColumn).trim();
		    	Item item=items.get(id);
		    	
		    	if (item==null){
		    		item=new Item(id);
		    		items.put(id, item);
		    		String name=getGeneralValue(row, nameColumn).trim();
		    		name.replaceAll("/TA11&TA12", "");
		    		if (name.length()==0) name="null";
		    		String designation = getGeneralValue(row, designationColumn).trim();
		    		String quantity = getGeneralValue(row, quantityColumn).trim();
		    		String position = getGeneralValue(row, positionColumn).trim();
		    		
		    		item.setProp("������������", name.replaceAll("(\"|>|<)", ""));
		    		item.setProp("�����������", designation);
		    		item.setProp("����������", quantity);
		    		item.setProp("�������", position);
		    		
		    		if(!isConstantType){
		    			String type = null;
		    			String subtype = null;
			    		String rawType = typeTable.get((int)Double.parseDouble(getGeneralValue(row, typeColumn))+"");
			    		if(rawType == null || rawType.equals("")){
			    			rawType = "������";
			    		}
			    		if(rawType.equals("������")){
			    			type = "Pm8_CompanyPart";
			    			subtype = "Assembly";
			    		} else if (rawType.equals("������")) {
			    			type = "Pm8_CompanyPart";
			    			subtype = "Part";
			    		} else if (rawType.equals("��������")) {
			    			type = "Pm8_CompanyPart";
			    			subtype = "Complex";
			    		} else if (rawType.equals("��������")) {
			    			type = "Pm8_CompanyPart";
			    			subtype = "Set";
			    		} else if (rawType.equals("����������� �������")) {
			    			type = "CommercialPart";
			    		} else if (rawType.equals("�������� �������")) {
			    			type = "CommercialPart";
			    		} else if (rawType.equals("��������")) {
			    			type = "Pm8_Material";
			    		}
			    		if(type == null){
			    			type = "Pm8_CompanyPart";
			    			subtype = "Assembly";
			    		}
			    		item.setProp("���", type);
			    		if(type.equals("Pm8_CompanyPart")){
			    			item.setProp("������", subtype);
			    		}
		    		} else {
		    			item.setProp("���", "Pm8_CompanyPart");
		    			item.setProp("������", "Assembly");
		    		}
		    	}
		    }
		    if(errors.size()>0){
		    	showErrorWindow();
		    } else {
		    	new PLMXMLExport(rootItem, items,  file.getParentFile());
		    }
		} catch(Exception ioe) {
		    ioe.printStackTrace();
		}
	}
	
	public static String getGeneralValue(Row row,int cellIndex){
		if(!(row.getCell(cellIndex)==null)){
			switch (row.getCell(cellIndex).getCellType()) {
			case Cell.CELL_TYPE_STRING:
	            return row.getCell(cellIndex).getStringCellValue().trim();
			case Cell.CELL_TYPE_NUMERIC:
				return (BigDecimal.valueOf(row.getCell(cellIndex).getNumericCellValue())).toPlainString().trim();
			case Cell.CELL_TYPE_BOOLEAN:
				return ""+row.getCell(cellIndex).getBooleanCellValue();
			default :
			}
			
		}
		return "";
	}
	
	public static void showErrorWindow(){
		try{
		UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex){
			ex.printStackTrace();
		}
		JFrame errorWnd = new JFrame();
    	Container main = errorWnd.getContentPane();
		main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
		JPanel c=new JPanel();
		main.add(c);
		c.setLayout(new BoxLayout(c,BoxLayout.LINE_AXIS));
		JTextArea errorText = new JTextArea();
		c.add(new JScrollPane(errorText));
		errorText.append("������������ ����������� � ������� �� ���������� ����������:\n");
    	for(String line: errors){
    		errorText.append(line+"\n");
    	}
    	errorWnd.setSize(450, 400);
    	errorWnd.setVisible(true);
	}
	
}
