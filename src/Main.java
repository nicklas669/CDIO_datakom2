import java.io.IOException;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	static String ip,user,pw;
	static int port;
	static Scanner sc;
	static String temp, fileDest;
	static int choice;
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		
		FTP ftp = new FTP();
		Thread FTP = new Thread(ftp);
		FTP.start();
		
		sc = new Scanner(System.in);
		System.out.println("** FTP-klient v. 1.0 **");
		System.out.println("** Forbind til FTP server. **");
		System.out.print("Indtast IP på server: ");
		ip = sc.nextLine();
		System.out.print("Indtast port(typisk 21): ");
		try {
			port = sc.nextInt();
		} catch (InputMismatchException e1) {
			System.out.println("** Portnummer skal være en integer! **");
		}
		sc.nextLine();
		
		System.out.println("Forsøger at forbinde, vent venligst...");
		while (!ftp.openCmdConnection(ip, port)) {
			System.out.println("Fejl i forbindelse. Prøv igen.");
			System.out.print("Indtast IP på server: ");
			ip = sc.nextLine();
			System.out.print("Indtast port(typisk 21): ");
			try {
				port = sc.nextInt();
			} catch (InputMismatchException e1) {
				System.out.println("Portnummer skal være en integer!");
			}
			System.out.println("Forsøger at forbinde, vent venligst...");
			sc.nextLine();
		}
		
		System.out.println("** Forbundet til FTP server **");
		System.out.println("** Log ind på FTP server. **");
		System.out.print("Indtast brugernavn: ");
		user = sc.nextLine();	
		System.out.print("Indtast password: ");
		pw = sc.nextLine();
		
		System.out.println("Forsøger at logge ind, vent venligst...");
		while (!ftp.logIn(user, pw)) {
			System.out.println("Fejl i log ind. Prøv igen.");
			System.out.print("Indtast brugernavn: ");
			user = sc.nextLine();	
			System.out.print("Indtast password: ");
			pw = sc.nextLine();
			System.out.println("Forsøger at logge ind, vent venligst...");
		}
		System.out.println("** Logget ind på FTP server **");
		System.out.println("Tryk på en tast for at komme til menuen.");
		temp = sc.nextLine();
		
		while (true) {
			for (int i = 0; i<30; i++) System.out.println("");
			System.out.println("** FTP-klient v. 1.0 **");
			System.out.println("Det nuværende working directory er: "+ftp.printWorkDir());
			System.out.println("1. Liste over filer og mapper i working directory.");
			System.out.println("2. Skift working directory.");
			System.out.println("3. Hent en fil fra server.");
			System.out.println("4. Afslut program.");
			System.out.print("Valg: ");
			try {
				choice = sc.nextInt();
			} catch (Exception e) {
				System.out.println("Ugyldigt valg. Prøv igen.");
				sc.nextLine();
				continue;
			}
			sc.nextLine();
			switch (choice) {
			case 1:
				for (int i = 0; i<30; i++) System.out.println("");
				System.out.println("** Liste over filer og mapper i working directory **");
				for (String entry : ftp.printWorkDirContents()) System.out.println(entry);
				System.out.println("Tryk på en tast for at komme tilbage til menuen.");
				temp = sc.nextLine();
				break;
			case 2:
				for (int i = 0; i<30; i++) System.out.println("");
				System.out.println("** Skift working directory **");
				System.out.print("Indtast navnet på mappen for det nye working directory: ");
				temp = sc.nextLine();
				ftp.changeWorkDir(temp);
				System.out.println("Tryk på en tast for at komme tilbage til menuen.");
				temp = sc.nextLine();
				break;
			case 3:
				for (int i = 0; i<30; i++) System.out.println("");
				System.out.println("** Hent en fil fra server **");
				System.out.println("Indtast navnet på filen(inkl. filtype) der skal hentes fra working directory (f.eks. Tekstfil.txt): ");
				temp = sc.nextLine();
				System.out.println("Indtast stien til den lokale destination for filen(f.eks. C:/Users/Brugernavn/Desktop/): ");
				fileDest = sc.nextLine();
				System.out.println(ftp.downloadFile(fileDest, temp));
				System.out.println("Tryk på en tast for at komme tilbage til menuen.");
				temp = sc.nextLine();
				break;
			case 4:
				ftp.closeConnections();
				System.out.println("Afslutter programmet.");
				System.exit(0);
				break;
			default:
				System.out.println("Ugyldigt valg. Prøv igen.");
				break;
			}
		}
		
		
		
	}
	
}
