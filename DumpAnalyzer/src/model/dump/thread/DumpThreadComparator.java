package model.dump.thread;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Comparator para ordenar por cantidad de descendientes en order decreciente
 * @author scormio
 *
 */
public class DumpThreadComparator implements Comparator<DumpThread> {
	
	private Map<DumpThreadState, Integer> stateValueMap;

	public DumpThreadComparator(){
		stateValueMap = new HashMap<DumpThreadState, Integer>();
		stateValueMap.put(DumpThreadState.BLOCKED, 3);
		stateValueMap.put(DumpThreadState.TIMED_WAITING, 2);
		stateValueMap.put(DumpThreadState.WAITING, 2);
		stateValueMap.put(DumpThreadState.RUNNING, 1);
		stateValueMap.put(DumpThreadState.RUNNABLE, 1);
	}

	public int compare(DumpThread o1, DumpThread o2) {
		int descendantDifference = o2.getNumberOfDescendants()-o1.getNumberOfDescendants();
		if(descendantDifference != 0){
			return descendantDifference;
		}else{ //En caso de que tengan la misma cantidad de descendientes debe ordenar por estado
			Integer value1 = (stateValueMap.get(o1.getState())!=null) ? stateValueMap.get(o1.getState()) : 1;
			Integer value2 =  (stateValueMap.get(o2.getState())!=null) ? stateValueMap.get(o2.getState()) : 1;
			//En caso de que tengan el mismo estado y cantidad de descendientes, entonces ordena por id de mayor a menor
			if(value2 - value1 == 0){
				return o2.getId() - o1.getId();
			}else{
				return value2 - value1;
			}
		}
	}

}
