package swingUI.custom;

import java.awt.event.KeyEvent;
import javax.swing.UIManager;

public abstract class UILocale {
	public static void set(String s) {
		
		if (s=="pl_PL") {
			UIManager.put("FileChooser.acceptAllFileFilterText", "Wszystkie pliki");
			UIManager.put("FileChooser.cancelButtonMnemonic", ""+KeyEvent.VK_A);
			UIManager.put("FileChooser.cancelButtonText", "Anuluj");
			UIManager.put("FileChooser.cancelButtonToolTipText", "Anuluj");
			UIManager.put("FileChooser.deleteFileButtonText", "Usu� plik");
			UIManager.put("FileChooser.detailsViewActionLabelText", "Szczeg�y");
			UIManager.put("FileChooser.detailsViewButtonAccessibleName", "Szczeg�y");
			UIManager.put("FileChooser.detailsViewButtonToolTipText", "Szczeg�y");
			UIManager.put("FileChooser.directoryOpenButtonText", "Otw�rz");
			UIManager.put("FileChooser.directoryOpenButtonToolTipText", "Otw�rz");
			UIManager.put("FileChooser.fileAttrHeaderText", "Atrybuty");
			UIManager.put("FileChooser.fileDateHeaderText", "Zmodyfkowano");
			UIManager.put("FileChooser.fileNameHeaderText", "Nazwa pliku:");
			UIManager.put("FileChooser.fileNameLabelMnemonic", ""+KeyEvent.VK_N);
			UIManager.put("FileChooser.fileNameLabelText", "Nazwa pliku:");
			UIManager.put("FileChooser.fileSizeHeaderText", "Rozmiar");
			UIManager.put("FileChooser.filesOfTypeLabelMnemonic", ""+KeyEvent.VK_T);
			UIManager.put("FileChooser.filesOfTypeLabelText", "Typ plik�w:");
			UIManager.put("FileChooser.fileTypeHeaderText", "Typ pliku");
			UIManager.put("FileChooser.filterLabelText", "Typ pliku:");
			UIManager.put("FileChooser.foldersLabelText", "Foldery");
	//		UIManager.put("FileChooser.goupFolderActionLabelText", "Poziom wy�ej");
			UIManager.put("FileChooser.helpButtonText", "Pomoc");
			UIManager.put("FileChooser.helpButtonToolTipText", "Pomoc");
			UIManager.put("FileChooser.homeFolderAccessibleName", "Pulpit");
			UIManager.put("FileChooser.homeFolderToolTipText", "Pulpit");
			UIManager.put("FileChooser.listViewActionLabelText", "Lista");
			UIManager.put("FileChooser.listViewButtonAccessibleName", "Lista");
			UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
			UIManager.put("FileChooser.lookInLabelMnemonic", ""+KeyEvent.VK_S);
			UIManager.put("FileChooser.lookInLabelText", "Szukaj w:");
			UIManager.put("FileChooser.newFolderAccessibleName", "Nowy folder");
			UIManager.put("FileChooser.newFolderActionLabelText", "Nowy folder");
			UIManager.put("FileChooser.newFolderButtonText", "Nowy folder");
			UIManager.put("FileChooser.newFolderErrorText", "B��d podczas tworzenia folderu");
			UIManager.put("FileChooser.newFolderToolTipText", "Utw�rz nowy folder");
			UIManager.put("FileChooser.openButtonMnemonic", ""+KeyEvent.VK_O);
			UIManager.put("FileChooser.openButtonText", "Otw�rz");
			UIManager.put("FileChooser.openButtonToolTipText", "Otw�rz wybrany plik");
			UIManager.put("FileChooser.openDialogTitleText", "Otw�rz");
			UIManager.put("FileChooser.refreshActionLabelText", "Od�wie�");
			UIManager.put("FileChooser.renameFileButtonText", "Zmie� nazw� pliku");
			UIManager.put("FileChooser.saveButtonText", "Zapisz");
			UIManager.put("FileChooser.saveButtonToolTipText", "Zapisz");
			UIManager.put("FileChooser.saveDialogTitleText", "Zapisz");
			UIManager.put("FileChooser.saveInLabelMnemonic", ""+KeyEvent.VK_W);
			UIManager.put("FileChooser.saveInLabelText", "Zapisz w");
			UIManager.put("FileChooser.updateButtonText", "Aktualizuj");
			UIManager.put("FileChooser.updateButtonToolTipText", "Aktualizuj");
			UIManager.put("FileChooser.upFolderAccessibleName", "Poziom wy�ej");
			UIManager.put("FileChooser.upFolderToolTipText", "Poziom wy�ej");
			UIManager.put("FileChooser.viewMenuLabelText", "Widok");
			UIManager.put("OptionPane.cancelButtonText", "Anuluj");
			UIManager.put("OptionPane.cancelButtonMnemonic", ""+KeyEvent.VK_A);
			UIManager.put("OptionPane.noButtonText", "Nie");
			UIManager.put("OptionPane.noButtonMnemonic", ""+KeyEvent.VK_N);
			UIManager.put("OptionPane.yesButtonText", "Tak");
			UIManager.put("OptionPane.yesButtonMnemonic", ""+KeyEvent.VK_T);
			UIManager.put("ProgressMonitor.progressText", "Prosz� czeka�");
		}
	}
}