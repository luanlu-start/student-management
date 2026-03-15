package vn.edu.fpt.app.dao;

import java.sql.Connection;

/**
 * Legacy JDBC base class kept temporarily so old DAO classes still compile
 * during migration to Spring Data JPA services/repositories.
 */
public class DBContext {
    protected Connection conn;
}


