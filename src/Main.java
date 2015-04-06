import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.commons.net.ftp.*;

public class Main {

	public static void main(String[] args) throws SocketException, IOException {
		// TODO Auto-generated method stub
		FTPClient ftp;
		ftp = new FTPClient();
		ftp.connect("localhost");
		System.out.println("Reply code: "+ftp.getReplyCode());
	}

}
