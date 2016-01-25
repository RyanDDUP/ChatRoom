package chatRoom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.omg.CORBA.PRIVATE_MEMBER;

/*
 * Server founds
 * Input and output should be independent
 */
public class Server {
	public List<MyChannel> all = new ArrayList<MyChannel>();

	public static void main(String[] args) throws IOException {
		new Server().start();
	}

	public void start() throws IOException {
		ServerSocket server = new ServerSocket(9999);
		while (true) {
			Socket client = server.accept();
			MyChannel channel = new MyChannel(client);
			all.add(channel);
			new Thread(channel).start(); // one way opened
		}
	}

	public class MyChannel implements Runnable {
		@Override
		public void run() {
			while (isRunning) {
				sendOthers(this.receive());
			}

		}

		private DataInputStream dis;
		private DataOutputStream dos;
		private Boolean isRunning = true;
		private String name;

		private MyChannel(Socket client) {
			super();
			try {
				this.dis = new DataInputStream(client.getInputStream());
				this.dos = new DataOutputStream(client.getOutputStream());

				this.name = dis.readUTF();
				System.out.println(this.name + " has come to the ChatRoom!");
				this.send("Welcome to ChatRoom!");
				sendOthers("System Message: " + this.name + " has come into the ChatRoom:)");
			} catch (IOException e) {
				CloseUtil.closeAll(dis, dos);
				isRunning = false;
			}
		}

		/*
		 * receive the message
		 */
		private String receive() {
			String msg = "";
			try {
				msg = dis.readUTF();
			} catch (IOException e) {
				CloseUtil.closeAll(dis, dos);
				isRunning = false;
				all.remove(this);
				System.out.println(this.name + " has left to the ChatRoom.");
				sendOthers("System Message: " + this.name + " has left into the ChatRoom:(");
			}
			return msg;
		}

		/*
		 * send the message to other clients send privatedly
		 */

		private void sendOthers(String msg) {
			System.out.println("this.name:" + msg);

			if (msg.startsWith("@") && msg.indexOf(":") > 0) { // Private
																// Chatting Line

				String name = msg.substring(1, msg.indexOf(":"));
				String content = msg.substring(msg.indexOf(":") + 1);
				for (MyChannel other : all) {
					if (other.name.equals(name)) {

						other.send(this.name + " @ you: " + msg);
					}
				}

			} else {
				for (MyChannel other : all) {
					if (other == this) {
						continue;
					}
					other.send(this.name + ": " + msg);
				}
			}
		}

		private void send(String msg) {
			if (msg == null || msg.equals("")) {
				return;
			}
			try {
				dos.writeUTF(msg);
				dos.flush();
			} catch (IOException e) {
				CloseUtil.closeAll(dis, dos);
				isRunning = false;
				all.remove(this);
				System.out.println(this.name + " has left to the ChatRoom.");
				sendOthers("System Message: " + this.name + " has left into the ChatRoom:(");
			}
		}
	}
}
