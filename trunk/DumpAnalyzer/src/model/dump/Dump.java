package model.dump;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dump.thread.DumpThread;
import model.dump.thread.DumpThreadComparator;

/**
 * Clase que representa el archivo Dump pero con una represantación de objetos
 * @author scormio
 *
 */
public class Dump {
//	private List<DumpThread> threads;
	private Map<Integer,DumpThread> threadsMap;
	private String header;
	private String footer;
	
	public Dump(){
		threadsMap = new HashMap<Integer, DumpThread>();
	}
	
	public String getFooter() {
		return footer;
	}
	
	public void setFooter(String footer) {
		this.footer = footer;
	}
	
	public String getHeader() {
		return header;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}

	public DumpThread getThread(Integer threadId) {
		DumpThread thread = threadsMap.get(threadId);
		if(thread == null){
			thread = new DumpThread();
			thread.setId(threadId);
			threadsMap.put(threadId, thread);
		}
		return thread;
	}
	
	public Collection<DumpThread> getAllThreads(){
		return threadsMap.values();
	}
	
	public List<DumpThread> getAllThreadsSorted(){
		//Pasaje a una lista para poder ordenar
		Collection<DumpThread> threads = getAllThreads();
		List<DumpThread> threadsList = new ArrayList<DumpThread>();
		for(DumpThread thread : threads){
			threadsList.add(thread);
		}
		//Ordena y muestra sólo los que tienen descendientes
		Collections.sort(threadsList, new DumpThreadComparator());
		return threadsList;
	}

}
