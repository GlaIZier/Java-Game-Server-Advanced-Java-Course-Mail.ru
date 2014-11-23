package server.db.jdbc;

import java.sql.ResultSet;

public interface TResultHandler<T> {
   
	public T handle(ResultSet resultSet);

}
