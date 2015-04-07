import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.*;

public class Main {

	public static void main(String[] args) throws SocketException, IOException {
		// TODO Auto-generated method stub
		FTPClient ftp = new FTPClient();
		ftp.connect("localhost");
		ftp.login("root", "");
		System.out.println("Reply code: "+ftp.getReplyCode());
		FTPFile[] files = ftp.listFiles();
		for (FTPFile file : files)	System.out.println(file.getName());
		
		FileOutputStream fos = new FileOutputStream("OSTEJOHN");
		String remoteFilePath = "";
		ftp.retrieveFile(remote, fos);
	}

}
