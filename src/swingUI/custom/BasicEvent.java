package swingUI.custom;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public final class BasicEvent {
	public static void performDialogExitFromButton(JFrame frame, AbstractButton button) {
		button.addActionListener(e -> {
			dialogExit(frame);
		});
	}
	
	public static void performDialogExitFromX(JFrame frame) {
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				dialogExit (frame);
			}
		});
	}
	
	public static void dialogExit(JFrame frame) {
		String message = "Czy na pewno chcesz zakoñczyæ dzia³anie programu?";
		int confirm = JOptionPane.showConfirmDialog(null, message, "PotwierdŸ", JOptionPane.YES_NO_OPTION);
		if (confirm == 0) System.exit(0);
	}
	
	public static boolean dialogYesNo(JFrame frame, String message) {
		int confirm = JOptionPane.showConfirmDialog(null, message, "PotwierdŸ", JOptionPane.YES_NO_OPTION);
		return (confirm == 0);
	}
	
	public static void dialogInformation(JFrame frame, String message) {
		String title = "Informacja";
		int type = JOptionPane.INFORMATION_MESSAGE;
		JOptionPane.showMessageDialog(frame, message, title, type);
	}
	
	public static void dialogError(JFrame frame, String message) {
		String title = "B³¹d";
		int type = JOptionPane.ERROR_MESSAGE;
		JOptionPane.showMessageDialog(frame, message, title, type);
	}
}