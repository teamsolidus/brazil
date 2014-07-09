/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           29.05.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package ComView;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;

public class UDPServer
{
    //byte[] receiveData;

  /**
   * out[0] = id --> Nachricht 1 oder 0 out[1] = length --> länge der Nachricht
   * 36 out[2] = check --> Prüfnummer out[3] = in0 --> Nachricht 0 out[4] = in1
   * --> Nachricht 1 out[5] = in2 --> Nachricht 2 out[6] = in3 --> Nachricht 3
   * out[7] = in4 --> Nachricht 4 out[8] = in5 --> Nachricht 5 out[9] = in6 -->
   * Nachricht 6 out[9] = in6 --> Nachricht 7 out[10] = in6 --> Nachricht 8
   *
   * @param port
   * @return
   * @throws Exception
   */
  public int[] getViewMessagr(DatagramSocket serverSocket) throws Exception
  {
    byte[] receiveData = new byte[36]; //
    byte[] in = new byte[36];

// bytes[] der seperaten Komponenten der zu lesenden Nachricht.
    byte[] id = new byte[1];
    byte[] length = new byte[2];
    byte[] check = new byte[1];

    byte[] in0 = new byte[4];
    byte[] in1 = new byte[4];
    byte[] in2 = new byte[4];
    byte[] in3 = new byte[4];
    byte[] in4 = new byte[4];
    byte[] in5 = new byte[4];
    byte[] in6 = new byte[4];
    byte[] in7 = new byte[4];

    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    serverSocket.receive(receivePacket);

    /* for (int i=0; i < 36; i++ )
     {
     System.out.println("INPUT " + i + ": "+Array.getByte(receiveData, i));
     }*/
    //String sentence = new String( receivePacket.getData());
    in = receivePacket.getData();

    id[0] = in[0];

    length[1] = in[1];
    length[0] = in[2];

    check[0] = in[3];

                  // Nachricht 0
    in0[3] = in[4];
    in0[2] = in[5];
    in0[1] = in[6];
    in0[0] = in[7];

                  // Nachricht 1
    in1[3] = in[8];
    in1[2] = in[9];
    in1[1] = in[10];
    in1[0] = in[11];

                  // Nachricht 2
    in2[3] = in[12];
    in2[2] = in[13];
    in2[1] = in[14];
    in2[0] = in[15];

                  // Nachricht 3
    in3[3] = in[16];
    in3[2] = in[17];
    in3[1] = in[18];
    in3[0] = in[19];

                  // Nachricht 4
    in4[3] = in[20];
    in4[2] = in[21];
    in4[1] = in[22];
    in4[0] = in[23];

                  // Nachricht 5
    in5[3] = in[24];
    in5[2] = in[25];
    in5[1] = in[26];
    in5[0] = in[27];

                  // Nachricht 6
    in6[3] = in[28];
    in6[2] = in[29];
    in6[1] = in[30];
    in6[0] = in[31];

                  // Nachricht 7
    in7[3] = in[32];
    in7[2] = in[33];
    in7[1] = in[34];
    in7[0] = in[35];

    int[] out = new int[8];

    out[0] = byteToInt(in0);
    out[1] = byteToInt(in1);
    out[2] = byteToInt(in2);
    out[3] = byteToInt(in3);
    out[4] = byteToInt(in4);
    out[5] = byteToInt(in5);
    out[6] = byteToInt(in6);
    out[7] = byteToInt(in7);

    /* 
     System.out.println("Nachricht ist bereit zum senden");
                 
     System.out.println("ID: " + byteToInt(id));
     System.out.println("Length: " + byteToInt(length));
     System.out.println("Check Number: " + byteToInt(check));
     System.out.println("In 0: " + out[0]);
     System.out.println("In 1: " + out[1]);
     System.out.println("In 2: " + out[2]);
     System.out.println("In 3: " + out[3]);
     System.out.println("In 4: " + out[4]);
     System.out.println("In 5: " + out[5]);
     System.out.println("In 6: " + out[6]);
     System.out.println("In 7: " + out[7]);
     */
    return out;

  }

