/**
 * @author Zawada Robert <ZawadaRobertDev@gmail.com>
 * @version 1.0.0
 */
package swingUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import common.ActivityTableModel;
import common.CPMActivity;
import common.ConvertUtil;
import swingUI.custom.BasicEvent;
import swingUI.custom.CharacterFilter;
import swingUI.custom.Check;
import swingUI.custom.GhostTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ProgramFrame extends JFrame {
	
	private static JFrame frame;
	private JButton addButton;
	private JButton clearButton;
	private JButton deleteButton;
	private JButton pathsButton;
	private JPanel buttonsPane;
	private JPanel centerPane;
	private JPanel contentPane;
	private JPanel getPathsPane;
	private JPanel inputPane;
	private JPanel northPane;
	private JPanel pathsPane;
	private JPanel tablePane;
	private JPanel timePane;
	private JSpinner timeSpinner;
	private JScrollPane pathsScroll;
	private JScrollPane tableScroll;
	private JTable table;
	private JTextArea pathsTxtArea;
	private JTextField idField;
	private JTextField nameField;
	private JTextField prevField;
	private JTextField timeField;
	private ActivityTableModel model;
	private JPanel totalDurationPane;
	private JTextField totalDurationField;
	private JLabel totalDurationLabel;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem exitMenuItem;
	
	// Uruchomienie aplikacji
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					frame = new ProgramFrame();
					frame.setLocationRelativeTo(null);
					BasicEvent.performDialogExitFromX(frame);
					frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Okienko programu
	public ProgramFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		model = new ActivityTableModel();
		int[] columnWidth = {40, 160, 60, 100, 100, 60, 60, 60, 60, 80};
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		fileMenu = new JMenu("Plik");
		menuBar.add(fileMenu);
		
		exitMenuItem = new JMenuItem("Wyj�cie");
		exitMenuItem.addActionListener(e -> BasicEvent.dialogExit(frame));
		fileMenu.add(exitMenuItem);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		northPane = new JPanel();
		contentPane.add(northPane, BorderLayout.NORTH);
		northPane.setLayout(new BorderLayout(0, 0));
		
		inputPane = new JPanel();
		inputPane.setBorder(BorderFactory.createTitledBorder("Dodaj akcj�"));
		northPane.add(inputPane, BorderLayout.WEST);
		inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.Y_AXIS));
		
		idField = new GhostTextField("Id akcji");
		idField.setToolTipText("Id akcji - dowolna liczba wi�ksza od 0");
		((AbstractDocument) idField.getDocument()).setDocumentFilter(new CharacterFilter("[^0-9]"));
		inputPane.add(idField);
		
		nameField = new GhostTextField("Nazwa");
		nameField.setToolTipText("Nazwa akcji");
		inputPane.add(nameField);
		
		timePane = new JPanel();
		inputPane.add(timePane);
		timePane.setLayout(new BoxLayout(timePane, BoxLayout.X_AXIS));
		
		timeField = new GhostTextField("Czas trwania");
		timeField.setToolTipText("Czas trwania akcji - w okre�lonej jednostce czasu");
		((AbstractDocument) timeField.getDocument()).setDocumentFilter(new CharacterFilter("[^0-9.]"));
		timePane.add(timeField);
		
		timeSpinner = new JSpinner();
		timeSpinner.setModel(new SpinnerListModel(new String[] {"sek", "min", "godz", "dni"}));
		timeSpinner.setValue("godz");
		timeSpinner.setToolTipText("Jednostka czasu");
		timeSpinner.setEditor(new JSpinner.DefaultEditor(timeSpinner));
		timePane.add(timeSpinner);
		
		prevField = new GhostTextField("Poprzedzaj�ce akcje");
		prevField.setToolTipText("Lista id poprzedzaj�cych akcji, odzdzielonych przecinkami");
		((AbstractDocument) prevField.getDocument()).setDocumentFilter(new CharacterFilter("[^0-9, ]"));
		inputPane.add(prevField);
		
		addButton = new JButton("Dodaj akcj�");
		addButton.setToolTipText("Dodaj podan� akcj� do listy");
		addButton.addActionListener(e -> addToModel());
		inputPane.add(addButton);
		
		pathsPane = new JPanel();
		northPane.add(pathsPane, BorderLayout.CENTER);
		pathsPane.setBorder(BorderFactory.createTitledBorder("�cie�ki krytyczne"));
		pathsPane.setLayout(new BorderLayout(0, 0));
		
		getPathsPane = new JPanel();
		pathsPane.add(getPathsPane, BorderLayout.NORTH);
		getPathsPane.setLayout(new BoxLayout(getPathsPane, BoxLayout.X_AXIS));
		
		pathsButton = new JButton("Wyznacz scie�ki");
		pathsButton.addActionListener(e -> printPaths());
		getPathsPane.add(pathsButton);
		
		totalDurationPane = new JPanel();
		getPathsPane.add(totalDurationPane);
		
		totalDurationLabel = new JLabel("Czas ca�kowity");
		totalDurationPane.add(totalDurationLabel);
		
		totalDurationField = new JTextField();
		totalDurationField.setEditable(false);
		totalDurationPane.add(totalDurationField);
		totalDurationField.setColumns(5);
		
		pathsTxtArea = new JTextArea();
		pathsPane.add(pathsTxtArea, BorderLayout.CENTER);
		pathsTxtArea.setEditable(false);
		pathsScroll = new JScrollPane(pathsTxtArea);
		pathsPane.add(pathsScroll);
		
		centerPane = new JPanel();
		contentPane.add(centerPane, BorderLayout.CENTER);
		centerPane.setLayout(new BorderLayout(0, 0));
		
		buttonsPane = new JPanel();
		centerPane.add(buttonsPane, BorderLayout.SOUTH);
		buttonsPane.setLayout(new BorderLayout(0, 0));
		
		clearButton = new JButton("Wyczy�� list� akcji");
		clearButton.addActionListener(e -> removeAllActions());
		buttonsPane.add(clearButton, BorderLayout.EAST);
		
		deleteButton = new JButton("Usu� zaznaczon� akcj�");
		deleteButton.addActionListener(e -> removeAction());
		buttonsPane.add(deleteButton, BorderLayout.WEST);
		
		tablePane = new JPanel();
		tablePane.setBorder(BorderFactory.createTitledBorder("Lista akcji"));
		centerPane.add(tablePane, BorderLayout.CENTER);
		tablePane.setLayout(new BorderLayout(0, 0));
		
		table = new JTable(model);
		for (int i=0 ; i<columnWidth.length ; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(columnWidth[i]);
		}
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_DELETE && !event.isShiftDown())
					removeAction();
				if (event.getKeyCode() == KeyEvent.VK_DELETE && event.isShiftDown())
					removeAllActions();
			}
		});
		tableScroll = new JScrollPane(table);
		tablePane.add(tableScroll);
		table.setBackground(SystemColor.text);
		
	}
	
	void removeAction() {
		int row = table.getSelectedRow();
		if (row != -1) {
			int id = Integer.parseInt(model.getValueAt(row,0).toString());
			if (BasicEvent.dialogYesNo(frame, "Czy na pewno chcesz usun�� akcj� o id "+id+"?")) {
				model.removeId(id);
				totalDurationField.setText(ConvertUtil.toLegibleString(model.getTotalDuration()));
			}
		}
	}
	
	void removeAllActions() {
		if (BasicEvent.dialogYesNo(frame, "Czy na pewno chcesz usun�� wszytskie akcje?")) {
			model.clear();
			totalDurationField.setText(ConvertUtil.toLegibleString(model.getTotalDuration()));
		}
	}
	
	CPMActivity getNewActivityFromPane() {
		//W przypadku niew�a�ciwej jednostki czasu czas trwania zostanie ustalony na 0
		Duration time = Duration.ofHours(0);
		String t = timeSpinner.getValue().toString();
		
		if(t.equals("sek"))
			time=ConvertUtil.toSeconds(timeField.getText());
		else if(t.equals("min"))
			time=ConvertUtil.toMinutes(timeField.getText());
		else if(t.equals("godz"))
			time=ConvertUtil.toHours(timeField.getText());
		else if(t.equals("dni"))
			time=ConvertUtil.toDays(timeField.getText());
		
		CPMActivity newActivity = new CPMActivity(Integer.parseInt(idField.getText()),nameField.getText(),time);
		newActivity.addPrevActionFromString(prevField.getText());
		
		return newActivity;
	}
	
	void addToModel() {
		if (!Check.areFilledsFilled(new JTextField[] {idField, nameField, timeField}))
			BasicEvent.dialogError(frame,"Podaj id, nazw� i czas trwania aktywno�ci.");
		else if (!Check.isNotZero(idField))
			BasicEvent.dialogError(frame,"Id nie mo�e wynosi� 0.");
		else if (model.getAllId().contains(Integer.parseInt(idField.getText())))
			BasicEvent.dialogError(frame,"Akcja o tym id ju� istnieje.");
		else if (!Check.isNotRepeted(idField, prevField))
			BasicEvent.dialogError(frame,"Id poprzedzaj�cej aktywno�ci nie mo�e by� takie samo jak id wprowadzanej aktywnosci.");
		else {
			CPMActivity newActivity = getNewActivityFromPane();
			model.addActivity(newActivity);
			model.refresh();
		}
		totalDurationField.setText(ConvertUtil.toLegibleString(model.getTotalDuration()));
	}
	
	void printPaths() {
		String output = String.join("\r\n",model.getCryticalPathsList());
		pathsTxtArea.setText(output);
	}
	
}