package swingUI.custom;

import java.io.File;

public final class FileUtil {
	
	public static String getExtension(File file) {
		String name = file.getName();
		int perioidIndex = name.lastIndexOf(".");
		if (perioidIndex == -1)
			return "";
		return name.substring(perioidIndex+1).toLowerCase();
	}
	
	public static String getBaseName(File file) {
		String name = file.getName();
		int perioidIndex = name.lastIndexOf(".");
		if (perioidIndex == -1)
			return name;
		return name.substring (0, perioidIndex);
	}
}