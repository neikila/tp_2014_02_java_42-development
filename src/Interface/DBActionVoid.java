package Interface;

public interface DBActionVoid<M, C> {
	void action(C dao, M param);
}
