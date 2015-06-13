package ASE;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class MainASE {

	static String response = "";
	static boolean RM20_status = false;

	static BufferedReader inputServer;
	static DataOutputStream outToServer;

	static boolean realScale = true;

	public static void main(String[] args) {
		DAL dal = new DAL();

		String opr_id, opr_name = "";
		String recept_navn, recept_id;
		String produktbatch_id;
		String tarabeholder_vaegt, afvejet_vaegt;
		String raavare_navn, raavare_amount, raavare_tolerance, raavarebatch_id;
		ArrayList<ReceptKomponentDTO> receptkomponenter = new ArrayList<ReceptKomponentDTO>();

		// Først skal operatøren logge ind

		try {
			// Forbindelse til vægten oprettes
			String response;
			Socket clientSocket;
			if (realScale) clientSocket = new Socket("169.254.2.3", 8000);
			else clientSocket = new Socket("localhost", 8000);

			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inputServer = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			// Første to linjer spises
			response = inputServer.readLine();
			System.out.println(response);
			if (!realScale) { // Der læses kun én linje hvis det er den rigtige vægt
				response = inputServer.readLine();
				System.out.println(response);
			}

			// Slå fra på rigtig vægt
			if (!realScale) {
				outToServer.writeBytes("P111 \"Blank\"" + '\n');
				inputServer.readLine();
				inputServer.readLine();
			}

			boolean loopOne = false;
			while (true) {

				// Prompt for gyldigt operatør nummer på vægt
				do {
					writeRM20ToScale(4, "Operator ID?", "", "");
					opr_id = readRM20FromScale();
					opr_name = dal.getOprNameFromID(opr_id);
				} while ("ID findes ikke!".equals(opr_name) || "SQL fejl".equals(opr_name));

				// Prompt for om navnet er korrekt på vægt
				loop1: while (true) {

					writeRM20ToScale(8, opr_name + "?(Y/N)", "Y", "");
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

			// 5: Operatøren indtaster produktbatch nummer.

			// Prompt for gyldigt produktbatch

			do {
				writeRM20ToScale(4, "Produktbatch ID?", "", "");
				response = readRM20FromScale();
				produktbatch_id = response;
				recept_id = dal.getReceptIDFromPBID(produktbatch_id);
				recept_navn = dal.getReceptNavnFromPBID(produktbatch_id);
			} while ("ID findes ikke!".equals(recept_navn) || "SQL Fejl".equals(recept_navn));

			// 6: Vægten svarer tilbage med navn på recept der skal produceres (eks: saltvand med citron)
			outToServer.writeBytes("P111 \""+recept_navn+"\"" + '\n');
			inputServer.readLine();
			if (!realScale) inputServer.readLine();

			// 16: Pkt. 7 – 15 gentages indtil alle råvarer er afvejet.
			// Hent alle råvarer i en recept
			receptkomponenter = dal.getRaavarerInRecept(Integer.valueOf(recept_id));

			for (ReceptKomponentDTO rk : receptkomponenter) {
				// 7: Operatøren kontrollerer at vægten er ubelastet og trykker ’ok’

				do {
					writeRM20ToScale(8, "Vægt ubelastet?(OK)", "OK", "");
					response = readRM20FromScale().toUpperCase();
				} while (!"OK".equals(response));

				// 8: Systemet sætter produktbatch nummerets status til ”Under produktion”.
				dal.setProduktBatchStatus(Integer.valueOf(produktbatch_id), 1);
				outToServer.writeBytes("P111 \"Produktbatch er nu Under Produktion\"" + '\n');
				inputServer.readLine();
				if (!realScale) inputServer.readLine();

				// 9: Vægten tareres
				outToServer.writeBytes("T" + '\n');
				System.out.println(inputServer.readLine());
				if (!realScale) inputServer.readLine();

				// DER ER ET PROBLEM HER, HVOR MAN IKKE KAN SIMULERE AT DER PLACERES EN BEHOLDER
				// (MAN KAN IKKE SKRIVE "B 2.12" FORDI DER SKAL SVARES PÅ RM20).....
				// LØSNING?? Vi kan lave B-kommandoen tilgængelig eksternt 	i vægt simulatoren

				// 10: Vægten beder om første tara beholder. 11: Operatør placerer første tarabeholder og trykker ’ok’.
				if (!realScale) {
					outToServer.writeBytes("B 0.125" + '\n');
					System.out.println(inputServer.readLine());
					inputServer.readLine();
				}

				do {
					writeRM20ToScale(8, "Sæt beholder på (OK)", "OK", "");
					response = readRM20FromScale().toUpperCase();
				} while (!"OK".equals(response));

				// 12: Vægten af tarabeholder registreres
				outToServer.writeBytes("S" + '\n');
				tarabeholder_vaegt = inputServer.readLine().split(" ")[7].replaceAll(",", ".");
				if (!realScale) inputServer.readLine();

				System.out.println("Beholder vægt: "+tarabeholder_vaegt);

				// 13: Vægten tareres.
				outToServer.writeBytes("T" + '\n');
				System.out.println(inputServer.readLine());
				if (!realScale) inputServer.readLine();

				raavare_navn = dal.getRaavareNameFromID(rk.getRvrId());

				// 14: Vægten beder om raavarebatch nummer på første råvare.
				do {
					writeRM20ToScale(4, "RB ID for "+raavare_navn, "", "");
					response = readRM20FromScale();
					raavarebatch_id = dal.getRaavarebatch(Integer.valueOf(response), rk.getRvrId());
				} while ("ID findes ikke!".equals(raavarebatch_id) || "SQL Fejl".equals(raavarebatch_id));

				// 15: Operatøren afvejer op til den ønskede mængde og trykker ’ok’
				raavare_amount = String.valueOf(rk.getNomNetto());
				raavare_tolerance = String.valueOf(rk.getTolerance());

				// Simulér masse placeret på vægt
				if (!realScale) {
					outToServer.writeBytes("B "+rk.getNomNetto() + '\n');
					System.out.println(inputServer.readLine());
					inputServer.readLine();
				}

				do {
					writeRM20ToScale(8, "Afvej "+raavare_amount+" kg", "OK", "");
					response = readRM20FromScale();
					outToServer.writeBytes("S" + '\n');
					afvejet_vaegt = inputServer.readLine().split(" ")[7].replaceAll(",", ".");
					if (!realScale) inputServer.readLine();
					System.out.println("Afvejet: "+afvejet_vaegt);
				} while (!"OK".equals(response) || 
						!(Double.valueOf(afvejet_vaegt) <= Double.valueOf(raavare_amount)+Double.valueOf(raavare_tolerance)) ||
						!(Double.valueOf(afvejet_vaegt) >= Double.valueOf(raavare_amount)-Double.valueOf(raavare_tolerance)));

				dal.insertProduktBatchKomp(Integer.valueOf(produktbatch_id), Integer.valueOf(raavarebatch_id), 
						Double.valueOf(tarabeholder_vaegt.replaceAll(",", ".")), Double.valueOf(afvejet_vaegt.replaceAll(",", ".")), Integer.valueOf(opr_id));
			}

			// 17: Systemet sætter produktbatch nummerets status til ”Afsluttet”.
			dal.setProduktBatchStatus(Integer.valueOf(produktbatch_id), 2);
			outToServer.writeBytes("P111 \"Produktbatch er nu Afsluttet\"" + '\n');
			inputServer.readLine();
			if (!realScale) inputServer.readLine();

			// 18: Det kan herefter genoptages af en ny operatør.
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
			if (!realScale) inputServer.readLine();
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
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		while (RM20_status) {
			response = inputServer.readLine();
			if (!realScale) inputServer.readLine();
			if (response.startsWith("RM20 A")) {
				response = response.split(" ")[2];
				// Validate here if response is an integer or string?
				RM20_status = false;
				return response.replaceAll("\"", "");
			} else if (response.startsWith("RM20 C")) {
				System.out.println("RM20 afbrudt på vægt.");
				RM20_status = false;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "RM20 afbrudt på vægt.";
	}

}
