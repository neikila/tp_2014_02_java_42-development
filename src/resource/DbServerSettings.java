package resource;

import java.io.Serializable;

/**
 * Created by neikila on 02.04.15.
 */
public class DbServerSettings implements Serializable, Resource {
    private String dialect;
    private String driverClass;
    private String connectionUrl;
    private String username;
    private String password;
    private String showSql;
    private String mode;

    public DbServerSettings() {}

    public String getDialect() {
        return dialect;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getShowSql() {
        return showSql;
    }

    public String getMode() {
        return mode;
    }

    @Override
    public void checkState() {
    }
}
