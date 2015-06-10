import java.sql.*;

public class DAL {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	static final String DB_URL = "jdbc:mysql://62.79.16.16/grp16";

	static final String USER = "grp16";
	static final String PASS = "ZHnPq74Y";

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");

			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			System.out.println("Creating statement...");
			stmt = conn.createStatement();

			String sql1;
			sql1 = "SELECT opr_id, opr_navn, ini, cpr, password FROM operatoer";
			String sql2;
			sql2 = "SELECT * FROM raavare";
//			String sql3;
//			sql3 = "INSERT INTO raavare VALUES (2,\"Pære\",\"Gullis Æbler\")";
//			
//			stmt.executeUpdate(sql3);
			
			ResultSet rs1 = stmt.executeQuery(sql1);
			
			

			System.out.println("Operatører:");
			while (rs1.next()) {

				int id = rs1.getInt("opr_id");
				String navn = rs1.getString("opr_navn");
				String ini = rs1.getString("ini");
				String cpr = rs1.getString("cpr");
				String password = rs1.getString("password");

				System.out.print("ID: " + id);
				System.out.print(" ini: " + ini);
				System.out.print(" Navn: " + navn);
				System.out.print(" CPR: " + cpr);
				System.out.println(" Password: " + password);

			}
			ResultSet rs2 = stmt.executeQuery(sql2);
			System.out.println();
			System.out.println("Ravvare:");
			
			while(rs2.next()) {
				System.out.print("ID: " + rs2.getString(1) + " ");
				System.out.print("Navn: " + rs2.getString(2) + " ");
				System.out.println("Leverandør: " + rs2.getString(3));
			}
			
			
			
			

			rs1.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			se.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}
}