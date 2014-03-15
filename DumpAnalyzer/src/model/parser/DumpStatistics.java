package model.parser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dump.Dump;
import model.dump.thread.DumpThread;
import model.dump.thread.DumpThreadState;

/**
 * Clase que toma un dump y saca estadisticas.
 * @author scormio
 *
 */
public class DumpStatistics {
	private Map<DumpThreadState, Integer> stateQuantityMap;
	private Dump dump;
	
	public DumpStatistics(Dump dump){
		this.dump = dump;
		extractStatistics();
	}

	private void extractStatistics() {
		//Quantities
		stateQuantityMap = new HashMap<DumpThreadState, Integer>();
		for(DumpThreadState state : DumpThreadState.values()){
			stateQuantityMap.put(state, 0);
		}
		for(DumpThread thread : dump.getAllThreads()){
			int quantity = stateQuantityMap.get(thread.getState());
			stateQuantityMap.put(thread.getState(),++quantity);
		}
	}
	
	public String getStatistics(){
		StringBuilder sb = new StringBuilder();
		sb.append("Quantity of threads per State:\n");
		sb.append("------------------------------------\n");
		for(DumpThreadState state : stateQuantityMap.keySet()){
			sb.append(state + ": " +  stateQuantityMap.get(state)+"\n");
		}
		sb.append("------------------------------------\n");
		sb.append("TOTAL of THREADS: " + dump.getAllThreads().size()+"\n");
		sb.append("------------------------------------\n");
		sb.append("Threads with Descendants:\n");
		//Pasaje a una lista para poder ordenar
		List<DumpThread> threadsList = dump.getAllThreadsSorted();
		for(DumpThread thread : threadsList){
			if(thread.getNumberOfDescendants()>0){
				sb.append(thread+"\n");
			}
		}
		sb.append("------------------------------------\n");
		//header
		sb.append("Dump Header:\n");
		sb.append(dump.getHeader());
		sb.append("------------------------------------\n");
		sb.append("Dump Footer:\n");
		sb.append(dump.getFooter());
		sb.append("------------------------------------\n");
		return sb.toString();
	}
}
