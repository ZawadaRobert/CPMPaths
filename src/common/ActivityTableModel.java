package common;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class ActivityTableModel extends AbstractTableModel {
	
	private ArrayList<String> headers = new ArrayList<String>( 
			Arrays.asList("Id", "Nazwa",
						  "Czas", "Poprzedzaj¹ce",
						  "Nastêpuj¹ce", "ES",
						  "EF", "LS", "LF",
						  "Krytczyne")); 
	
	private ArrayList<CPMActivity> data;
	
	public ActivityTableModel() {
		data = new ArrayList<CPMActivity>();
	}
	
	public TreeSet<Integer> getAllId() {
		TreeSet<Integer> allIdSet = new TreeSet<Integer>();
		for (CPMActivity activity : data)
			allIdSet.add(activity.getId());
		return allIdSet;
	}
	
	public boolean isCellEditable(int row, int col) {
		if (col==1)
			return true;
		else
			return false;
	}
	
	public void addRow(CPMActivity activity) {
		data.add(activity);
		fireTableRowsInserted(1,0);
	}
	
	public void removeId(Integer id) {
		data.removeIf(e -> e.isId(id));
		fireTableDataChanged();
	}
	
	public CPMActivity getActivity(int a) {
		return  data.stream()
					.filter(e -> e.isId(a))
					.findAny()
					.get();
	}
	
	public Duration getTotalDuration() {
		Duration total = Duration.ZERO;
		for (CPMActivity activity : data) {
			if (activity.getEarlyFinish().compareTo(total) > 0) {
				total = activity.getEarlyFinish();
			}
		}
		return total;
	}
	
	public ArrayList<CPMActivity> getData() {
		return data;
	}
	
	public LinkedList<String> getCryticalPathsList() {
		TreeSet<CPMActivity> cryticalSet = new TreeSet<CPMActivity>();
		TreeSet<Integer> cryticalIdSet = new TreeSet<Integer>();
		TreeSet<Integer> nonCryticalIdSet = new TreeSet<Integer>();
		TreeSet<Integer> startingIdSet = new TreeSet<Integer>();
		LinkedList<String> pathList;
		Graph criticalGraph = new Graph();
		
		for (CPMActivity activity : data) {
			if (activity.isCrytical()) {
				cryticalSet.add(activity);
				cryticalIdSet.add(activity.getId());
				if (activity.isStart()) {
					startingIdSet.add(activity.getId());
				}
			}
		}
		
		nonCryticalIdSet = getAllId();
		nonCryticalIdSet.removeAll(cryticalIdSet);
		
		for (CPMActivity activity : cryticalSet) {
			for (Integer id : nonCryticalIdSet) {
				activity.removeFromPrevList(id);
				activity.removeFromNextList(id);
			}
			for (Integer nextId : activity.getNextList())
				criticalGraph.addEdge(activity.getId(), nextId);
		}
		
		pathList = criticalGraph.getPathStartEnd();
		return pathList;
	}
	
	public void calculate() {
		// Obliczanie czasów EarlyStart i LateFinish, trzeba poprawiæ wydajnoœæ
		for (int a=0; a<data.size(); a++) {	
			
			Duration totalDuration = Duration.ZERO;
			
			for (CPMActivity activity : data) {
				activity.addNextActionFromList(data);
				Set<Integer> prevList = activity.getPrevList();
				Duration baseES = Duration.ZERO;
				
				for (Integer i : prevList) {
					for (CPMActivity act : data) {
						if (act.getEarlyFinish()!=null) {
							if ((act.getId() == i)&&(baseES.compareTo(act.getEarlyFinish()) < 0)) {
								baseES = act.getEarlyFinish();
							}
						}
					}
				}
				activity.setEarlyStart(baseES);
			}
			
			for (CPMActivity activity : data) {
				if (activity.getEarlyFinish().compareTo(totalDuration) > 0) {
					totalDuration = activity.getEarlyFinish();
				}
			}
			
			for (CPMActivity activity : data) {
				Set<Integer> nextList = activity.getNextList();	
				Duration baseLF = totalDuration;
				
				for (Integer i : nextList) {
					for (CPMActivity act : data) {
						if (act.getLateStart()!=null) {
							if ((act.getId() == i)&&(baseLF.compareTo(act.getLateStart()) > 0)) {
								baseLF = act.getLateStart();
							}
						}
					}
				}
				activity.setLateFinish(baseLF);
			}
		}
	}
	
	public void clear() {
		data.clear();
		fireTableDataChanged();
	}
	
	@Override
	public String getColumnName(int col) {
		return headers.get(col);
	}
	
	@Override
	public int getColumnCount() {
		return headers.size();
	}
	
	@Override
	public int getRowCount() {
		int size;
		if (data == null)
			size = 0;
		else
			size = data.size();
		return size;
	}
	
	@Override	
	public Class getColumnClass(int col) {
		if (col == 0)
			return Integer.class;
		else if (col == 1)
			return String.class;
		else if (col == 3)
			return TreeSet.class;
		else if (col == 4)
			return TreeSet.class;
		else
			return Duration.class;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		Object temp = null;
		if (col == 0)
			temp = data.get(row).getId();
		else if (col == 1)
			temp = data.get(row).getName();
		else if (col == 2)
			temp = data.get(row).getTime();
		else if (col == 3)
			temp = data.get(row).getPrevList();
		else if (col== 4)
			temp = data.get(row).getNextList();
		else if (col== 5)
			temp = data.get(row).getEarlyStart();
		else if (col == 6)
			temp = data.get(row).getEarlyFinish();
		else if (col == 7)
			temp = data.get(row).getLateStart();
		else if (col == 8)
			temp = data.get(row).getLateFinish();
		return temp;
	}
	
	@Override
	public void setValueAt(Object value, int row, int column) {
		if (column==1)
			data.get(row).setName(value.toString());
	}
}