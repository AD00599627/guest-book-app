package com.bt.form.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * The Class UserDaoImpl.
 */
@Repository
public class UserDaoImpl implements UserDao {
	

	private final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    public static final String COMMENTS = "comments";
    public static final String EMAIL = "email";
    public static final String ISAPPROVED = "isapproved";
	
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * Sets the named parameter jdbc template.
	 *
	 * @param namedParameterJdbcTemplate the new named parameter jdbc template
	 * @throws DataAccessException the data access exception
	 */
	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
			 {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the user info
	 */
	@Override
	public UserInfo findById(Integer id) {

		Map<String, Object> params = new HashMap<>();
		params.put("id", id);

		String sql = "SELECT * FROM USERINFO WHERE id=:id";
		UserInfo userInfo = null;
		try {
			userInfo = namedParameterJdbcTemplate.queryForObject(sql, params, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			logger.error("encounter error while retriving object from database: {} " , e.getMessage());
		}
		return userInfo;
	}

	/**
	 * Extracting All users list from Database for approval and delete by Admin.
	 *
	 * @return the list
	 */
	@Override
	public List<UserInfo> findAll() {

		String sql = "SELECT * FROM USERINFO";
		return namedParameterJdbcTemplate.query(sql, new UserMapper());
	}

	/**
	 * User Save.
	 *
	 * @param userInfo the user info
	 */
	@Override
	public void save(UserInfo userInfo) {

		findByEmail(userInfo.getEmail());
		UserInfo userInfoExist = findByEmail(userInfo.getEmail());
		if (Objects.nonNull(userInfoExist)) {
			logger.debug("**With the given email user already exist please provide another email.**");
			return;
		}

		KeyHolder keyHolder = new GeneratedKeyHolder();

		String sql = "INSERT INTO USERINFO(NAME, PASSWORD, CONTACT, EMAIL, COMMENTS, ISADMIN, ISAPPROVED) "
				+ "VALUES ( :name, :password, :contact, :email, :comments, :isadmin, :isapproved)";

		namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(userInfo), keyHolder);
		userInfo.setId(keyHolder.getKey().intValue());
	}

	/**
	 * Update comments.
	 *User updating comments
	 * @param userInfo the user info
	 */
	@Override
	public void updateComments(UserInfo userInfo) {
		MapSqlParameterSource in = new MapSqlParameterSource();
		String sqlUpdateComments = "UPDATE USERINFO SET COMMENTS=:comments WHERE id=:id";
		in.addValue("id", userInfo.getId());
		in.addValue(COMMENTS, userInfo.getComments());
		namedParameterJdbcTemplate.update(sqlUpdateComments, in);
	}

	/**
	 * Update approve status.
	 *Approve of user comments by admin 
	 * @param id the id
	 */
	public void updateApproveStatus(Integer id) {
		MapSqlParameterSource in = new MapSqlParameterSource();
		in.addValue("id", id);
		in.addValue(ISAPPROVED, true);
		String sqlUpdateApprovalStatus = "UPDATE USERINFO SET ISAPPROVED=:isapproved where id = :id";
		namedParameterJdbcTemplate.update(sqlUpdateApprovalStatus, in);
	}

	/**
	 * Delete.
	 *
	 * @param id the id
	 * Deleting User based on ID
	 */
	@Override
	public void delete(Integer id) {

		String sql = "DELETE FROM USERINFO WHERE id= :id";
		namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
	}


	private SqlParameterSource getSqlParameterByModel(UserInfo userInfo) {

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("id", userInfo.getId());
		paramSource.addValue("name", userInfo.getName());
		paramSource.addValue("password", userInfo.getPassword());
		paramSource.addValue("contact", userInfo.getContact());
		paramSource.addValue(EMAIL, userInfo.getEmail());
		paramSource.addValue(COMMENTS, userInfo.getComments());
		paramSource.addValue("isadmin", userInfo.getIsAdmin());
		paramSource.addValue(ISAPPROVED, userInfo.getIsApproved());
		return paramSource;
	}


	private static final class UserMapper implements RowMapper<UserInfo> {

		/**
		 * Map row.
		 *
		 * @param rs the rs
		 * @param rowNum the row num
		 * @return the user info
		 * @throws SQLException the SQL exception
		 * Converting Resultset data to UserInfo object
		 */
		public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserInfo userInfo = new UserInfo();
			userInfo.setId(rs.getInt("id"));
			userInfo.setName(rs.getString("name"));
			userInfo.setPassword(rs.getString("password"));
			userInfo.setContact(rs.getInt("contact"));
			userInfo.setEmail(rs.getString(EMAIL));
			userInfo.setComments(rs.getString(COMMENTS));
			userInfo.setIsAdmin(rs.getBoolean("isadmin"));
			userInfo.setIsApproved(rs.getBoolean(ISAPPROVED));
			return userInfo;
		}
	}

	/**
	 * Find by email.
	 *
	 * @param email the email
	 * @return the user info
	 * Extracting user details from email
	 */
	@Override
	public UserInfo findByEmail(String email) {
		Map<String, Object> params = new HashMap<>();
		params.put(EMAIL, email);

		String sql = "SELECT * FROM USERINFO WHERE email=:email";
		UserInfo userInfo = null;
		try {
			userInfo = namedParameterJdbcTemplate.queryForObject(sql, params, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			logger.error("Encounter while extracting user details");
		}
		return userInfo;
	}

}