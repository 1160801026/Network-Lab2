package sr_FileTrans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WinMgr {
  static boolean issr = false;  //˫���������ã����ͬʱ����
  volatile static int maxseq=4;   //���ͷ�ʹ�ã���Ϊ���ڴ�С�̶�����ֻ��Ҫ��һ�ߵ���ż�¼����,volatileʹ�仯�ɼ�

//  static int used_window = 0;
  static int recved_window = 0;  //���ͷ�ʹ��
  static int window_size = 4;
  static long timeout = 1000;

  static Set<Integer> unacked = new HashSet<>();  //���ͷ�ʹ��
  static List<Frame> buffer = new ArrayList<>();   //���շ�ʹ��
  
  static double packet_loss=0;  
  static double ack_loss=0;   //���շ�ʹ��
  
  static List<String> input=new ArrayList<>();
  static List<Frame> result=new ArrayList<>();  //��Ŷ�ȡ�������ս��
}
