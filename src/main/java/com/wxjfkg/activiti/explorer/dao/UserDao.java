package com.wxjfkg.activiti.explorer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public User selectUser(String username) {
		User result = null;
		try {
			result = jdbcTemplate.queryForObject(
					"select * from ACT_USER where user_name = ?",
					new RowMapper<User>() {

						@Override
						public User mapRow(ResultSet rs, int row)
								throws SQLException {
							User user = new UserEntity();
							user.setId(String.valueOf(rs.getLong("id")));
							user.setFirstName(rs.getString("user_name"));
							user.setLastName(rs.getString("user_name"));
							return user;
						}

					}, new Object[] { username });
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			return null;
		}
		return result;
	}
	
}
