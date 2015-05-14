package dbService.controller;

public interface DBAction<T, M, C> {
	T action(C session, M param);
}
