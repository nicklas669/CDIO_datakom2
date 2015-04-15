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
	static DataOutputStream outToServer;
	static BufferedReader inFromServer;
	static Map<Integer, String> items = new HashMap<Integer, String>();

	public static void main(String[] args) {
		readStoreFile("res/store.txt");
		try {
			clientSocket = new Socket("localhost", 4567);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			// 4 = integer, 8 = alphanum
			//			outToServer.writeBytes("RM20 4 text1 text2 text3\r\n");
			outToServer.writeBytes("RM20 4 \"text1\" \"text2\" \"text3\"\r\n");
			while (true) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(inFromServer.readLine());
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Reads the file specified and adds "varenummer, varenavn" of each line into a dictionary.
	 * @param path
	 */
	public static void readStoreFile(String path) {
		try {
			BufferedReader storeFile = new BufferedReader(new FileReader(path));
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
	
	public static boolean checkItemExists(int item) {
		if (items.get(item) != null) return true;
		return false;
	}

}