  /**
   *
   * Sendet das mitgegebene byte[] über den Port port an die Ip ip
   *
   * @param sendData
   * @throws SocketException
   * @throws UnknownHostException
   * @throws IOException
   */
  public void sendViewMessage(int[] out, String ip, int port) throws SocketException, UnknownHostException, IOException, InterruptedException
  {
    /*
     System.out.println("SENDEN Nachricht 0: " + out[0]);
     System.out.println("SENDEN Nachricht 1: " + out[1]);
     System.out.println("SENDEN Nachricht 2: " + out[2]);
     System.out.println("SENDEN Nachricht 3: " + out[3]);
     System.out.println("SENDEN Nachricht 4: " + out[4]);
     System.out.println("SENDEN Nachricht 5: " + out[5]);
     System.out.println("SENDEN Nachricht 6: " + out[6]);
     System.out.println("SENDEN Nachricht 7: " + out[7]);
     */

    byte[] sendData = new byte[36];

    byte[] id = new byte[1];
    byte[] length = new byte[2];
    byte[] check = new byte[1];

    byte[] out0 = new byte[4];
    byte[] out1 = new byte[4];
    byte[] out2 = new byte[4];
    byte[] out3 = new byte[4];
    byte[] out4 = new byte[4];
    byte[] out5 = new byte[4];
    byte[] out6 = new byte[4];
    byte[] out7 = new byte[4];

    length = intToByte(2, 36);

    out0 = intToByte(4, out[0]);
    out1 = intToByte(4, out[1]);
    out2 = intToByte(4, out[2]);
    out3 = intToByte(4, out[3]);
    out4 = intToByte(4, out[4]);
    out5 = intToByte(4, out[5]);
    out6 = intToByte(4, out[6]);
    out7 = intToByte(4, out[7]);

        //Nachricht id
    sendData[0] = 0;

        //Nachricht länge ( ist immer 36 Byte)
    sendData[1] = length[1];
    sendData[2] = 0; // length[0] ergibt 9, sollte aber 0 ergeben???

        // senData[3] steht weiter unten und kann erst nach erstellen der restlichen
    // Nachricht erstellt werden.
        //Nachricht 0
    sendData[4] = out0[3];
    sendData[5] = out0[2];
    sendData[6] = out0[1];
    sendData[7] = out0[0];

        //Nachricht 1
    sendData[8] = out1[3];
    sendData[9] = out1[2];
    sendData[10] = out1[1];
    sendData[11] = out1[0];

        //Nachricht 2
    sendData[12] = out2[3];
    sendData[13] = out2[2];
    sendData[14] = out2[1];
    sendData[15] = out2[0];

        //Nachricht 3
    sendData[16] = out3[3];
    sendData[17] = out3[2];
    sendData[18] = out3[1];
    sendData[19] = out3[0];

        //Nachricht 4
    sendData[20] = out4[3];
    sendData[21] = out4[2];
    sendData[22] = out4[1];
    sendData[23] = out4[0];

        //Nachricht 5
    sendData[24] = out5[3];
    sendData[25] = out5[2];
    sendData[26] = out5[1];
    sendData[27] = out5[0];

        //Nachricht 6
    sendData[28] = out6[3];
    sendData[29] = out6[2];
    sendData[30] = out6[1];
    sendData[31] = out6[0];

        //Nachricht 7
    sendData[32] = out7[3];
    sendData[33] = out7[2];
    sendData[34] = out7[1];
    sendData[35] = out7[0];

    byte[] nutzdaten = new byte[32];

        // Stellt die, für dei Prüfsumme relevanten Bytes zusammen
    for (int i = 0; i < 32; i++)
    {
      if (i != 2)
      {
        // System.out.println(i);
        nutzdaten[i] = sendData[i];
      }
    }

        // Berechnet die Prüfsumme
    byte[] temp = intToByte(1, checksum(nutzdaten, nutzdaten.length));

        // Nachricht Prüfsumme
    sendData[3] = temp[0];

       // Konsolenausgabe
       /*
     for (int i=0; i < 36; i++ )
     {
       
     if ( i == 4 || i == 8 || i == 12 || i == 16 || i == 20 || i == 24 || i == 28 || i == 32)
     {
     System.out.println("-- Nachricht " + ((i/4) - 1) + " --");
     }
       
     System.out.println("OUTPUT " + i + ": "+ Array.getByte(sendData, i));
     }
     */
   // ------------------------------- SENDEN -----------------------------------
    DatagramSocket send = new DatagramSocket();
    InetAddress ipadress = InetAddress.getByName(ip);
    send.connect(ipadress, port);

    send.send(new DatagramPacket(sendData, sendData.length));

    send.disconnect();
    Thread.sleep(100);

  }

// ----------------------- Byte[] zu Integer -----------------------------------
  /**
   * Wandelt ein byte[] in einen Interger um, es muss ds zu umwandelnde byte[]
   * mitgegeben werden
   *
   * @param in
   * @return
   */
  public static int byteToInt(byte[] in)
  {
// byte[] -> int
    int lenght = in.length;
    int number = 0;
    for (int i = 0; i < lenght; ++i)
    {
      number |= (in[lenght - 1 - i] & 0xff) << (i << lenght - 1);
    }

    return number;
  }

// ----------------------- Integer zu Byte[] -----------------------------------
  /**
   * Wandelt einen Integer in einen byte[] um, es muss der zu umwandelnde
   * Interger und die gewüscht bytelänge mitgegeben werden.
   *
   * @param length
   * @param in
   * @return
   */
  public static byte[] intToByte(int length, int in)
  {
    byte[] data = new byte[length];

    // int -> byte[]
    for (int i = 0; i < length; ++i)
    {
      int shift = i << length - 1; // i * 8
      data[length - 1 - i] = (byte) ((in & (0xff << shift)) >>> shift);
    }
    return data;
  }

  /**
   * Berechnet die Prüfsumme für das zu sendende byte[] (byte[3])
   *
   * @param nutzdaten
   * @param nutzdatenLänge
   * @return
   */
  public static int checksum(byte[] nutzdaten, int nutzdatenLänge)
  {

    int s0 = 0;

    for (int i = 0; i < nutzdatenLänge; ++i)
    {
      s0 = (s0 + nutzdaten[i]) % 256;
    }
    return (0xFF - s0);

  }

}
