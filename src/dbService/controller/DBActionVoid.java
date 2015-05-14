package dbService.controller;

public interface DBActionVoid<M, C> {
	void action(C session, M param);
}
