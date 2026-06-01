package database;

import org.flywaydb.core.Flyway;
import java.util.logging.Logger;
import java.util.logging.Level;

public class MigrationConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/pagibig";
    private static final String USER     = "root";
    //private static final String PASSWORD = "adminuser";
    private static final String PASSWORD = "mysqladmin";
    
    
    public static void migrate() {
        // ── Suppress Flyway logs ─────────────────────────────────────────────
        Logger.getLogger("org.flywaydb").setLevel(Level.OFF);

        System.out.println("Running migrations...");

        Flyway flyway = Flyway.configure()
            .dataSource(URL, USER, PASSWORD)
            .locations("filesystem:database/migrations")
            .baselineOnMigrate(true)
            .load();

        flyway.migrate();

        System.out.println("Migrations completed successfully.");
    }

    public static void main(String[] args) {
        migrate();
    }
}