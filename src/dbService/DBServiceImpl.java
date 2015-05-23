package dbService;

import dbService.controller.TExecutor;
import dbService.dao.UserProfileDAO;
import main.user.UserProfile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import resource.DbServerSettings;

import java.util.List;

public class DBServiceImpl implements DBService {
    private SessionFactory sessionFactory;
    private TExecutor tExecutor;

    public DBServiceImpl(DbServerSettings dbServerSettings) {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserProfile.class);

        configuration.setProperty("hibernate.dialect", dbServerSettings.getDialect());
        configuration.setProperty("hibernate.connection.driver_class", dbServerSettings.getDriverClass());
        configuration.setProperty("hibernate.connection.url", dbServerSettings.getConnectionUrl());
        configuration.setProperty("hibernate.connection.username", dbServerSettings.getUsername());
        configuration.setProperty("hibernate.connection.password", dbServerSettings.getPassword());
        configuration.setProperty("hibernate.show_sql", dbServerSettings.getShowSql());
        configuration.setProperty("hibernate.hbm2ddl.auto", dbServerSettings.getMode());

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

    public void deleteAllUsers() {
        tExecutor.actionVoid(
                (session, temp) -> {
                    UserProfileDAO dao = new UserProfileDAO(session);
                    dao.deleteAll();
                }, null);
    }

    public void save(UserProfile dataSet) {
        tExecutor.actionVoid(
                (session, temp) -> {
                    UserProfileDAO dao = new UserProfileDAO(session);
                    dao.save(temp);
                }, dataSet);
    }

    public void update(UserProfile dataSet) {
        tExecutor.actionVoid(
                (session, temp) -> {
                    UserProfileDAO dao = new UserProfileDAO(session);
                    dao.update(temp);
                }, dataSet);
    }

    public UserProfile readUser(long id) {
        return tExecutor.action(
                (session, temp) -> {
                    UserProfileDAO dao = new UserProfileDAO(session);
                    return dao.read(temp);
                }, id);
    }

    public UserProfile readUserByName(String name) {
        return tExecutor.action(
                (session, temp) -> {
                    UserProfileDAO dao = new UserProfileDAO(session);
                    return dao.readByName(temp);
                }, name);
    }

    public List<UserProfile> readAll() {
        return tExecutor.action(
                (session, temp) -> {
                    UserProfileDAO dao = new UserProfileDAO(session);
                    return dao.readAll();
                }, null);
    }

    public long countAllUsers() {
        return tExecutor.action(
                (session, temp) -> {
                    UserProfileDAO dao = new UserProfileDAO(session);
                    return dao.countAll();
                } , null);
    }

    public List<UserProfile> readLimitOrder(int limit) {
        return tExecutor.action(
                (session, temp) -> {
                    UserProfileDAO dao = new UserProfileDAO(session);
                    return dao.readLimitOrder(temp);
                }, limit);
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