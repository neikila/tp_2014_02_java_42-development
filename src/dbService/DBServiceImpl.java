package dbService;

import Interface.DBAction;
import Interface.DBActionVoid;
import Interface.DBService;
import dbService.dao.UserProfileDAO;
import main.user.UserProfile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class DBServiceImpl implements DBService {
    private SessionFactory sessionFactory;

    public DBServiceImpl() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserProfile.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/gameDB");
        configuration.setProperty("hibernate.connection.username", "gameAdmin");
        configuration.setProperty("hibernate.connection.password", "rfrltkf");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "validate");

        sessionFactory = createSessionFactory(configuration);
    }

    private <T, M> T actionUserProfileDAO(DBAction<T, M, UserProfileDAO> action, M param){
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        T value = action.action(dao, param);
        return value;
    }

    private <M> void actionUserProfileDAOVoid(DBActionVoid<M, UserProfileDAO> action, M param){
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        Transaction transaction = session.beginTransaction();
        action.action(dao, param);
        transaction.commit();
    }

    public String getLocalStatus() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String status = transaction.getLocalStatus().toString();
        session.close();
        return status;
    }

    public void vipe() {
        actionUserProfileDAOVoid((dao, temp) -> dao.vipe(), null);
    }

    public void deleteAllUsers() {
        actionUserProfileDAOVoid((dao, temp) -> dao.deleteAll(), null);
    }

    public void save(UserProfile dataSet) {
        actionUserProfileDAOVoid((dao, temp) -> dao.save(temp), dataSet);
    }

    public UserProfile readUser(long id) {
        return actionUserProfileDAO((dao, temp) -> dao.read(temp), id);
    }

    public UserProfile readUserByName(String name) {
        return actionUserProfileDAO((dao, temp) -> dao.readByName(temp), name);
    }

    public List<UserProfile> readAll() {
        return actionUserProfileDAO((dao, temp) -> dao.readAll(), null);
    }

    public long countAllUsers() {
        return actionUserProfileDAO((dao, temp) -> dao.countAll(), null);
    }

    public List<UserProfile> readLimitOrder(int limit) {
        return actionUserProfileDAO((dao, temp) -> dao.readLimitOrder(temp), limit);
    }

    public void shutdown(){
        sessionFactory.close();
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}