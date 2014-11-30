

import java.awt.GridBagConstraints;

public class UtilGridBagConstraints {

	private static GridBagConstraints constraints = new GridBagConstraints();
	
	public static void setConstraints(int x, int y, int rowspan, int colspan){
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridheight = rowspan;
		constraints.gridwidth = colspan;
		constraints.weighty=1.0;
		constraints.weightx=1.0;
	}
	
	public static GridBagConstraints getConstraints(){
		return constraints;
	}
	
	public static GridBagConstraints cons(int x, int y, int rowspan, int colspan){
		
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridheight = rowspan;
		constraints.gridwidth = colspan;
		constraints.weighty=1.0;
		constraints.weightx=1.0;
		
		return constraints;
	}
	
	
}
