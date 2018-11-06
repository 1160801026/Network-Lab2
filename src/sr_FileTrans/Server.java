package sr_FileTrans;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;

public class Server {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    
    try {
      FileInputStream input=new FileInputStream("C:\\Users\\samma\\Desktop\\1.txt");
      BufferedReader reader=new BufferedReader(new InputStreamReader(input));
      String str=null;
      while((str=reader.readLine())!=null) {
        WinMgr.input.add(str);
      }//文件读完了
      reader.close();
      
      DatagramSocket socket = new DatagramSocket(8081);
      Server_recver server_recver=new Server_recver(socket);
      Thread thread=new Thread(server_recver);
      thread.start();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
