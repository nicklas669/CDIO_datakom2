package ASE;
import java.sql.*;
import java.util.ArrayList;

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

	public String insertProduktBatchKomp(int pb_id, int rb_id, double tara, double netto, int opr_id){
		try {
			String sql = String.format("INSERT INTO produktbatchkomponent VALUES (%d, %d, %f, %f, %d)", pb_id, rb_id, tara, netto, opr_id);
			System.out.println(sql);
			stmt = conn.createStatement();
			int result = stmt.executeUpdate(sql);
			return "Success";
		} catch(SQLException e) {
			return "SQL Fejl";
		}
	}

	public String setProduktBatchStatus(int pb_id, int status){
		try {
			//			String sql = "SELECT recept_navn FROM grp16.recept WHERE recept_id = (SELECT recept_id FROM grp16.produktbatch WHERE pb_id = " + id + ");";
			String sql = "UPDATE produktbatch SET status = "+status+" WHERE pb_id = "+pb_id;
			stmt = conn.createStatement();
			int result = stmt.executeUpdate(sql);
			return "Success";
		} catch(SQLException e) {
			return "SQL Fejl";
		}
	}

	public String getReceptNavnFromPBID(String id){
		try {
			String sql = "SELECT recept_navn FROM grp16.recept WHERE recept_id = (SELECT recept_id FROM grp16.produktbatch WHERE pb_id = " + id + ");";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				return rs.getString("recept_navn");
			} else {
				return "ID findes ikke!";
			}
		} catch(SQLException e) {
			return "SQL Fejl";
		}
	}

	public String getRaavarebatch(int id, int raav_id){
		try {
			String sql = "SELECT raavare_id FROM raavarebatch WHERE rb_id = "+id;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				if (raav_id == rs.getInt("raavare_id")) {
					return "Success";
				} 
			}
			return "ID findes ikke!";

			} catch(SQLException e) {
				return "SQL Fejl";
			}
		}

		public String getRaavareNameFromID(int id){
			try {
				String sql = "SELECT raavare_navn FROM raavare WHERE raavare_id = "+id;
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				if (rs.next()) {
					return rs.getString("raavare_navn");
				} else {
					return "ID findes ikke!";
				}
			} catch(SQLException e) {
				return "SQL Fejl";
			}
		}

		public ArrayList<ReceptKomponentDTO> getRaavarerInRecept(int id) {
			try {
				ArrayList<ReceptKomponentDTO> receptkomponenter = new ArrayList<ReceptKomponentDTO>();

				String sql = "SELECT * FROM receptkomponent WHERE recept_id = " + id;
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next())
				{
					receptkomponenter.add(new ReceptKomponentDTO(rs.getInt("recept_id"), rs.getInt("raavare_id"),
							rs.getDouble("nom_netto"), rs.getDouble("tolerance")));
				}

				return receptkomponenter;

			} catch (SQLException e) {
				return null;
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

		public String getReceptIDFromPBID(String id) {
			try {
				String sql = "SELECT recept_id FROM produktbatch WHERE pb_id = " + id;
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				if (rs.next()) {
					return rs.getString("recept_id");
				} else {
					return "ID findes ikke!";
				}
			} catch(SQLException e) {
				return "SQL Fejl";
			}
		}
	}

