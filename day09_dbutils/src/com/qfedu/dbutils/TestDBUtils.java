package com.qfedu.dbutils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.qfedu.bean.User;
import com.qfedu.c3p0.C3P0Utils;

public class TestDBUtils {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		test();
	}

	public static void test() throws SQLException {
		// 第一个dbutils的例子：
		// 创建queryrunner对象，参数是一个数据源
		// 我们使用c3p0数据源。
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from user where id=?";
		// queryRunner的query方法第二个参数ResultSetHandler对象需要一个泛型
		// 这个泛型就是ResultSetHandler的handle方法的返回值类型
		// ResultSetHandler的handler方法的返回值会交给queryrunner的query方法，作为返回值返回。
		// String result=qr.query(sql, new ResultSetHandler<String>() {
		//
		// //handle方法有一个参数是ResultSet
		// //这个ResultSet就是query方法的第一个参数sql语句的查询结果。
		// @Override
		// public String handle(ResultSet rs) throws SQLException {
		// // TODO Auto-generated method stub
		// if(rs.next()) {
		// return rs.getString("username");
		// }
		// return "123";
		// }
		//
		// });
		// System.out.println(result);

//		User user = qr.query(sql, new UserResultSetHandler());
//		System.out.println(user);
//		User user=qr.query(sql, new BeanResultSetHandler<User>(User.class));
//		System.out.println(user);
		
		Map<String, Object> map=qr.query(sql, new MapHandler(),2);
		//HashMap  toString  每一个Node都key=value
		System.out.println(map);
		
		//那如果要想获取List<Bean>呢？
		//那如果要想获取List<Map<String,Object>>呢？
		
		
	}

	static class UserResultSetHandler implements ResultSetHandler<User> {

		@Override
		public User handle(ResultSet rs) throws SQLException {
			// TODO Auto-generated method stub
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				return user;
			}
			return null;
		}
	}

	static class BeanResultSetHandler<T> implements ResultSetHandler<T> {
		private Class<T> clazz = null;

		public BeanResultSetHandler(Class<T> clazz) {
			super();
			this.clazz = clazz;
		}

		@Override
		public T handle(ResultSet rs) throws SQLException {
			// TODO Auto-generated method stub
			try {
				if (rs.next()) {
					T object = clazz.newInstance();

					// 获取所有的成员
					Field[] fields = clazz.getDeclaredFields();
					for (int i = 0; i < fields.length; i++) {
						// 把成员可见性设置为true
						fields[i].setAccessible(true);
						// 根据名字取值并且设置给object
						fields[i].set(object, rs.getObject(fields[i].getName()));
					}
					return object;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
	//User [id=1, username=zhangsan, password=123456]
	//Map key=id  value=1   key=username  value=zhangsan  key=password value=123456
	//可见，一个bean对象可以理解为一个map
	static class MapHandler implements ResultSetHandler<Map<String,Object>>{

		@Override
		public Map<String,Object> handle(ResultSet rs) throws SQLException {
			// TODO Auto-generated method stub
			if(rs.next()) {
				Map<String,Object> map=new HashMap<String,Object>();
				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
					String key=rs.getMetaData().getColumnName(i+1);
					Object value=rs.getObject(i+1);
					map.put(key, value);
				}
				return map;
			}
			
			return null;
		}
		
	}

}
