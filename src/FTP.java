import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class FTP implements Runnable {

	static String ip,user,pw;
	static int port;
	static Socket cmdConnection;
	static BufferedReader cmdReader;
	static BufferedWriter cmdWriter;
	static Socket dataSocket;
	static BufferedReader dataReader;
	static BufferedInputStream fileReader;
	static ArrayList<String> dirContents;
	
	@Override
	public void run() {
		
	}
	
	public ArrayList<String> printWorkDirContents() throws UnknownHostException, IOException {
		openDataConnection(cmdReader, cmdWriter);
		cmdWriter.write("LIST \r\n");
		cmdWriter.flush();
		cmdReader.readLine();
		dirContents = new ArrayList<String>();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (dataReader.ready()) dirContents.add(dataReader.readLine());
		cmdReader.readLine();
		dataSocket.close();
		return dirContents;
	}
	
	public void changeWorkDir(String folder) throws IOException {
		cmdWriter.write("CWD "+folder+"\r\n");
		cmdWriter.flush();
		System.out.println(cmdReader.readLine());
	}

	public boolean openCmdConnection(String ip, int port) throws IOException {
		
		try {
			cmdConnection = new Socket(ip, port);
		} catch (Exception e) {
			return false;
		}
		cmdReader = new BufferedReader(new InputStreamReader(cmdConnection.getInputStream()));
		cmdWriter = new BufferedWriter(new OutputStreamWriter(cmdConnection.getOutputStream()));
		
		while (cmdReader.ready()) cmdReader.readLine(); // læs welcome message fra server
//		cmdReader.readLine();
		return true;
	}

	public boolean logIn(String username, String pass) throws IOException {
		cmdWriter.write("USER "+username+"\r\n");
		cmdWriter.flush();
		cmdReader.readLine();
		
		cmdWriter.write("PASS "+pass+"\r\n");
		cmdWriter.flush();
		
		String response = cmdReader.readLine();
		if (!response.startsWith("230")) return false; // return hvis bruger ikke findes
		return true;
	}

	public String printWorkDir() throws IOException {
		cmdWriter.write("PWD \r\n");
		cmdWriter.flush();
		String response = cmdReader.readLine();
		return response.split(" ")[1];
	}

	public void closeConnections() throws IOException {
		if (cmdReader != null) cmdReader.close();
		if (cmdWriter != null) cmdWriter.close();
		if (cmdConnection != null) cmdConnection.close();
		
		if (fileReader != null) fileReader.close();
		if (dataReader != null) dataReader.close();
		if (dataSocket != null) dataSocket.close();
	}
	
	public String downloadFile(String localPath, String fileName) throws UnknownHostException, IOException {
		
		openFileTransferConnection(cmdReader, cmdWriter);
		cmdWriter.write("RETR "+fileName+" \r\n");
		cmdWriter.flush();
		
		String response = cmdReader.readLine();
		if (!response.startsWith("150")) return response; // return hvis fil ikke findes
		
		if (!localPath.endsWith("/")) localPath += "/";
		File targetFile = new File(localPath+fileName);
		
		OutputStream targetStream;
		try {
			targetStream = new FileOutputStream(targetFile);
			byte[] buffer = new byte[8 * 1024];
			int bytesRead;
			
			while ((bytesRead = fileReader.read(buffer)) != -1) {
				targetStream.write(buffer, 0, bytesRead);
			}
			targetStream.close();
		} catch (FileNotFoundException e) {
			return "Lokal sti er ugyldig.";
		}
		return cmdReader.readLine();
	}
	
	public void openFileTransferConnection(BufferedReader cmdReader,
			BufferedWriter cmdWriter) throws UnknownHostException, IOException {
		cmdWriter.write("PASV \r\n");
		cmdWriter.flush();
		String response = cmdReader.readLine();
		
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
	      } catch (Exception e) { System.out.println("Fejl i tokenizer!");
	      }
	    }
	    
		dataSocket = new Socket(ip, port);
		fileReader = new BufferedInputStream(dataSocket.getInputStream());
	}
	
	
	public void openDataConnection(BufferedReader cmdReader,
			BufferedWriter cmdWriter) throws UnknownHostException, IOException {
		cmdWriter.write("PASV \r\n");
		cmdWriter.flush();
		String response = cmdReader.readLine();
		
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
	      } catch (Exception e) { System.out.println("Fejl i tokenizer!");	      }
	    }
	    
		dataSocket = new Socket(ip, port);
	    dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
	}

}
