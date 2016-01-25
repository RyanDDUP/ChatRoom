package chatRoom;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {
	public static void closeAll(Closeable...io) {
		for(Closeable i:io) {
				try {
					if (null != i) {
					i.close();
				
					}
				}catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}
