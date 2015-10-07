package chess.network;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import chess.server.CServerManager;
import chess.server.CHandler;


public class CTCPClient
{
	
	Socket clientSocket;
	BufferedOutputStream outToServer;
	BufferedInputStream inFromServer;
	CTCPReaderThread readThread;
	
	static String server_address = "192.168.1.115";
	static int server_port = 9999;
	private static CTCPClient client;
	
	private CTCPClient(final String address, final int port){

		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					clientSocket = new Socket(address, port);
					outToServer = new BufferedOutputStream(clientSocket.getOutputStream());
					inFromServer = new BufferedInputStream(clientSocket.getInputStream());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				readThread = new CTCPReaderThread(inFromServer);
				readThread.start();
			}
		});

		t.start();
	}
	
	public static CTCPClient GetInstance(){
		if (client == null){
			synchronized (CTCPClient.class) {
				if (client == null){
					client = new CTCPClient(server_address, server_port);
				}
			}
		}
		
		return client;
	}
	
	
	public void Send(CBinPacker packer){
		
		if (outToServer == null){
			System.out.println("CTCPClient.Send outToServer is null!");
			return;
		}
		
		final byte data[] = packer.GetPackBuffer();
		
		synchronized(outToServer){
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						outToServer.write(data);
						outToServer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			t.start();
		}
		
	}
	
	public void close(){
		try {
			outToServer.close();
			outToServer = null;
			
			inFromServer.close();
			inFromServer = null;
			
			clientSocket.close();
			clientSocket = null;
		} catch (UnknownHostException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}


class CTCPReaderThread extends Thread{
	BufferedInputStream inFromServer;
	
	public CTCPReaderThread(BufferedInputStream reader){
		inFromServer = reader;
	}
	
	public void run(){
		
		while (true){
			CBinUnpacker unpacker = new CBinUnpacker(inFromServer);
			chess.server.CHandler.OnPackage(unpacker);
		}
	
	}
	
	byte read_byte(){
		byte b = 0;
		try {
			b = (byte) inFromServer.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	
	short read_word(){
		int result;
		short s = 0;
		byte b[] = new byte[2];
		try {
			result = (short) inFromServer.read(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ByteArrayToShort(b);
	}
	
	int read_dword(){
		int result;
		byte b[] = new byte[4];
		try {
			result = inFromServer.read(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ByteArrayToInt(b);
	}
	
	String read_string(){
		short len = read_word();
		System.out.println("recv word: "+len);
		byte b[] = new byte[len];
		int result;
		try {
			result = inFromServer.read(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = null;
		//str = new String(b);
		
		
		try {
			str = new String(b, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return str;
	}
	
	public short ByteArrayToShort(byte[] b){
		return (short) ((b[1] & 0xff) |
			   (b[0] & 0xff) << 8);
	}
	
	public int ByteArrayToInt(byte[] b){
		return (b[3] & 0xff) |
			   (b[2] & 0xff) << 8 |
			   (b[1] & 0xff) << 16 |
			   (b[0] & 0xff) << 24;
	}
}
