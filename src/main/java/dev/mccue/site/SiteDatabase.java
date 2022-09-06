package dev.mccue.site;

import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.sql.*;
import java.util.logging.Logger;

public final class SiteDatabase implements DataSource {
    private static final Path SQLITE_DB_PATH = Path.of("database.db");

    private final SQLiteDataSource dataSource;

    private SiteDatabase(SQLiteDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static SiteDatabase create() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + SQLITE_DB_PATH);

        try (var conn = dataSource.getConnection();
             var createTables = conn.prepareStatement("""
                CREATE TABLE IF NOT EXISTS "user" (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    email text unique
                );
                """)) {
            createTables.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new SiteDatabase(dataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return this.dataSource.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.dataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.dataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.dataSource.getLoginTimeout();
    }

    @Override
    public ConnectionBuilder createConnectionBuilder() throws SQLException {
        return this.dataSource.createConnectionBuilder();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.dataSource.getParentLogger();
    }

    @Override
    public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
        return this.dataSource.createShardingKeyBuilder();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.dataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.dataSource.isWrapperFor(iface);
    }
}
