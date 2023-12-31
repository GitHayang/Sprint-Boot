package com.kopo.color;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.sqlite.SQLiteConfig;

public class DB {
	
	Connection connection;
	private void open() {
		try {
			Class.forName("org.sqlite.JDBC");
			SQLiteConfig config = new SQLiteConfig();
			this.connection = DriverManager.getConnection("jdbc:sqlite:/C:\\kopo\\sts-4.19.1.RELEASE\\color.db", config.toProperties());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void close() {
		if(this.connection != null) {
			try {
				this.connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.connection = null;
	}
	
	
	public void deleteData(int idx) {
        this.open();
	    try {
	        String query = "DELETE FROM history WHERE idx = (?)";
	        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
	        preparedStatement.setInt(1, idx);
			preparedStatement.execute();
	        preparedStatement.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
        this.close();
	}

	
	
	public ArrayList<Color> select() {
		ArrayList<Color> result = new ArrayList<Color>();
		// db open
		this.open();
		try {
			String query = "SELECT * FROM history ORDER BY created DESC";
			PreparedStatement preparedStatement = this.connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				int idx = resultSet.getInt("idx");
				String code = resultSet.getString("color");
				String created = resultSet.getString("created");
				result.add(new Color(idx, code, created));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.close();
		return result;
	}
	
	
	public void insertTable(String code) {
		this.open();
		String query = "INSERT INTO history (color, created) VALUES (?,?)";
		try {
			PreparedStatement preparedStatement = this.connection.prepareStatement(query);
			preparedStatement.setString(1, code); // 색
			Date now = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formatedNow = formatter.format(now);
			preparedStatement.setString(2, formatedNow); // 현재 시간
			preparedStatement.execute();
			preparedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
	}
	
	
	public void createTable() {
		this.open();
		String query = "CREATE TABLE history (idx INTEGER PRIMARY KEY AUTOINCREMENT, color TEXT, created TEXT)";
		try {
			Statement statement = this.connection.createStatement();
			statement.execute(query);
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.close();
	}
	
}
