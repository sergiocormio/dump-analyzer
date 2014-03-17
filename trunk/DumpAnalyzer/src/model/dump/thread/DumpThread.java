package model.dump.thread;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase que representa un thread mostrado en el dump
 * @author scormio
 *
 */
public class DumpThread {
	private Integer id;
	private String name;
	private DumpThreadState state;
	private Set<DumpThread> children;
	private DumpThread parent;
	private String completeText;
	private String lock;
	
	public DumpThread(){
		children = new HashSet<DumpThread>();
	}
	
	/**
	 * Devuelve el numero de descendientes (hijos, nietos, etc)
	 * Este método es recursivo
	 * @return
	 */
	public int getNumberOfDescendants(){
		int quantity = children.size();
		for(DumpThread thread: children){
			quantity+= thread.getNumberOfDescendants();
		}
		return quantity;
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the state
	 */
	public DumpThreadState getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(DumpThreadState state) {
		this.state = state;
	}
	/**
	 * @return the children
	 */
	public Set<DumpThread> getChildren() {
		return children;
	}
	
	public void addChildren(DumpThread child){
		children.add(child);
	}
	
	public int getChildrenQuantity(){
		return children.size();
	}
	/**
	 * @return the parent
	 */
	public DumpThread getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(DumpThread parent) {
		this.parent = parent;
	}
	/**
	 * @return the completeText
	 */
	public String getCompleteText() {
		return completeText;
	}
	/**
	 * @param completeText the completeText to set
	 */
	public void setCompleteText(String completeText) {
		this.completeText = completeText;
	}
	/**
	 * @return the lock
	 */
	public String getLock() {
		return lock;
	}
	/**
	 * @param lock the lock to set
	 */
	public void setLock(String lock) {
		this.lock = lock;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this){
			return true;
		}
		if(obj instanceof DumpThread){
			DumpThread dt = (DumpThread) obj;
			if(dt.getId()!=null){
				return dt.getId().equals(this.getId());
			}else{
				return false;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		if(id != null){
			return id.hashCode();
		}
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		return  "Id=" + id +" Name=" + name + " State=" + state + (lock!=null? " Lock=" + lock:"") + " # of Descendants: " + getNumberOfDescendants();
	}
}
