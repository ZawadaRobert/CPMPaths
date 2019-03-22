package swingUI.custom;

import java.util.List;

import javax.swing.text.JTextComponent;

import common.ConvertUtil;

public abstract class Check {
	
	public static boolean isEmpty(JTextComponent text) {
		return text.getText().equals("");
	}

	public static boolean isNotZero(JTextComponent text) {
		return Integer.parseInt(text.getText())!=0;
	}

	public static boolean areFilledsFilled(JTextComponent[] text) {
		for(JTextComponent t : text) {
			if(t.getText().isEmpty())
				return false;
		}
		return true;
	}
	
	public static boolean isNotRepeted(JTextComponent text, JTextComponent textList) {
		if (isEmpty(textList))
			return true;
		else {
			List<Integer> list = ConvertUtil.toIntegerList(textList.getText(),',');
			return !list.contains(Integer.parseInt(text.getText()));
		}
	}
}
