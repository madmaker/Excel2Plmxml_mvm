package importer;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -5216703372991698138L;
	
	private static JTextField filename=new JTextField(30);
	private static JTextField rootDesignation=new JTextField(10);
	private static JTextField rootName=new JTextField();
	private static JComboBox<String> compName=new JComboBox<String>();
	private static JComboBox<String> compDesignation=new JComboBox<String>();
	private static JComboBox<String> compQuantity = new JComboBox<String>();
	private static JComboBox<String> compPosition = new JComboBox<String>();
	private static JRadioButton constant=new JRadioButton("Постоянный");
	private static JRadioButton variable=new JRadioButton("Зависимый");
	private static JComboBox<String> compType = new JComboBox<String>();
	private static JTable typeTable;
	//private static 
	private static JButton export=new JButton("Export 2 PLMXML");
	final JFileChooser fc = new JFileChooser("C:\\");
	MainWindow wnd;
	Reader reader;
	
	private String targetFile="";
	private File file = null;
	
	
	/**
	 * @wbp.parser.constructor
	 */
	public MainWindow(Reader reader){
		wnd = this;
		this.reader = reader;
		makeshape();
	}
	
	public MainWindow(Reader reader, String file){
		this(reader);
		targetFile=file;
		filename.setText(file);
		makeshape();
	}

	private void makeshape(){
		Container pane=this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		JPanel rootData=new JPanel();
		rootData.setLayout(new GridBagLayout());
		rootData.setBorder(BorderFactory.createTitledBorder(""));
		rootData.add(new JLabel("Файл: "), new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5, 5, 5, 5),0,0));
		rootData.add(filename, new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5),0,0));
		JButton filedialog=new JButton("...");
		rootData.add(filedialog, new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(5, 5, 5, 5),0,0));
		
		JPanel typeData=new JPanel();
		typeData.setLayout(new GridBagLayout());
		typeData.setBorder(BorderFactory.createTitledBorder("Корневая сборка"));;
		typeData.add(new JLabel("Обозначание: "), new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5, 5, 5, 5),0,0));
		typeData.add(rootDesignation, new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5),0,0));
		typeData.add(new JLabel("Наименование: "), new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.WEST,new Insets(5, 5, 5, 5),0,0));
		typeData.add(rootName, new GridBagConstraints(1,1,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5),0,0));
		
		// mapping panel
		JPanel mappingPanel=new JPanel();
		mappingPanel.setBorder(BorderFactory.createTitledBorder("Вхождения"));;
		GridBagLayout gbl_mappingPanel = new GridBagLayout();
		gbl_mappingPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0};
		mappingPanel.setLayout(gbl_mappingPanel);
		
		JPanel component=new JPanel();
		component.setBorder(BorderFactory.createTitledBorder(""));
		component.setLayout(new GridBagLayout());
		
		mappingPanel.add(new JLabel("Обозначение"), new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5, 5, 5, 5),0,0));
		mappingPanel.add(compDesignation, new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5),0,0));
		String[] columnNames = {"Тип Teamcenter", "Тип 1С"};
		Object[][] data = {{"Сборка", "3"}, {"Деталь", "4"}, {"Комплект", "8"}, {"Комплекс", "2"}, {"Покупное изделие", "6"}, {"Стандартное изделие", "5"}, {"Материал", "7"}};
		variable.setSelected(true);
		mappingPanel.add(constant, new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5, 5, 5, 5),0,0));
		
		constant.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				compType.setEnabled(false);
				typeTable.setEnabled(false);
			}
		});
		mappingPanel.add(variable, new GridBagConstraints(3,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5, 5, 5, 5),0,0));
		
		variable.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				compType.setEnabled(true);
				typeTable.setEnabled(true);
			}
		});
		mappingPanel.add(new JLabel("Наименование"), new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5, 5, 5, 5),0,0));
		mappingPanel.add(compName, new GridBagConstraints(1,1,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5),0,0));
		typeTable = new JTable(data, columnNames);
		compType.setEnabled(true);
		typeTable.setEnabled(true);
		JScrollPane scrollPane = new JScrollPane(typeTable);
		typeTable.setFillsViewportHeight(true);
		mappingPanel.add(scrollPane, new GridBagConstraints(2,1,2,4,1,1,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5, 5, 0, 5),0,0));
		mappingPanel.add(new JLabel("Количество"), new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5, 5, 5, 5),0,0));
		mappingPanel.add(compQuantity, new GridBagConstraints(1,2,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5),0,0));
		
		ButtonGroup group=new ButtonGroup();
		group.add(constant);
		group.add(variable);
		mappingPanel.add(new JLabel("Тип объекта"), new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5, 5, 0, 5),0,0));
		mappingPanel.add(new JLabel("Позиция"), new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5, 5, 0, 5),0,0));
		mappingPanel.add(compType, new GridBagConstraints(1,3,1,1,1,1,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5, 5, 0, 5),0,0));
		mappingPanel.add(compPosition, new GridBagConstraints(1,4,1,1,1,1,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5, 5, 0, 5),0,0));
		
		pane.add(rootData,new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5),0,0));
		pane.add(typeData,new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,new Insets(5, 5, 5, 5),0,0));
		pane.add(mappingPanel,new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(5, 5, 5, 5),0,0));
		
		
		
		
		pane.add(export,new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(5, 5, 5, 5),0,0));
		
		filedialog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (fc.showDialog(null, "ОК")==JFileChooser.APPROVE_OPTION){
					filename.setText(fc.getSelectedFile().getAbsolutePath());
					try{
						ArrayList<String> columnNames = reader.getColumnNames(new File(filename.getText()));
						for(String name: columnNames){
							compDesignation.addItem(name);
							compName.addItem(name);
							compQuantity.addItem(name);
							compType.addItem(name);
							compPosition.addItem(name);
						}
					} catch (Exception ex){
						ex.printStackTrace();
						JOptionPane.showMessageDialog(wnd, "Проблема с чтением из файла!\nУбедитесь что выбран корректный файл для импорта.");
						filename.setText("");
					}
				}
			}
		});
		
		/*** DEBUG ***/
		/*try{
			filename.setText("F:\\_development\\eclipse_java\\work_workspace\\Importer\\export3.xlsx");
			ArrayList<String> columnNames1 = reader.getColumnNames(new File("F:\\_development\\eclipse_java\\work_workspace\\Importer\\export3.xlsx"));
			for(String name: columnNames1){
				compDesignation.addItem(name);
				compName.addItem(name);
				compQuantity.addItem(name);
				compType.addItem(name);
				compPosition.addItem(name);
			} 
		} catch (Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(wnd, "Проблема с чтением из файла!\nУбедитесь что выбран корректный файл для импорта.");
			filename.setText("");
		}*/
		/*************/
		
		export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				file=new File(filename.getText());
				if(validateInput()){
					reader.setDesignationColumn(compDesignation.getSelectedIndex());
					reader.setNameColumn(compName.getSelectedIndex());
					reader.setQuantityColumn(compQuantity.getSelectedIndex());
					reader.setTypeColumn(compType.getSelectedIndex());
					reader.setPosColumn(compPosition.getSelectedIndex());
					reader.setRootData(rootDesignation.getText(), rootName.getText());
					if(variable.isSelected()){
						reader.setIsVariableType();
						Hashtable<String, String> typeHashTable = new Hashtable<String, String>();
						for(int i =0; i<typeTable.getRowCount(); i++){
							for(String tcType: typeTable.getModel().getValueAt(i, 1).toString().split(",")){
								typeHashTable.put(tcType.trim(), typeTable.getModel().getValueAt(i, 0).toString());
							}
						}
						reader.setTypeTable(typeHashTable);
					}
					wnd.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(wnd, "Одна или более настроек импорта не верны!\nПроверьте что все настройки импорта заданы корректно.");
				}
			}
		}); 
		
		this.pack();
		this.setSize(600, 420);
		this.setLocationByPlatform(true);
	}
	
	boolean validateInput(){
		boolean flag = false;
		
		if(file==null) flag = true;
		if(rootDesignation.getText().length()<=0) flag = true;
		if(rootName.getText().length()<=0) flag = true;
		if(compDesignation.getSelectedIndex()==-1) flag = true;
		if(compName.getSelectedIndex()==-1) flag = true;
		if(compQuantity.getSelectedIndex()==-1) flag = true;
		if(compType.getSelectedIndex()==-1 && variable.isSelected()) flag = true;
		
		return !flag;
	}
	
	public File getFile(){
		 return file;
	}
	
}


