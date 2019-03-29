/**
 * @author Zawada Robert <ZawadaRobertDev@gmail.com>
 * @version 1.0.4
 * 
 * +Dodano minimalny rozmiar okna
 * +Dodano wprowadzanie akcji przez klikniêcie Enter w dowolnym polu wprowadzajacym
 * +Zmienino NimbusFeelAndLook na WindowsLookAndFeel
 * +Dodano mo¿liwoœæ usuniêcia kilku zaznaczonych akcji z tabeli
 * +Wstêpna implementacja zapisu i wczytania list akcji 
 * +Dodano t³umaczenie UIMenagera
 * +Zmieniono sposób dzia³ania okienek wyboru Tak/Nie aby wspó³dzia³a³ z t³umaczeniem UIMenager
 */
package swingUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
import swingUI.custom.UILocale;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;

public class ProgramFrame extends JFrame {
	
	private static JFrame frame;
	private JButton addButton;
	private JButton clearButton;
	private JButton deleteButton;
	private JButton loadButton;
	private JButton pathsButton;
	private JButton saveButton;
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
	private JTextField totalDurationField;
	private ActivityTableModel model;
	private JPanel totalDurationPane;
	private JLabel totalDurationLabel;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem exitMenuItem;
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	
	// Uruchomienie aplikacji
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
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
		UILocale.set("pl_PL");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setMinimumSize(new Dimension(600, 500));
		model = new ActivityTableModel();
		int[] columnWidth = {40, 160, 60, 100, 100, 60, 60, 60, 60, 80};
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		fileMenu = new JMenu("Plik");
		menuBar.add(fileMenu);
		
		newMenuItem = new JMenuItem("Nowy");
		newMenuItem.setIcon(new ImageIcon(ProgramFrame.class.getResource("/resources/icons8-new-file-24.png")));
		newMenuItem.addActionListener(e -> removeAllActions());
		fileMenu.add(newMenuItem);
		
		openMenuItem = new JMenuItem("Otwórz...");
		openMenuItem.setIcon(new ImageIcon(ProgramFrame.class.getResource("/resources/icons8-opened-folder-24.png")));
		openMenuItem.addActionListener(e -> {
			try {
				openFile();
			}
			catch (Exception e1) {
				BasicEvent.dialogError(frame,e1.getMessage());
			}
		});
		fileMenu.add(openMenuItem);
		
		saveMenuItem = new JMenuItem("Zapisz jako...");
		saveMenuItem.setIcon(new ImageIcon(ProgramFrame.class.getResource("/resources/icons8-save-as-24.png")));
		saveMenuItem.addActionListener(e -> {
			try {
				saveFile();
			}
			catch (Exception e1) {
				BasicEvent.dialogError(frame,e1.getMessage());
			}
		});
		fileMenu.add(saveMenuItem);
		
		fileMenu.addSeparator();
		
