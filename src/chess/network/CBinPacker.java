package chess.network;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class CBinPacker {
	
	ArrayList<CMemoryBlock> blocks;
	
	public CBinPacker(){
		blocks = new ArrayList<CMemoryBlock>();
	}
	
	public void WriteBuffer(byte[] bs){
		
		int block_len = blocks.size();
		CMemoryBlock block = null;
		if (block_len != 0)
			block = blocks.get(block_len-1);
		
		if (block == null || block.off >= CMemoryBlock.MEMORY_BLOCK_SIZE){
			CMemoryBlock new_block = new CMemoryBlock();
			blocks.add(new_block);
			block = new_block;
		}
		
		int length = bs.length;
		int offset = 0;
		int remain_space = length - offset;
		
		while (offset < length){
			remain_space = (remain_space > CMemoryBlock.MEMORY_BLOCK_SIZE-block.off) ? 
					CMemoryBlock.MEMORY_BLOCK_SIZE-block.off : remain_space;
			
			System.arraycopy(bs, offset, block.buf, block.off, remain_space);
			block.off += remain_space;
			offset += remain_space;
		}
	}
	
	public void PackByte(byte b){
		byte bs[] = new byte[1];
		bs[0] = b;
		WriteBuffer(bs);
	}
	
	public void PackWord(short s){
		byte bs[] = ShortToNetworkByteArray(s);
		WriteBuffer(bs);
	}
	
	public void PackDWord(int n){
		byte bs[] = IntToNetworkByteArray(n);
		WriteBuffer(bs);
	}
	
	public void PackString(String str){
		
		byte data[] = null;
		
		try {
			data = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		short len = (short)data.length;
		PackWord(len);
		WriteBuffer(data);
	}
	
	public byte[] GetPackBuffer(){
		int len = 0;
		for (CMemoryBlock block : blocks){
			len += block.off;
		}
		
		byte data[] = new byte[len+CBinUnpacker.DWORD_SIZE];
		byte bs[] = IntToNetworkByteArray(len);
		System.arraycopy(bs, 0, data, 0, CBinUnpacker.DWORD_SIZE);
		int offset = CBinUnpacker.DWORD_SIZE;

		for (CMemoryBlock block : blocks){
			System.arraycopy(block.buf, 0, data, offset, block.off);
			offset += block.off;
		}
		
		assert(offset == len);
		
		return data;
	}
	
	public static byte[] ShortToNetworkByteArray(short n){
		byte[] b = new byte[2];
		b[1] = (byte) (n & 0xff);
		b[0] = (byte) (n >> 8 & 0xff);
		return b;
	}
	
	public static byte[] IntToNetworkByteArray(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}

}
