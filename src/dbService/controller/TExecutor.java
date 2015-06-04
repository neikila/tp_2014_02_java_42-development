package dbService.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Created by neikila on 02.05.15.
 */
public class TExecutor{

    private Logger logger = LogManager.getLogger(TExecutor.class.getName());
    private SessionFactory sessionFactory;

    public TExecutor (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T, M> T action(DBAction<T, M, Session> action, M param){
        T value = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            value = action.action(session, param);
        } catch (HibernateException he) {
            logger.error(he);
            he.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
        return value;
    }

    public <M> void actionVoid(DBActionVoid<M, Session> action, M param){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            action.action(session, param);
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            if (transaction != null)
                transaction.rollback();
        } finally {
            if (transaction != null)
                transaction.commit();
            if (session != null && session.isOpen())
                session.close();
        }
    }
}
