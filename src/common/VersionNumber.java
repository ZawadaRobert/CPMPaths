package common;

public class VersionNumber {
	
	private int major;
	private int minor;
	private int relese;
	
	public VersionNumber(int major, int minor ,int relese) {
		this.major=major;
		this.minor=minor;
		this.relese=relese;
	}
	
	public int getMajor() {
		return major;
	}
	
	public void setMajor(int major) {
		this.major = major;
	}
	
	public int getMinor() {
		return minor;
	}
	
	public void setMinor(int minor) {
		this.minor = minor;
	}
	
	public int getRelese() {
		return relese;
	}
	
	public void setRelese(int relese) {
		this.relese = relese;
	}
	
	@Override
	public String toString() {
		return(major+"."+minor+" "+relese);
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		result = 100000000 * result + major;
		result = 100000 * result + minor;
		result = result + relese;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersionNumber other = (VersionNumber) obj;
		if (major != other.major)
			return false;
		if (minor != other.minor)
			return false;
		if (relese != other.relese)
			return false;
		return true;
	}
	
	public int compareTo(VersionNumber other) {
		if (this.getMajor()!=other.getMajor())
			return this.getMajor() - other.getMajor();
		if (this.getMinor()!=other.getMinor())
			return this.getMinor() - other.getMinor();
		return this.getRelese() - other.getRelese();
	}
}