		exitMenuItem = new JMenuItem("Wyjœcie");
		exitMenuItem.setIcon(new ImageIcon(ProgramFrame.class.getResource("/resources/icons8-shutdown-24.png")));
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
		inputPane.setBorder(BorderFactory.createTitledBorder("Dodaj akcjê"));
		northPane.add(inputPane, BorderLayout.WEST);
		inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.Y_AXIS));
		
		idField = new GhostTextField("Id akcji");
		idField.setToolTipText("Id akcji - dowolna liczba wiêksza od 0");
		((AbstractDocument) idField.getDocument()).setDocumentFilter(new CharacterFilter("[^0-9]"));
		idField.addActionListener(e -> addToModel());
		inputPane.add(idField);
		
		nameField = new GhostTextField("Nazwa");
		nameField.setToolTipText("Nazwa akcji");
		nameField.addActionListener(e -> addToModel());
		inputPane.add(nameField);
		
		timePane = new JPanel();
		inputPane.add(timePane);
		timePane.setLayout(new BoxLayout(timePane, BoxLayout.X_AXIS));
		
		timeField = new GhostTextField("Czas trwania");
		timeField.setToolTipText("Czas trwania akcji - w okreœlonej jednostce czasu");
		((AbstractDocument) timeField.getDocument()).setDocumentFilter(new CharacterFilter("[^0-9.]"));
		timeField.addActionListener(e -> addToModel());
		timePane.add(timeField);
		
		timeSpinner = new JSpinner();
		timeSpinner.setModel(new SpinnerListModel(new String[] {"sek", "min", "godz", "dni"}));
		timeSpinner.setValue("godz");
		timeSpinner.setToolTipText("Jednostka czasu");
		timeSpinner.setEditor(new JSpinner.DefaultEditor(timeSpinner));
		timePane.add(timeSpinner);
		
		prevField = new GhostTextField("Poprzedzaj¹ce akcje");
		prevField.setToolTipText("Lista id poprzedzaj¹cych akcji, odzdzielonych przecinkami");
		((AbstractDocument) prevField.getDocument()).setDocumentFilter(new CharacterFilter("[^0-9, ]"));
		prevField.addActionListener(e -> addToModel());
		inputPane.add(prevField);
		
		addButton = new JButton("Dodaj akcjê");
		addButton.setToolTipText("Dodaj podan¹ akcjê do listy");
		addButton.addActionListener(e -> addToModel());
		inputPane.add(addButton);
		
		pathsPane = new JPanel();
		northPane.add(pathsPane, BorderLayout.CENTER);
		pathsPane.setBorder(BorderFactory.createTitledBorder("Œcie¿ki krytyczne"));
		pathsPane.setLayout(new BorderLayout(0, 0));
		
		getPathsPane = new JPanel();
		pathsPane.add(getPathsPane, BorderLayout.NORTH);
		getPathsPane.setLayout(new BoxLayout(getPathsPane, BoxLayout.X_AXIS));
		
		pathsButton = new JButton("Wyznacz scie¿ki");
		pathsButton.addActionListener(e -> printPaths());
		getPathsPane.add(pathsButton);
		
		totalDurationPane = new JPanel();
		getPathsPane.add(totalDurationPane);
		
		totalDurationLabel = new JLabel("Czas ca³kowity");
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
		
		clearButton = new JButton("Wyczyœæ listê akcji");
		clearButton.addActionListener(e -> removeAllActions());
		buttonsPane.add(clearButton, BorderLayout.EAST);
		
		deleteButton = new JButton("Usuñ zaznaczone akcje");
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
		
		totalDurationField.setText(ConvertUtil.toLegibleString(model.getTotalDuration()));
	}
		
	void removeAction() {
		int[] rows = table.getSelectedRows();
		Set<Integer> ids = Arrays.stream(rows).map(r -> Integer.parseInt(model.getValueAt(r,0).toString())).boxed().collect(Collectors.toSet());
		String idList = ids.stream().map(i -> i.toString()).collect(Collectors.joining(", "));
		
		if (BasicEvent.dialogYesNo(frame, "Czy na pewno chcesz usun¹æ akcje o id: "+idList+"?")) {
			for (int id : ids)
				model.removeId(id);
			totalDurationField.setText(ConvertUtil.toLegibleString(model.getTotalDuration()));
		}
	}
	
	void removeAllActions() {
		if (BasicEvent.dialogYesNo(frame, "Czy na pewno chcesz usun¹æ wszytskie akcje?")) {
			model.clear();
			totalDurationField.setText(ConvertUtil.toLegibleString(model.getTotalDuration()));
		}
	}
	
	CPMActivity getNewActivityFromPane() {
		//W przypadku niew³aœciwej jednostki czasu czas trwania zostanie ustalony na 0
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
			BasicEvent.dialogError(frame,"Podaj id, nazwê i czas trwania aktywnoœci.");
		else if (!Check.isNotZero(idField))
			BasicEvent.dialogError(frame,"Id nie mo¿e wynosiæ 0.");
		else if (model.getAllId().contains(Integer.parseInt(idField.getText())))
			BasicEvent.dialogError(frame,"Akcja o tym id ju¿ istnieje.");
		else if (!Check.isNotRepeted(idField, prevField))
			BasicEvent.dialogError(frame,"Id poprzedzaj¹cej aktywnoœci nie mo¿e byæ takie samo jak id wprowadzanej aktywnosci.");
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
		totalDurationField.setText(ConvertUtil.toLegibleString(model.getTotalDuration()));
	}
	
	void saveFile() throws FileNotFoundException, IOException {
		JFileChooser fileChooser = new JFileChooser();
		
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			fileChooser.getFileFilter().toString().replaceFirst(".*extensions=\\[(.*)]]", ".$1").replaceFirst(".*AcceptAllFileFilter.*", "");
			try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
				output.writeObject(model.getActivitySet());
			}
		}
	}
	
	void openFile() throws FileNotFoundException, IOException, ClassNotFoundException {
		JFileChooser fileChooser = new JFileChooser();
		
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		File file = fileChooser.getSelectedFile();
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
				
				TreeSet<CPMActivity> humanSet = (TreeSet<CPMActivity>) input.readObject();
				for (CPMActivity act : humanSet) {
					model.addActivity(act);
				}
				model.refresh();
				totalDurationField.setText(ConvertUtil.toLegibleString(model.getTotalDuration()));
			}
			catch (Exception e) {
				BasicEvent.dialogError(frame,"Wskazany plik nie zawiera poprawnej listy aktywnoœci.");
			}
		}
	}
	
}