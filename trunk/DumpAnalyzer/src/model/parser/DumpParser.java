package model.parser;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import model.dump.Dump;
import model.dump.thread.DumpThread;
import model.dump.thread.DumpThreadState;

/**
 * Parser para el archivo Dump.
 * @author scormio
 *
 */
public class DumpParser {
	
	private static final String LINE_BREAK = "\r\n";

	public void parse(Dump dump, String filename) throws IOException{
		 parse(dump,new File(filename));
	}
	
	public void parse(Dump dump, File file) throws IOException{
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		byte fileContent[] = new byte[(int)file.length()];
		bis.read(fileContent);
		bis.close();
		
		String fileContentAsString = new String(fileContent);
		String tokens[] = fileContentAsString.split(LINE_BREAK+LINE_BREAK);
		//hace un tratamiento especial al primer token para sacarle el encabezado
		int i = tokens[0].indexOf("\"");
		String header = "";
		if(i>=0){
			header = tokens[0].substring(0,i);
			tokens[0] = tokens[0].substring(i);
		}
		//setea el encabezado y el pie
		dump.setHeader(header);
		dump.setFooter(tokens[tokens.length-1]);
		
		for(String token : tokens){
			parseToken(dump, token);
		}
	}
	
	private void parseToken(Dump dump, String token) {
		//si comienza con comillas dobles es un thread
		if(token.startsWith("\"")){
			//name
			String name = token.split("\"")[1];
			//Id
			int beginOfId = token.indexOf("Id=") + 3;
			int endOfId = token.indexOf(" ", beginOfId);
			String id = token.substring(beginOfId, endOfId);
			DumpThread thread = dump.getThread(Integer.parseInt(id));
			thread.setName(name);
			//completeText
			thread.setCompleteText(token);
			
			//State
			parseState(token, thread);
			
			//lock
			parseLock(token, thread);
			
			//Si esta bloqueado busca el padre
			findAndSetParent(dump, token, thread);

		}
		
	}

	/**
	 * En caso de que este en estado Blocked...
	 * Busca el thread padre dentro del token y lo asigna dentro del thread
	 * @param dump
	 * @param token
	 * @param thread
	 */
	private void findAndSetParent(Dump dump, String token, DumpThread thread) {
		if(thread.getState()==DumpThreadState.BLOCKED){
			int beginOfOwnedBy = token.indexOf("owned by "); 
			int endOfOwnedBy = token.indexOf(LINE_BREAK,beginOfOwnedBy);
			if(beginOfOwnedBy>=0){
				int beginOfOwnerId = token.indexOf("Id=",beginOfOwnedBy) + 3;
				String parentId = token.substring(beginOfOwnerId, endOfOwnedBy);
				DumpThread parent = dump.getThread(Integer.parseInt(parentId));
				parent.addChildren(thread);
				thread.setParent(parent);
			}
		}
	}

	/**
	 * Busca el Lock dentro del token y lo asigna dentro del thread
	 * @param token
	 * @param thread
	 */
	private void parseLock(String token, DumpThread thread) {
		int beginOfOnLock = token.indexOf(" on lock="); 
		int endOfOnLock = token.indexOf(LINE_BREAK,beginOfOnLock);
		if(beginOfOnLock>=0){
			beginOfOnLock+=9;
			String onLock = token.substring(beginOfOnLock, endOfOnLock);
			thread.setLock(onLock);
		}
	}

	/**
	 * Busca el State dentro del token y lo asigna dentro del thread
	 * @param token
	 * @param thread
	 * @return
	 */
	private DumpThreadState parseState(String token, DumpThread thread) {
		int beginOfState = token.indexOf(" in ") + 4;
		int endOfState = token.indexOf(" ", beginOfState);
		if(endOfState<0){
			endOfState = token.indexOf(LINE_BREAK, beginOfState);
		}
		if(endOfState<0){
			endOfState = token.length();
		}
		String stateStr = token.substring(beginOfState, endOfState);
		DumpThreadState state = DumpThreadState.valueOf(stateStr.trim());
		thread.setState(state);
		return state;
	}

	public static void main(String[] args) throws IOException {
		DumpParser parser = new DumpParser();
		Dump dump = new Dump();
		parser.parse(dump, "./bin/resources/example_dump");
		System.out.println(new DumpStatistics(dump).getStatistics());
		
	}
}
