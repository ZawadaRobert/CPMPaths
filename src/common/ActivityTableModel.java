package common;
import java.time.Duration;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.table.DefaultTableModel;

public class ActivityTableModel extends DefaultTableModel {
	
	private TreeSet<CPMActivity> activitySet;
	
	public ActivityTableModel() {
		String[] columnNames = {"Id", "Nazwa",
								"Czas",	"Poprzedzaj¹ce",
								"Nastêpuj¹ce", "ES",
								"EF", "LS", "LF",
								"Krytczyne"};
		for (String l : columnNames)
			addColumn(l);
		activitySet = new TreeSet<CPMActivity>();
		refresh();
	}
	
	public TreeSet<Integer> getAllId() {
		TreeSet<Integer> allIdSet = new TreeSet<Integer>();
		for (CPMActivity activity : activitySet) {
			allIdSet.add(activity.getId());
		}
		return allIdSet;
	}
	
	public void addActivity(CPMActivity activity) {
		activitySet.add(activity);
	}
	
	public void removeId(Integer id) {
		activitySet.remove(activitySet.stream()
									.filter(g -> g.getId() == id)
									.findFirst().get());
		refresh();
	}
	
	public CPMActivity getActivity(int a) {
		CPMActivity activity;
		activity = activitySet
				.stream()
				.filter(p -> p.getId() == a)
				.findAny()
				.get();
		return activity;
	}
	
	public Duration getTotalDuration() {
		refresh();
		Duration total = Duration.ZERO;
		for (CPMActivity activity : activitySet) {
			if (activity.getEarlyFinish().compareTo(total) > 0) {
				total = activity.getEarlyFinish();
			}
		}
		return total;
	}
	
	public void refresh() {
		setRowCount(0);
		int b=activitySet.size();
		
		for (int a=0; a<b; a++) {	
			
			Duration totalDuration = Duration.ZERO;
			
			for (CPMActivity activity : activitySet) {
				activity.addNextActionFromList(activitySet);
				Set<Integer> prevList = activity.getPrevList();
				Duration baseES = Duration.ZERO;
				
				for (Integer i : prevList) {
					for (CPMActivity act : activitySet) {
						if (act.getEarlyFinish()!=null) {
							if ((act.getId() == i)&&(baseES.compareTo(act.getEarlyFinish()) < 0)) {
								baseES = act.getEarlyFinish();
							}
						}
					}
				}
				activity.setEarlyStart(baseES);
			}
			
			for (CPMActivity activity : activitySet) {
				if (activity.getEarlyFinish().compareTo(totalDuration) > 0) {
					totalDuration = activity.getEarlyFinish();
				}
			}
			
			for (CPMActivity activity : activitySet) {
				Set<Integer> nextList = activity.getNextList();	
				Duration baseLF = totalDuration;
				
				for (Integer i : nextList) {
					for (CPMActivity act : activitySet) {
						if (act.getLateStart()!=null) {
							if ((act.getId() == i)&&(baseLF.compareTo(act.getLateStart()) > 0)) {
								baseLF = act.getLateStart();
							}
						}
					}
				}
				activity.setLateFinish(baseLF);
				activity.calculateReserve();
			}
		}
		
		for (CPMActivity activity : activitySet) {
			addRow(activity.getArrayRow());
		}
	}
	
	public TreeSet<CPMActivity> getActivitySet() {
		return activitySet;
	}
	
	public LinkedList<String> getCryticalPathsList() {
		
		TreeSet<CPMActivity> cryticalSet = new TreeSet<CPMActivity>();
		TreeSet<Integer> cryticalIdSet = new TreeSet<Integer>();
		TreeSet<Integer> nonCryticalIdSet = new TreeSet<Integer>();
		TreeSet<Integer> startingIdSet = new TreeSet<Integer>();
		LinkedList<String> pathList;
		Graph criticalGraph = new Graph();
		
		for (CPMActivity activity : activitySet) {
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
	
	//Czyszczenie modelu i zestawu aktywnoœci
	public void clear() {
		activitySet.clear();
		refresh();
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}