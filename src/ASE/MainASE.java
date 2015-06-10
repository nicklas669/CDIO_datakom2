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
		boolean bError = true;
		// Først skal operatøren logge ind

		try {
			// Forbindelse til vægten oprettes
			String response;


			Socket clientSocket = new Socket("localhost", 8000);

			outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			inputServer = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));


			//Første to linjer spises
			response = inputServer.readLine();
			System.out.println(response);
			response = inputServer.readLine();
			System.out.println(response);

			//Slå fra på rigtig vægt
			outToServer.writeBytes("P111 \"Blank\"" + '\n');
			response = inputServer.readLine();
			inputServer.readLine();




			do {
				//Først findes gyldigt operatør nummer
				writeRM20ToScale(4, "Operator ID?", "", "");
				response = readRM20FromScale();
				System.out.println(response);
				response = datalayer.getOprNameFromID(response);
			} while ("ID findes ikke!".equals(response));

			System.out.println(response);

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
	 * @param type - 4 = integer, 8 = alphanum
	 * @param text1 - The string to be displayed on the scale (max. 24 chars).
	 * @param text2 - text2 Text/value to be displayed as default, and to be overwritten by user input.
	 * @param text3 - Unit (max. 7 characters).
	 */
	private static void writeRM20ToScale(int type, String text1, String text2,
			String text3) {
		try {
			System.out.println("Sender RM20 omkring \""+text1+"\"");
			//			outToServer.writeBytes("RM20 4 \"Operatør nummer?\" \"\" \"\" " + "\n\r");
			outToServer.writeBytes("RM20 "+type+" \""+text1+"\" \""+text2+"\" \""+text3+"\"\r\n");
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
			}
			else if (response.startsWith("RM20 I")) {
				System.out.println("Command understood but not executable at the moment.");
				break;
			}
			else if (response.startsWith("RM20 L")) {
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
			}
			else if (response.startsWith("RM20 C")) {
				System.out.println("RM20 afbrudt på vægt.");
				RM20_status = false;
			}
		}
		return "RM20 afbrudt på vægt.";
	}

}
