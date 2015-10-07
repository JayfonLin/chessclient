package chess.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CBinUnpacker {
	//ArrayList<CMemoryBlock> blocks;
	byte data[];
	int offset;
	
	public static final int BYTE_SIZE = 1;
	public static final int WORD_SIZE = 2;
	public static final int DWORD_SIZE = 4;
	
	public CBinUnpacker(BufferedInputStream inFromServer){
		
		//TODO 同步问题
		//blocks = new ArrayList<CMemoryBlock>();
		int pack_len = ReadInt(inFromServer);
		
		data = new byte[pack_len];
		offset = 0;
		int result;
		try {
			result = inFromServer.read(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte ReadByte(){
		byte b = data[offset];
		offset += BYTE_SIZE;
		return b;
	}
	
	public short ReadWord(){
		int result;
		short s = 0;
		byte b[] = new byte[WORD_SIZE];
		System.arraycopy(data, offset, b, 0, WORD_SIZE);
		offset += WORD_SIZE;
		return ByteArrayToShort(b);
	}
	
	public int ReadDWord(){
		int result;
		byte b[] = new byte[DWORD_SIZE];
		System.arraycopy(data, offset, b, 0, DWORD_SIZE);
		offset += DWORD_SIZE;
		return ByteArrayToInt(b);
	}
	
	public String ReadString(){
		short len = ReadWord();
		System.out.println("recv word: "+len);
		byte b[] = new byte[len];
		int result;
		System.arraycopy(data, offset, b, 0, len*BYTE_SIZE);
		offset += len*BYTE_SIZE;
		
		String str = null;
		try {
			str = new String(b, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return str;
	}
	
	private int ReadInt(BufferedInputStream inFromServer){
		
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
