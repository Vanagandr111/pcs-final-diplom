import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TestClass {

  @Test
  public void test() throws IOException {

    boolean done = false;

    try (Socket socket = new Socket(InetAddress.getLocalHost(), 8989);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true))
    {
      out.println("бизнес");
      String s = in.readLine();
      if(s != null) System.out.println(s);

    }
  }
}
