package gbn;

import java.util.LinkedList;
import java.util.Queue;

public class WinMgr {
  static boolean isgbn=false;  //˫������Ҫ����
  
  static int used_window=0;
//  static int recved_window=0;    GBN���շ�û����
  static int expected_seq=0;   //���շ�ʹ�á����շ�����һ��ȫ�ֱ���expected_seq,�ñ�����ɾ�������ҾͲ�ɾ��
  static int window_size=4;
  static long timeout=1000;
  static int ack_size=2;

  static Queue<Integer> queue=new LinkedList<>();   //���ͷ�ʹ�ã���ACK�Ƿ��Ӧ
  static Queue<byte[]> pool=new LinkedList<>();  //��Ŵ�ȷ�ϵİ�
}
