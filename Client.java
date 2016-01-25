package chatRoom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.stream.Stream;

/*
 * Create Client: OutputStream + InputStream
 */
public class Client {
	public static void main(String[] args) throws Exception {
		Socket client = new Socket("localhost", 9999);
		System.out.println("Please type in your User-name:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String name = br.readLine();
		String regex = "{10,}";
		if (name.equals("") || name.equals("")) {
			return;
		}
		new Thread(new Send(client, name)).start();
		new Thread(new Receive(client)).start();

	}
}
