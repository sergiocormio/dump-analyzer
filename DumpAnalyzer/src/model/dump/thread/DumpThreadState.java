package model.dump.thread;
/**
 * Representa los estados en los que puede encontrarse un thread dentro del dump
 * @author scormio
 *
 */
public enum DumpThreadState {
	RUNNING, RUNNABLE, WAITING,TIMED_WAITING, BLOCKED
}
