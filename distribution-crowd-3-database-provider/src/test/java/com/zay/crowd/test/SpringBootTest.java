package com.zay.crowd.test;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {
	@Autowired
	private DataSource dataSource;
	@Test
	public void test() throws SQLException {
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
	}
	
}
