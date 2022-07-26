package papem32;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sisres.sec.NovoLogin;

public class DButils {
	
	 
	
	static public void closeQuietly(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				Logger logger = LoggerFactory.getLogger(DButils.class);
				logger.error(e.getMessage(),e);
			}
		}

	}

	static public void closeQuietly(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) { 
				Logger logger = LoggerFactory.getLogger(DButils.class);
				logger.error(e.getMessage(),e);
			}
		}

	}

	static public void closeQuietly(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) { 
				Logger logger = LoggerFactory.getLogger(DButils.class);
				logger.error(e.getMessage(),e);
			}
		}

	}

	static public void closeQuietly(ResultSet rs, PreparedStatement ps, Connection conn) {
		closeQuietly(rs);
		closeQuietly(ps);
		closeQuietly(conn);
	}

	public static void rollbackQuietly(Connection conn) {
		if (conn!=null)
			try {
				conn.rollback();
			} catch (SQLException e) {
				Logger logger = LoggerFactory.getLogger(DButils.class);
				logger.error(e.getMessage(),e);
			}
	}

	public static void setAutoCommitQuietly(Connection conn, boolean b) {
		if (conn!=null)
			try {
				conn.setAutoCommit(b);
			} catch (SQLException e) {
				Logger logger = LoggerFactory.getLogger(DButils.class);
				logger.error(e.getMessage(),e);
			}
	}

	public static void closeQuietly(ResultSet results, PreparedStatement stmt) {
		closeQuietly(results);
		closeQuietly(stmt);
		
	}
}
