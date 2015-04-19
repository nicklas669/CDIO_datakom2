package wcu;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

	public static void main(String[] args) throws InterruptedException, IOException {
		readStoreFile("res/store.txt");

		while (!openConnection("localhost", 4567)) {
			System.out.println("Fejl i forbindelse, prøver igen...");
			Thread.sleep(500);
		}


		writeRM20ToScale(4, "Operatør nr?", " ", " ");
		readRM20FromScale();

		
		do {
			writeRM20ToScale(4, "Vare nr?", " ", " ");
			// Add check if an integer is received...
			item = Integer.valueOf(readRM20FromScale());
		} while (!itemExists(item));
		
	}


	private static boolean itemExists(int item) {
		if (!items.containsKey(item)) {
			System.out.println("Vare nr. "+item+" ikke fundet!");
			// Write response to Scale with P111?
			return false;
		}
		else { 
			System.out.println("Vare nr. "+item+" "+ "("+items.get(item)+") fundet.");
			// Write response to Scale with P111?
			return true;
		}
	}


	/**
	 * Reads from scale until RM20 response is received.
	 * @throws IOException
	 */
	private static String readRM20FromScale() throws IOException {
		while (!RM20_status) {
			response = inFromScale.readLine();
			//			System.out.println(response);
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
	 * @param text1 - The string to be displayed on the scale.
	 * @param text2
	 * @param text3
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

	/**
	 * Opens socket to specified IP and port and sets up Input- and OutputStream.
	 * @param ip
	 * @param port
	 * @return Returns true if connection succeeds, else returns false.
	 */
	private static boolean openConnection(String ip, int port) {
		try {
			clientSocket = new Socket("localhost", 4567);
			outToScale = new DataOutputStream(clientSocket.getOutputStream());
			inFromScale = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			return true;
		} catch (UnknownHostException e) {
			return false;
		}
		catch (IOException e) {
			return false;
		}
	}


	/**
	 * Reads the file specified and adds "varenummer, varenavn" of each line into a dictionary.
	 * @param path
	 */
	public static void readStoreFile(String path) {
		try {
			BufferedReader storeFile = new BufferedReader(new FileReader(path));
			System.out.println("Indlæser varelager...");
			String line;
			while ((line = storeFile.readLine()) != null) {
				String[] lineSplit = line.split(", ");
				items.put(Integer.valueOf(lineSplit[0]), lineSplit[1]);
			}
			storeFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
