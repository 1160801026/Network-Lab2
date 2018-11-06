package gbn_withFrame;

import java.util.LinkedList;
import java.util.Queue;

public class WinMgr {
  static boolean isgbn=false;
  
  static int used_window=0;
//  static int recved_window=0;    GBN接收方没窗口
  static int expected_seq=0;   //接收方使用
  static int window_size=4;
  static long timeout=1000;
  static int ack_size=2;

  static Queue<Integer> queue=new LinkedList<>();   //发送方使用，看ACK是否对应
  static Queue<Frame> pool=new LinkedList<>();  //存放待确认的包
}
