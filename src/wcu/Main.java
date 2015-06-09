package wcu;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class Main {
	static Socket clientSocket;
	static DataOutputStream outToScale;
	static BufferedReader inFromScale;
	static Map<Integer, String> items = new HashMap<Integer, String>();
	static String response = "";
	static boolean RM20_status = false;
	static int item = -1;
	static boolean realScale = false;
	static int operator = 0;
	static double tara, brutto;

	public static void main(String[] args) throws InterruptedException, IOException {
		//readStoreFile("res/store.txt");

		// Opens connection to scale
		// Real Mettler Scale has IP 169.254.2.2 and uses port 8000
		while (!openConnection("localhost", 8000)) {
			System.out.println("Fejl i forbindelse, prøver igen...");
			Thread.sleep(500);
		}

		// Der udsendes I4 A "3154308" når vægten starter...
		if (realScale) inFromScale.readLine(); // Read startup message from Scale

		//		outToServer.writeBytes("RM20 4 text1 text2 text3\r\n");
		//		outToServer.writeBytes("RM20 4 \"text1\" \"text2\" \"text3\"\r\n");

		//		outToServer.writeBytes("RM20 4 \"Indtast operatoernummer\" \"\" \"\"\r\n");  // Virker fint på vægt!
		//		outToServer.writeBytes("P111 \"\"\r\n");
		//		outToServer.writeBytes("D \"1234567\" \r\n"); // Virker på vægt men 7 karakterer maks.
		//		outToServer.writeBytes("DW\r\n");
		//		System.out.println(inFromServer.readLine());
		//		outToServer.writeBytes("S\r\n"); // Virker på vægt
		
		System.out.println(inFromScale.readLine());
		System.out.println(inFromScale.readLine());
		outToScale.writeBytes("S\r\n");
		System.out.println(inFromScale.readLine());
		inFromScale.readLine();
		writeRM20ToScale(4, "Operatoer nr?", "", "");
		readRM20FromScale();
		
		
		// TODO: VALIDATION AF OPERATOR ID
		// 3. BED OM OPERATØR ID
//		if (operator == 0) {
//			writeRM20ToScale(4, "Operatoer nr?", " ", " ");
//			operator = Integer.valueOf(readRM20FromScale());
//		}
		
		// TODO: 4. VÆGTEN SKAL SVARE MED OPERATØR NAVN SOM SÅ SKAL GODKENDES

		// TODO: 5. OPERATØR INDTASTER PRODUKTBATCH NUMMER
		
		// TODO: 6. VÆGTEN SVARER TILBAGE MED NAVN PÅ RECEPT DER SKAL PRODUCERES (f.eks.: saltvand med citron)
		
		// TODO: 7. OPERATØR KONTROLLERER AT VÆGT ER UBELASTET OG TRYKKER OK
		
		// TODO: 8. SYSTEMET SÆTTER PRODUKTBATCH NUMMERETS STATUS TIL "UNDER PRODUKTION"
		
		// TODO: 9. VÆGTEN TARERES
		
		// TODO: 10. VÆGTEN BEDER OM FØRSTE TARA BEHOLDER
		
		// TODO: 11. OPERATØR PLACERER FØRSTE TARABEHOLDER OG TRYKKER "OK"
		
		// TODO: 12. VÆGTEN AF TARABEHOLDER REGISTRERES
		
		// TODO: 13. VÆGTEN TARERES
		
		// TODO: 14. VÆGTEN BEDER OM RAAVAREBATCH NUMMER PÅ FØRSTE RÅVARE
		
		// TODO: 15. OPERATØREN AFVEJER OP TIL DEN ØNSKEDE MÆNGDE OG TRYKKER "OK"
		
		// TODO: 16. PKT. 7-14 GENTAGES INDTIL ALLE RÅVARER ER AFVEJET
		
		// TODO: 17. SYSTEMET SÆTTER PRODUKTBATCH NUMMERETS STATUS TIL "AFSLUTTET"
		
		// TODO: 18. DET KAN HEREFTER GENOPTAGES AF EN NY OPERATØR.
		
		// Ask for item number
//		do {
//			writeRM20ToScale(4, "Vare nr?", " ", " ");
//			// Add check if an integer is received...
//			item = Integer.valueOf(readRM20FromScale());
//		} while (!itemExists(item));
//
//		// Write item name to Scale and ask for operator acceptance
//		writeRM20ToScale(8, items.get(item)+ " - Ja/nej?", " ", " ");
//		response = readRM20FromScale();
//		if ("JA".equals(response.trim().toUpperCase())) System.out.println("DER BLEV SAGT JA!!");
//		else System.out.println("Der blev sgu sagt nej tak til den vare!!");
//		
//		// Instruct operator to place bowl/container and acknowledge
//		writeRM20ToScale(8, "Saet skaal og svar OK", " ", " ");
//		response = readRM20FromScale();
//		if ("OK".equals(response.trim().toUpperCase())) System.out.println("DER BLEV SAGT OK!!");
//		else System.out.println("Der blev sgu svaret noget random shit i stedet for OK!!");
//		
//		// Tare weight and register tara
//		tareScale();
//		//evt skriv P111 her om at vægten er tareret?
//		tara = Double.valueOf(readTare().split(" ")[7]);
//		
//		// Instruct operator to top up product and acknowledge
//		writeRM20ToScale(8, "Afvej og svar OK", " ", " ");
//		response = readRM20FromScale();
//		if ("OK".equals(response.trim().toUpperCase())) System.out.println("DER BLEV SAGT OK!!");
//		else System.out.println("Der blev sgu svaret noget random shit i stedet for OK!!");
//		
//		// Register netto (brutto?)
//		brutto = Double.valueOf(readBruttoFromScale().split(" ")[7]);
////		System.out.println(brutto);
//		//evt skriv P111 her om at brutto er registreret?
//
//		// Instruct operator to remove tara and netto 
//		writeRM20ToScale(8, "Fjern skaal og svar OK", " ", " ");
//		response = readRM20FromScale();


	}

//	private static String readBruttoFromScale() {
//		try {
//			outToScale.writeBytes("S\r\n");
//			return inFromScale.readLine();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private static String readTare() {
//		try {
//			return inFromScale.readLine();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private static void tareScale() {
//		try {
//			outToScale.writeBytes("T\r\n");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static boolean itemExists(int item) {
//		if (!items.containsKey(item)) {
//			//			writeP111ToScale("Vare nr. "+item+" ikke fundet!");
//			System.out.println("Vare nr. "+item+" ikke fundet!");
//			// Write response to Scale with P111?
//			return false;
//		}
//		else { 
//			//			writeP111ToScale("Vare nr. "+item+" "+ "("+items.get(item)+") fundet.");
//			System.out.println("Vare nr. "+item+" "+ "("+items.get(item)+") fundet.");
//			// Write response to Scale with P111?
//			return true;
//		}
//	}
//
//
//	private static void writeP111ToScale(String string) {
//		try {
//			//			outToScale.writeBytes("P111 "+string+"\r\n");
//			outToScale.writeBytes("P111 \""+string+"\"\r\n");
//			System.out.println("P111: "+inFromScale.readLine());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//
	/**
	 * Reads from scale until RM20 response is received.
	 * @throws IOException
	 */
	private static String readRM20FromScale() throws IOException {
		while (!RM20_status) {
			response = inFromScale.readLine();
			inFromScale.readLine();
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
			response = inFromScale.readLine();
			inFromScale.readLine();
			if (response.startsWith("RM20 A")) {
				response = response.split(" ")[2];
				// Validate here if response is an integer?
				System.out.println("Svar fra operatør: "+response);
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
			outToScale.writeBytes("RM20 "+type+" \""+text1+"\" \""+text2+"\" \""+text3+"\"\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	/**
//	 * Opens socket to specified IP and port and sets up Input- and OutputStream.
//	 * @param ip
//	 * @param port
//	 * @return Returns true if connection succeeds, else returns false.
//	 */
	private static boolean openConnection(String ip, int port) {
		try {
			clientSocket = new Socket(ip, port);
//			outToScale = new PrintWriter(clientSocket.getOutputStream(), true);
			outToScale = new DataOutputStream(clientSocket.getOutputStream());
//			outToScale = new DataOutputStream(clientSocket.getOutputStream());
			inFromScale = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			return true;
		} catch (UnknownHostException e) {
			return false;
		}
		catch (IOException e) {
			return false;
		}
	}
//
//
//	/**
//	 * Reads the file specified and adds "varenummer, varenavn" of each line into a dictionary.
//	 * @param path
//	 */
//	public static void readStoreFile(String path) {
//		try {
//			BufferedReader storeFile = new BufferedReader(new FileReader(path));
//			System.out.println("Indlæser varelager...");
//			String line;
//			while ((line = storeFile.readLine()) != null) {
//				String[] lineSplit = line.split(", ");
//				items.put(Integer.valueOf(lineSplit[0]), lineSplit[1]);
//			}
//			storeFile.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//	}
}
