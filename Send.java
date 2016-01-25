package chatRoom;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/*
 * Send msg
 */
public class Send implements Runnable{
	private BufferedReader console ;	
	private DataOutputStream dos;
	private Boolean isRunning = true;
	private String name;
	
	public Send() {
		this.console = new BufferedReader(new InputStreamReader(System.in));
	}


	public Send(Socket client, String name) {
		this();
		try {
			this.dos = new DataOutputStream(client.getOutputStream());
			this.name = name;
			send(this.name);
		} catch (IOException e) {
			isRunning = false;
			CloseUtil.closeAll(console, dos);
		}
	}

/*
 * send method
 */
	private String getMsgFromConsole() {
		try {
			return console.readLine();
		} catch (IOException e) {
		}
		return "";
	}
	
	public void send(String str) {
		//System.out.println("Please type in words:");
		String msg = str;
		try {
			if (null!=msg && !msg.equals("")) {
				
				dos.writeUTF(msg);
				dos.flush();
			}
		} catch (IOException e) {
			isRunning = false;
			CloseUtil.closeAll(console, dos);
		}
		
	}

	public void run() {
		while(isRunning) {
			send(getMsgFromConsole());
		}
	}


}
