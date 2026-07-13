package com.inno.business.auth.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Corrige les contraintes de base de données héritées d'une ancienne version du schéma.
 * La contrainte UNIQUE sur user_id dans la table companies doit être supprimée
 * car la relation est 1 User → N Companies (et non 1:1).
 */
@Component
public class DatabaseConstraintFixer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConstraintFixer.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseConstraintFixer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void dropLegacyUniqueConstraints() {
        // Supprime la contrainte unique sur user_id dans companies
        // Cette contrainte empêche un utilisateur d'avoir plusieurs sociétés
        dropConstraintIfExists("companies", "uk5xg6ed73n32iai9psir68pia9");

        // Si le nom de la contrainte est différent, on cherche aussi par la colonne
        dropUniqueConstraintOnColumnIfExists("companies", "user_id");
    }

    private void dropConstraintIfExists(String table, String constraintName) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.table_constraints " +
                    "WHERE table_name = ? AND constraint_name = ?",
                    Integer.class, table, constraintName
            );
            if (count != null && count > 0) {
                jdbcTemplate.execute(
                        "ALTER TABLE " + table + " DROP CONSTRAINT IF EXISTS " + constraintName
                );
                log.info("✅ Contrainte '{}' supprimée de la table '{}'", constraintName, table);
            }
        } catch (Exception e) {
            log.warn("⚠️ Impossible de supprimer la contrainte '{}': {}", constraintName, e.getMessage());
        }
    }

    private void dropUniqueConstraintOnColumnIfExists(String table, String columnName) {
        try {
            // Recherche toutes les contraintes UNIQUE sur la colonne donnée
            var constraints = jdbcTemplate.queryForList(
                    "SELECT tc.constraint_name " +
                    "FROM information_schema.table_constraints tc " +
                    "JOIN information_schema.constraint_column_usage ccu " +
                    "  ON tc.constraint_name = ccu.constraint_name " +
                    "WHERE tc.table_name = ? " +
                    "  AND tc.constraint_type = 'UNIQUE' " +
                    "  AND ccu.column_name = ?",
                    table, columnName
            );
            for (var row : constraints) {
                String name = (String) row.get("constraint_name");
                jdbcTemplate.execute("ALTER TABLE " + table + " DROP CONSTRAINT IF EXISTS " + name);
                log.info("✅ Contrainte UNIQUE '{}' sur colonne '{}' supprimée", name, columnName);
            }
        } catch (Exception e) {
            log.warn("⚠️ Recherche contraintes sur '{}': {}", columnName, e.getMessage());
        }
    }
}
