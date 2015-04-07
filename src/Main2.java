import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;


public class Main2 {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 21);
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		System.out.println(reader.readLine());
		System.out.println(reader.readLine());
		System.out.println(reader.readLine());
		
		// Log ind
		System.out.println("USER root\r\n");
		writer.write("USER root\r\n");
		writer.flush();
		System.out.println(reader.readLine());
		
		System.out.println("PASS \r\n");
		writer.write("PASS \r\n");
		writer.flush();
		System.out.println(reader.readLine());
		
		System.out.println("PWD \r\n");
		// Print working directory
		writer.write("PWD \r\n");
		writer.flush();
		System.out.println(reader.readLine());
		
		System.out.println("PASV \r\n");
		writer.write("PASV \r\n");
		writer.flush();
		String response = reader.readLine();
		System.out.println(response);
		
		// Opret data forbindelse
		String ip = null;
	    int port = -1;
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
	        throw new IOException("SimpleFTP received bad data link information: "
	            + response);
	      }
	    }
	    System.out.println(ip+", "+port);
	    
	    Socket dataSocket = new Socket(ip, port);
	    BufferedReader reader2 = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
	    System.out.println(reader2.readLine());
	    
	    
	    // Print filer i working directory
		System.out.println("LIST \r\n");
		writer.write("LIST \r\n");
		writer.flush();
		System.out.println(reader.readLine());
	}

}
