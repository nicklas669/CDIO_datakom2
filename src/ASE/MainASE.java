package ASE;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MainASE {

	static String response = "";
	static boolean RM20_status = false;

	static BufferedReader inputServer;
	static DataOutputStream outToServer;

	public static void main(String[] args) {
		DAL datalayer = new DAL();

		String opr_name = "";
		String recept_navn;
		String produktbatch_id;
		// Først skal operatøren logge ind

		try {
			// Forbindelse til vægten oprettes
			String response;

			Socket clientSocket = new Socket("localhost", 8000);

			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inputServer = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			// Første to linjer spises
			response = inputServer.readLine();
			System.out.println(response);
			response = inputServer.readLine();
			System.out.println(response);

			// Slå fra på rigtig vægt
			outToServer.writeBytes("P111 \"Blank\"" + '\n');
			inputServer.readLine();
			inputServer.readLine();

			boolean loopOne = false;
			while (true) {

				// Prompt for gyldigt operatør nummer på vægt
				System.out.println("Going in response is:" + response);
				do {
					writeRM20ToScale(4, "Operator ID?", "", "");
					response = readRM20FromScale();
					opr_name = datalayer.getOprNameFromID(response);
				} while ("ID findes ikke!".equals(opr_name) || "SQL fejl".equals(opr_name));

				// Prompt for om navnet er korrekt på vægt
				loop1: while (true) {

					writeRM20ToScale(4, opr_name + "?(Y/N)", "", "");
					response = readRM20FromScale().toUpperCase();
					if (response.equals("Y")) {
						// Hvis respons er Y breakes ud af while loopet
						loopOne = true;
						break loop1;

					} else if (response.equals("N")) {
						// Hvis respons er N skal nyt operatør nummer indtastes,
						// break til loop1
						break;
					} else {
						// Hvis respons hverken er Y eller N spørges igen ved
						// break til loop2
						continue;
					}
				}
				if (loopOne) {
					break;
				}
			}

			System.out.println(opr_name + " is locked in as the using operator");

			// Operatør rved vægt spørges om hvilken produktbatch han skal lave


			// Prompt for gyldigt produktbatch

			do {
				writeRM20ToScale(4, "Produktbatch ID?", "", "");
				response = readRM20FromScale();
				produktbatch_id = response;
				recept_navn = datalayer.getReceptNavnFromPBID(response);
			} while ("ID findes ikke!".equals(recept_navn) || "SQL Fejl".equals(recept_navn));

			System.out.println("Got: " + recept_navn);

			// Operatøren kontrollerer at vægten er ubelastet og trykker ’ok’

			do {
				writeRM20ToScale(4, "Vægt ubelastet?(OK)", "", "");
				response = readRM20FromScale().toUpperCase();
			} while (!"OK".equals(response));

//			8: Systemet sætter produktbatch nummerets status til ”Under produktion”.
			datalayer.setProduktBatchStatus(Integer.valueOf(produktbatch_id), 1);
			outToServer.writeBytes("P111 \"Produktbatch er nu Under Produktion\"" + '\n');
			inputServer.readLine();
			inputServer.readLine();
			
			System.out.println("Goodbye");

			clientSocket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Sends an RM20 command to the scale.
	 * 
	 * @param type
	 *            - 4 = integer, 8 = alphanum
	 * @param text1
	 *            - The string to be displayed on the scale (max. 24 chars).
	 * @param text2
	 *            - text2 Text/value to be displayed as default, and to be
	 *            overwritten by user input.
	 * @param text3
	 *            - Unit (max. 7 characters).
	 */
	private static void writeRM20ToScale(int type, String text1, String text2,
			String text3) {
		try {
			System.out.println("Sender RM20 omkring \"" + text1 + "\"");
			// outToServer.writeBytes("RM20 4 \"Operatør nummer?\" \"\" \"\" " +
			// "\n\r");
			outToServer.writeBytes("RM20 " + type + " \"" + text1 + "\" \""
					+ text2 + "\" \"" + text3 + "\"\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String readRM20FromScale() throws IOException {
		while (!RM20_status) {
			response = inputServer.readLine();
			inputServer.readLine();
			if (response.startsWith("RM20 B")) {
				System.out.println("Command executed, user input follows.");
				RM20_status = true;
			} else if (response.startsWith("RM20 I")) {
				System.out
				.println("Command understood but not executable at the moment.");
				break;
			} else if (response.startsWith("RM20 L")) {
				System.out.println("Command understood but parameter wrong.");
				break;
			}
		}

		while (RM20_status) {
			response = inputServer.readLine();
			inputServer.readLine();
			if (response.startsWith("RM20 A")) {
				response = response.split(" ")[2];
				// Validate here if response is an integer?
				RM20_status = false;
				return response;
			} else if (response.startsWith("RM20 C")) {
				System.out.println("RM20 afbrudt på vægt.");
				RM20_status = false;
			}
		}
		return "RM20 afbrudt på vægt.";
	}

}
