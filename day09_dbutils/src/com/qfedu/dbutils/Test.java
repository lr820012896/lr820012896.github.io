package com.qfedu.dbutils;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//转型：
		//强制类型转换
		//当类型转化的时候，分为向上转型，向下转型，装拆箱
		Integer i=new Integer(1);
		Object o=(Object)i;//向上转型
		Integer i1=(Integer)o;//向下转型
		
		
		//错误的向下转型
		//向下转型并不是随意转型的
		//对象本身如果不是这个类型就不可以转
//		Long l=(Long)o;//ClassCastException
//		int j=3;
//		Object ob=(Object)j;
//		long k=(long)ob;
//		System.out.println(k);
	}

}
