package ASE;
import java.sql.*;

public class DAL {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	static final String DB_URL = "jdbc:mysql://62.79.16.16/grp16";

	static final String USER = "grp16";
	static final String PASS = "ZHnPq74Y";
	Connection conn;
	Statement stmt;

	public DAL() {
		try {

			Class.forName("com.mysql.jdbc.Driver");

			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			System.out.println("Creating statement...");
			stmt = conn.createStatement();

//			String sql1;
//			sql1 = "SELECT opr_id, opr_navn, ini, cpr, password FROM operatoer";
//			String sql2;
//			sql2 = "SELECT * FROM raavare";
//			String sql3;
//			sql3 = "INSERT INTO raavare VALUES (2,\"Pære\",\"Gullis Æbler\")";
//			
//			stmt.executeUpdate(sql3);
			
//			ResultSet rs1 = stmt.executeQuery(sql1);
			
			
//
//			System.out.println("Operatører:");
//			while (rs1.next()) {
//
//				int id = rs1.getInt("opr_id");
//				String navn = rs1.getString("opr_navn");
//				String ini = rs1.getString("ini");
//				String cpr = rs1.getString("cpr");
//				String password = rs1.getString("password");
//
//				System.out.print("ID: " + id);
//				System.out.print(" ini: " + ini);
//				System.out.print(" Navn: " + navn);
//				System.out.print(" CPR: " + cpr);
//				System.out.println(" Password: " + password);
//				
//
//			}
//			ResultSet rs2 = stmt.executeQuery(sql2);
//			System.out.println();
//			System.out.println("Ravvare:");
			
//			while(rs2.next()) {
//				System.out.print("ID: " + rs2.getString(1) + " ");
//				System.out.print("Navn: " + rs2.getString(2) + " ");
//				System.out.println("Leverandør: " + rs2.getString(3));
//			}
			
			
//			System.out.println();
//			DatabaseMetaData md = conn.getMetaData();
//		    ResultSet rs = md.getTables(null, null, "%", null);
//		    while (rs.next()) {
//		      System.out.println(rs.getString(3));
//		    }
			
			//Swag
			
			

//			rs1.close();
			

		} catch (SQLException se) {
			se.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		}

	}
	
	
	public String getOprNameFromID(String id) {
		try {
			String sql = "SELECT opr_navn FROM grp16.operatoer WHERE opr_id = " + id;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			if (rs.next()) {
				return rs.getString("opr_navn");		
			} else {
				return "ID findes ikke!";
			}
			
		} catch (SQLException e) {
			return "SQL fejl";
		}
	}
}

class noOprID extends Exception {
	public noOprID(String message) {
		super(message);
	}
}