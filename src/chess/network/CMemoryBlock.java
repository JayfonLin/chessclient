package chess.network;

public class CMemoryBlock {
	
	public static final int MEMORY_BLOCK_SIZE = 512;

	public byte buf[];
	
	public int off;
	
	public CMemoryBlock(){
		buf = new byte[MEMORY_BLOCK_SIZE];
		off = 0;
	}

}
