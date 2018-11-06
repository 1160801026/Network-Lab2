package sr_FileTrans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WinMgr {
  static boolean issr = false;  //双方均可以用，最好同时更新
  volatile static int maxseq=4;   //发送方使用，因为窗口大小固定所以只需要用一边的序号记录即可,volatile使变化可见

//  static int used_window = 0;
  static int recved_window = 0;  //发送方使用
  static int window_size = 4;
  static long timeout = 1000;

  static Set<Integer> unacked = new HashSet<>();  //发送方使用
  static List<Frame> buffer = new ArrayList<>();   //接收方使用
  
  static double packet_loss=0;  
  static double ack_loss=0;   //接收方使用
  
  static List<String> input=new ArrayList<>();
  static List<Frame> result=new ArrayList<>();  //存放读取到的最终结果
}
