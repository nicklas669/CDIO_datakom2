import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
	static String ip,user,pw;
	static Scanner sc;
	static int port;
	static Socket cmdConnection;
	static BufferedReader cmdReader;
	static BufferedWriter cmdWriter;
	static Socket dataSocket;
	static BufferedReader dataReader;
	static BufferedInputStream fileReader;
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		
		FTP ftp = new FTP();
		Thread FTP = new Thread(ftp);
		FTP.start();
		
		sc = new Scanner(System.in);
		System.out.println("FTP Osteklient v. 0.8");
//		System.out.println("Forbind til FTP server.");
//		System.out.print("Indtast IP: ");
//		ip = sc.nextLine();
//		System.out.print("Indtast port: ");
//		port = sc.nextInt();
		ip = "localhost";
		port = 21;
		
		
		// Åbn forbindelse til kommandoer
		ftp.openCmdConnection(ip, port);
		
//		System.out.println("Log ind p� FTP server.");
//		System.out.print("Indtast brugernavn: ");
//		user = sc.nextLine();	
//		System.out.print("Indtast password: ");
//		pw = sc.nextLine();
		
		user = "root";
		pw = "";
		// Log ind
		ftp.logIn(user, pw);
		
		// Print working directory
		ftp.printWorkDir();
		
	    // Åbn data forbindelse
//		openDataConnection(cmdReader, cmdWriter);
	    
//	    // Print filer i working directory
//		cmdWriter.write("LIST \r\n");
//		cmdWriter.flush();
//		System.out.println(cmdReader.readLine());
//		while (dataReader.ready()) System.out.println(dataReader.readLine());
//		System.out.println(cmdReader.readLine());
//		
//		// Skift working directory
//		changeWorkDir("pseudofolder");
//		
//		// Download fil
//		System.out.println(downloadFile(cmdReader, cmdWriter, 
//				"C:/Users/Mogens/Desktop/", "Cmdlineargumenttest.java"));
		
		// Close up connections
//		closeConnections();
	}
	
}
