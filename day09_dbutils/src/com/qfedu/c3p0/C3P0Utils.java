package com.qfedu.c3p0;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

//对C3P0进行封装
public class C3P0Utils {
	//连接池对象
	private static DataSource dataSource=new ComboPooledDataSource();
	//不用写静态代码块初始化了。
	
	public static Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new RuntimeException("服务器繁忙。。。");
		}
	}
	//封装一个可以访问内部连接池的方法
	//为了之后dbutils的使用
	public static DataSource getDataSource() {
		return dataSource;
	}
	public static void closeAll(Statement ps,Connection conn) {
		
		if(ps!=null) {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void closeAll(ResultSet rs,Statement ps,Connection conn) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		closeAll(ps, conn);
	}
	
}
