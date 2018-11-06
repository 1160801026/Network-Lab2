package gbn;

import java.util.LinkedList;
import java.util.Queue;

public class WinMgr {
  static boolean isgbn=false;  //双方都需要更新
  
  static int used_window=0;
//  static int recved_window=0;    GBN接收方没窗口
  static int expected_seq=0;   //接收方使用。接收方换用一个全局变量expected_seq,该变量可删除。但我就不删。
  static int window_size=4;
  static long timeout=1000;
  static int ack_size=2;

  static Queue<Integer> queue=new LinkedList<>();   //发送方使用，看ACK是否对应
  static Queue<byte[]> pool=new LinkedList<>();  //存放待确认的包
}
