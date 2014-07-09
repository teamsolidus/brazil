/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Adrian
 */
public class PingRefbox
{

    public void pingRefbox() throws IOException
    {

        Runtime r = Runtime.getRuntime();
        Process p = r.exec("ping 172.26.100.100");


        /*  pb.directory( new File( "C:\\dell" ) );
         pb.start();*/
        BufferedReader in
            = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
        String currentLine = null;
        while ((currentLine = in.readLine()) != null)
        {
            System.out.println(currentLine);
        }
        BufferedReader err
            = new BufferedReader(
                new InputStreamReader(p.getErrorStream()));
        while ((currentLine = err.readLine()) != null)
        {
            System.out.println(currentLine);
        }
        
       // p.destroy();
    }

    public static void main(String[] args) throws IOException
    {
        PingRefbox ping = new PingRefbox();
        ping.pingRefbox();
    }
    
    
}


