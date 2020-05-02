package com.ardagnsrn.uniopiyango.managers;

import com.ardagnsrn.uniopiyango.UnioPiyango;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLManager {
	
	private ConnectionPoolManager pool;
	public String database;
	private UnioPiyango plugin;

	public SQLManager(UnioPiyango plugin) {
		this.plugin = plugin;
		pool = new ConnectionPoolManager(plugin, "UnioPiyangoPool");
		this.database = "genel";
	}

	public void fix(double credit, int realcredit)
	{
		String QUERY = "SELECT * FROM `piyango` WHERE `odul` = "+credit+";";
		try ( Connection connection = pool.getConnection() ) {
			PreparedStatement statement = connection.prepareStatement(QUERY);
			ResultSet res = statement.executeQuery();
			while (res.next())
			{
				plugin.getTicketManager().removeAward(res.getString("oyuncu"), res.getString("biletturu"));
				plugin.getTicketManager().awardThatWorks(res.getString("oyuncu"), res.getString("biletturu"), realcredit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public boolean updateSQL(String QUERY)
	{
		try ( Connection connection = pool.getConnection() ) {
			PreparedStatement statement = connection.prepareStatement(QUERY);
			int count = statement.executeUpdate();
			if(count > 0) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void onDisable() {
		pool.closePool();
	}

}