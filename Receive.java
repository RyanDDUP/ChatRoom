package chatRoom;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/*
 * receive msg
 *
 */
public class Receive implements Runnable{
	private DataInputStream dis;
	private Boolean isRunning = true;

	private Receive() {
		
	}
	public Receive(Socket client) {
		try {
			this.dis = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			isRunning = false;
			CloseUtil.closeAll(dis);
		}
	}
	public String receive() {
		String msg = "";
		try {
			msg = dis.readUTF();
		} catch (IOException e) {
			isRunning = false;
			CloseUtil.closeAll(dis);
		}
		return msg;
	} 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning) {
			System.out.println(receive());
		}
	}

}
