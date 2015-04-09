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
import java.util.StringTokenizer;


public class Main {
	static String ip;
	static int port;
	static Socket cmdConnection;
	static BufferedReader cmdReader;
	static BufferedWriter cmdWriter;
	static Socket dataSocket;
	static BufferedReader dataReader;
	static BufferedInputStream fileReader;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		// Åbn forbindelse til kommandoer
		openCmdConnection("localhost", 21);
		
		// Log ind
		logIn("root", " ");
		
		// Print working directory
		printWorkDir();
		
	    // Åbn data forbindelse
		openDataConnection(cmdReader, cmdWriter);
	    
	    // Print filer i working directory
		cmdWriter.write("LIST \r\n");
		cmdWriter.flush();
		System.out.println(cmdReader.readLine());
		while (dataReader.ready()) System.out.println(dataReader.readLine());
		System.out.println(cmdReader.readLine());
		
		// Skift working directory
		changeWorkDir("pseudofolder");
		
		// Download fil
		System.out.println(downloadFile(cmdReader, cmdWriter, 
				"C:/Users/Mogens/Desktop/", "Cmdlineargumenttest.java"));
		
		// Close up connections
		closeConnections();
	}
	
	private static void changeWorkDir(String folder) throws IOException {
		cmdWriter.write("CWD "+folder+"\r\n");
		cmdWriter.flush();
		System.out.println(cmdReader.readLine());
	}

	private static void openCmdConnection(String ip, int port) throws UnknownHostException, IOException {
		cmdConnection = new Socket(ip, port);
		cmdReader = new BufferedReader(new InputStreamReader(cmdConnection.getInputStream()));
		cmdWriter = new BufferedWriter(new OutputStreamWriter(cmdConnection.getOutputStream()));
		
		System.out.println(cmdReader.readLine());  
		System.out.println(cmdReader.readLine()); // welcome message is first 3 lines for FileZilla server
		System.out.println(cmdReader.readLine());
	}

	private static void logIn(String username, String pass) throws IOException {
		cmdWriter.write("USER "+username+"\r\n");
		cmdWriter.flush();
		System.out.println(cmdReader.readLine());
		
		cmdWriter.write("PASS "+pass+"\r\n");
		cmdWriter.flush();
		System.out.println(cmdReader.readLine());
	}

	private static void printWorkDir() throws IOException {
		cmdWriter.write("PWD \r\n");
		cmdWriter.flush();
		System.out.println(cmdReader.readLine());
	}

	private static void closeConnections() throws IOException {
		System.out.println("Lukker forbindelser!");
		cmdReader.close();
		cmdWriter.close();
		cmdConnection.close();
		
		fileReader.close();
		dataReader.close();
		dataSocket.close();
	}
	
	private static String downloadFile(BufferedReader cmdReader,
			BufferedWriter cmdWriter, 
			String localPath, String fileName) throws UnknownHostException, IOException {
		
		openFileTransferConnection(cmdReader, cmdWriter);
		cmdWriter.write("RETR "+fileName+" \r\n");
		cmdWriter.flush();
		
		String response = cmdReader.readLine();
		if (!response.startsWith("150")) return response; // return hvis fil ikke findes
		
		File targetFile = new File(localPath+fileName);
		OutputStream targetStream = new FileOutputStream(targetFile);
		byte[] buffer = new byte[8 * 1024];
		int bytesRead;
	
		while ((bytesRead = fileReader.read(buffer)) != -1) {
			targetStream.write(buffer, 0, bytesRead);
		}
//		System.out.println(cmdReader.readLine());
		return cmdReader.readLine();
	}
	
	private static void openFileTransferConnection(BufferedReader cmdReader,
			BufferedWriter cmdWriter) throws UnknownHostException, IOException {
		cmdWriter.write("PASV \r\n");
		cmdWriter.flush();
		String response = cmdReader.readLine();
		System.out.println(response);
		
		// Fang IP og port angivet af server
	    int opening = response.indexOf('(');
	    int closing = response.indexOf(')', opening + 1);
	    if (closing > 0) {
	      String dataLink = response.substring(opening + 1, closing);
	      StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
	      try {
	        ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
	            + tokenizer.nextToken() + "." + tokenizer.nextToken();
	        port = Integer.parseInt(tokenizer.nextToken()) * 256
	            + Integer.parseInt(tokenizer.nextToken());
	      } catch (Exception e) {
	        throw new IOException("Received bad data link information: "+ response);
	      }
	    }
	    
		dataSocket = new Socket(ip, port);
		fileReader = new BufferedInputStream(dataSocket.getInputStream());
	}
	
	
	private static void openDataConnection(BufferedReader cmdReader,
			BufferedWriter cmdWriter) throws UnknownHostException, IOException {
		cmdWriter.write("PASV \r\n");
		cmdWriter.flush();
		String response = cmdReader.readLine();
		System.out.println(response);
		
		// Fang IP og port angivet af server
	    int opening = response.indexOf('(');
	    int closing = response.indexOf(')', opening + 1);
	    if (closing > 0) {
	      String dataLink = response.substring(opening + 1, closing);
	      StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
	      try {
	        ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
	            + tokenizer.nextToken() + "." + tokenizer.nextToken();
	        port = Integer.parseInt(tokenizer.nextToken()) * 256
	            + Integer.parseInt(tokenizer.nextToken());
	      } catch (Exception e) {
	        throw new IOException("Received bad data link information: "+ response);
	      }
	    }
	    
		dataSocket = new Socket(ip, port);
	    dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
	}
}
