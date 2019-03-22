package common;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class CPMActivity implements Comparable<CPMActivity>, Serializable {	
	private static final long serialVersionUID = 8297224384371996552L;
	private Integer id;
	private String name;
	private Duration time, earlyStart, lateStart, earlyFinish, lateFinish, reserve;
	private boolean crytical;
	private Set<Integer> prevList, nextList;
	
	public CPMActivity (Integer id, String name, Duration time) {
		setId(id);
		setName(name);
		setTime(time);
		prevList = new TreeSet<Integer>();
		nextList = new TreeSet<Integer>();
	}
	
	public void addPrevAction (Integer z) {
		if (z!=this.id)
			this.prevList.add(z);
	}
	
	public void removePrevAction (Integer z) {
		this.prevList.remove(z);
	}
	
	public static boolean isValidActivity (Integer newId, String prevString) {
		if (!prevString.isEmpty()) {
			List<Integer> list = ConvertUtil.toIntegerList(prevString,',');
			return !list.contains(newId);
		}
		return true;
	}
	
	public void addPrevActionFromString (String prevString) {
		if (!prevString.isEmpty()) {
			List<Integer> list = ConvertUtil.toIntegerList(prevString,',');	
			if (!list.contains(this.id))
				this.prevList.addAll(list);
		}
	}
	
	public void addNextActionFromIndex (Integer newId, Set<Integer> potentialNextList) {
		for(Integer x : potentialNextList) {
			if (x==id)
				this.nextList.add(newId);
		}
	}
	
	public void addNextActionFromList (Set<CPMActivity> list) {
		for(CPMActivity activity : list) {
			this.addNextActionFromIndex(activity.getId(),activity.getPrevList());
		}
	}
	
	public void calculateReserve () {
		reserve=lateStart.minus(earlyStart);
		if (reserve.isZero())
			crytical=true;
		else
			crytical=false;
	}
	
	public Object[] getArrayRow() {
		Object[] array = {
				getId(),
				getName(),
				ConvertUtil.toLegibleString(getTime()),
				getPrevList(),
				getNextList(),
				ConvertUtil.toLegibleString(getEarlyStart()),
				ConvertUtil.toLegibleString(getEarlyFinish()),
				ConvertUtil.toLegibleString(getLateStart()),
				ConvertUtil.toLegibleString(getLateFinish()),
				isCrytical()
				};
		return array;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Duration getTime() {
		return time;
	}
	
	public void setTime(Duration time) {
		this.time = time;
	}
	
	public Duration getEarlyStart() {
		return earlyStart;
	}
	
	public void setEarlyStart(Duration earlyStart) {
		this.earlyStart = earlyStart;
		earlyFinish=earlyStart.plus(time);
	}
	
	public Duration getLateStart() {
		return lateStart;
	}
	
	public Duration getEarlyFinish() {
		return earlyFinish;
	}
	
	public Duration getLateFinish() {
		return lateFinish;
	}
	
	public void setLateFinish(Duration lateFinish) {
		this.lateFinish = lateFinish;
		lateStart=lateFinish.minus(time);
	}
	
	public Duration getReserve() {
		return reserve;
	}
	
	public boolean isCrytical() {
		return crytical;
	}
	
	public Set<Integer> getPrevList() {
		return prevList;
	}
	
	public void setPrevList(Set<Integer> prevList) {
		this.prevList = prevList;
	}
	
	public Set<Integer> getNextList() {
		return nextList;
	}
	
	public void setNextList(Set<Integer> nextList) {
		this.nextList = nextList;
	}
	
	public boolean isPrevListContains(Integer id) {
		return prevList.contains(id);
	}
	
	public boolean isFromNextContains(Integer id) {
		return nextList.contains(id);
	}
	
	public void removeFromPrevList(Integer id) {
		prevList.remove(id);
	}
	
	public void removeFromNextList(Integer id) {
		nextList.remove(id);
	}
	
	public boolean isStart() {
		return prevList.size()==0;
	}
	
	public boolean isEnd() {
		return nextList.size()==0;
	}
	
	@Override
	public String toString() {
		return("Id:"+id+"_"+name+"_time:"+time+"Prev:"+prevList+"_Next:"+nextList);
	}
	
	@Override
	public int hashCode() {
		int hash = this.id;
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CPMActivity activity = (CPMActivity) obj;
		return Objects.equals(id, activity.id);
	}

	@Override
	public int compareTo(CPMActivity other) {
		int value = this.getId() - other.getId();
		return value;
	}
}