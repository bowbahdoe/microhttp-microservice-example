package dev.mccue.site.persistence;

import dev.mccue.site.SiteDatabase;
import dev.mccue.site.domain.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public final class UserPersistence {
    private static final String SELECT_COLUMNS = "\"user\".user_id, \"user\".email";
    private final DataSource dataSource;

    public UserPersistence(SiteDatabase dataSource) {
        this.dataSource = dataSource;
    }

    private User fromCurrentRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("email")
        );
    }

    public Optional<User> byId(int id) {
        try (var conn = this.dataSource.getConnection();
             var statement = conn.prepareStatement("SELECT %s FROM user WHERE user_id = ?;".formatted(SELECT_COLUMNS))) {
            statement.setInt(1, id);
            var rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(fromCurrentRow(rs));
            }
            else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createRandom() {
        try (var conn = this.dataSource.getConnection();
             var statement = conn.prepareStatement("""
                     INSERT INTO user (email)
                     VALUES (?)
                     """)) {
            statement.setString(1, UUID.randomUUID().toString());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
