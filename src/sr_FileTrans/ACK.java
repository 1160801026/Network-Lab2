package sr_FileTrans;

public class ACK {
  int seq;
  public ACK(int seq) {
    this.seq=seq;
  }
  
  public ACK(String str) {
    this.seq=Integer.parseInt(str.split(": ")[1]);
  }
  
  public byte[] getBytes() {
    return ("ACK: "+seq).getBytes();
  }
  
  public int getSeq() {
    return seq;
  }
}
