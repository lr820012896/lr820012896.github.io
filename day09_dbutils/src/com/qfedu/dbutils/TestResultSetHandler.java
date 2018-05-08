package com.qfedu.dbutils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.qfedu.bean.User;
import com.qfedu.c3p0.C3P0Utils;

public class TestResultSetHandler {

	
	//1、ArrayHandler：适合取一条记录，比如登录
	public static void test1() throws SQLException {
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql="select id,username,password from user where id=? and username=?";
		Object [] arr=qr.query(sql, new ArrayHandler(),1,"zhangsan");
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i]+",");
		}
	}
	//2、ArrayListHandler：适合取多条记录
	//每一条记录都是Object[]，再把Object[]放入List中。
	public static void test2() throws SQLException {
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql="select * from user";
		List<Object[]> list=qr.query(sql, new ArrayListHandler());
		//遍历查询结果
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).length; j++) {
				System.out.print(list.get(i)[j]+" ");
			}
			System.out.println();
		}
	}
	
	//3、ColumnListHandler：取某一列数据，保存到list中
	public static void test3() throws SQLException {
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql="select username,id,password from user";
		//ColumnListHandler构造方法可以传入一个int值，如果不传入默认是1
		//这个int值描述的是查询的结果集的列的序号，列的序号从1开始
		List<Object> list=qr.query(sql, new ColumnListHandler(3));
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	
	
	//4、MapHandler：取一条记录，把一条记录放入一个map中
	public static void test4() throws SQLException {
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql="select * from user where id=?";
		Map<String, Object> map=qr.query(sql, new MapHandler(),2);
		for (Map.Entry<String, Object> m :map.entrySet()) {
			System.out.println(m.getKey()+":"+m.getValue());
		}
	}
	
	//5、keyedHandler
	//它取一个查询集合，返回一个map套map，每一条记录封装到map中，每一个字段又在map里面的map中
	//Map<Object,Map<String,Object>>
	public static void test5() throws SQLException {
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql="select id,username,password from user";
		//KeyedHandler类里面构造方法传入一个永不重复的列，不然map的特点是key相同就覆盖。
		Map<Object,Map<String,Object>> map=qr.query(sql, new KeyedHandler(3));
		for (Map.Entry<Object, Map<String,Object>> m:map.entrySet()) {
			System.out.println(m.getKey());
			for (Map.Entry<String, Object> mm:m.getValue().entrySet()) {
				System.out.print(mm.getKey()+":"+mm.getValue()+"   ");
			}
			System.out.println();
			System.out.println("--------------------------");
		}
	}
	
	//6、MapListHandler ***  加强记忆
	//List<Map<String,Object>>
	//每一行都是一个map，再把map放入list中
	public static void test6() throws SQLException {
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql="select * from user";
		List<Map<String,Object>> list=qr.query(sql, new MapListHandler());
		for (int i = 0; i < list.size(); i++) {
			for (Map.Entry<String, Object> m:list.get(i).entrySet()) {
				System.out.println(m.getKey()+":"+m.getValue());
			}
			System.out.println("-------------------");
		}
	}
	
	//7、ScalarHandler取单行单列*** 加强记忆
	//比如说求count(*)
	public static void test7() throws SQLException {
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql="select count(*) from user";
		Object object=qr.query(sql, new ScalarHandler());
		//*****当查询count(*)的时候，事实上返回值是一个Long
		//你如果把它强转成Integer或者int，就是ClassCastException
		//你拿到的是object，但是你只能换成Long
		//(Long)((Object)new Integer(1));
		
		//Long转成long叫做拆箱
		//long转成int叫基本数据类型的转化，造成精度损失
		//Long转成Integer类转化异常
		System.out.println((int)(long)object);
	}
	
	
	//8、BeanHandler*****
	public static void test8() throws SQLException {
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		
		String sql="select * from user where id=?";
		
		User user=qr.query(sql, new BeanHandler<User>(User.class),3);
		
		System.out.println(user);
	}
	
	
	//9、BeanListHandler*****
	public static void test9() throws SQLException {
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql="select * from user where id>?";
		List<User> list=qr.query(sql, new BeanListHandler<User>(User.class),0);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	
	//insert、update、delete
	public static void testInsert() throws SQLException{
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql ="insert into user(id,username,password) values(?,?,?);";
		
		int rs=qr.update(sql,4,"zhaoliu","555555");
		if(rs>0) {
			System.out.println("数据添加成功");
		}
	}
	public static void testUpdate() throws SQLException{
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql ="update user set password=? where id=?";
		
		int rs=qr.update(sql,"666666",4);
		if(rs>0) {
			System.out.println("数据更新成功");
		}
	}
	public static void testDelete() throws SQLException{
		QueryRunner qr=new QueryRunner(C3P0Utils.getDataSource());
		String sql ="delete from user where id=?";
		
		int rs=qr.update(sql,4);
		if(rs>0) {
			System.out.println("数据删除成功");
		}
	}
	
	
	
	
	
	
	
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		//发现使用ResultSetHandler很麻烦，每一个都要实现一次
		//Apache commons DBUtils中有已经默认的ResultSetHandler的实现类。
		testDelete();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
