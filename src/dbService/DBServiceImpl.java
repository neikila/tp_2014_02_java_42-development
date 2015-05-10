package dbService;

import dbService.controller.TExecutor;
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
    private TExecutor tExecutor;

    public DBServiceImpl() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserProfile.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/gameDB");
        configuration.setProperty("hibernate.connection.username", "gameAdmin");
        configuration.setProperty("hibernate.connection.password", "rfrltkf");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        sessionFactory = createSessionFactory(configuration);

        tExecutor = new TExecutor(sessionFactory);
    }

    public String getLocalStatus() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String status = transaction.getLocalStatus().toString();
        session.close();
        return status;
    }

    public void vipe() {
        tExecutor.actionUserProfileDAOVoid((dao, temp) -> dao.vipe(), null);
    }

    public void deleteAllUsers() {
        tExecutor.actionUserProfileDAOVoid((dao, temp) -> dao.deleteAll(), null);
    }

    public void save(UserProfile dataSet) {
        tExecutor.actionUserProfileDAOVoid((dao, temp) -> dao.save(temp), dataSet);
    }

    public UserProfile readUser(long id) {
        return tExecutor.actionUserProfileDAO((dao, temp) -> dao.read(temp), id);
    }

    public UserProfile readUserByName(String name) {
        return tExecutor.actionUserProfileDAO((dao, temp) -> dao.readByName(temp), name);
    }

    public List<UserProfile> readAll() {
        return tExecutor.actionUserProfileDAO((dao, temp) -> dao.readAll(), null);
    }

    public long countAllUsers() {
        return tExecutor.actionUserProfileDAO((dao, temp) -> dao.countAll(), null);
    }

    public List<UserProfile> readLimitOrder(int limit) {
        return tExecutor.actionUserProfileDAO((dao, temp) -> dao.readLimitOrder(temp), limit);
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