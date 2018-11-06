package gbn_withFrame;

public class Frame {
  int seq;
  String content;
  
  public Frame(int seq,String content) {
    this.seq=seq;
    this.content=content;
  }
  
  public Frame(String str) {
    this.seq=Integer.parseInt(str.split(" | ")[0]);
    int start=str.indexOf(" | ");
    
    this.content=str.substring(start+3);
  }
  
  public byte[] getBytes() {
    return (seq+" | "+content).getBytes();
  }
  
  public int getSeq() {
    return seq;
  }
  
  public String getContent() {
    return content;
  }
}
