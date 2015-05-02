package dbService.controller;

public interface DBActionVoid<M, C> {
	void action(C dao, M param);
}
