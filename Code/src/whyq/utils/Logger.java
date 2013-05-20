package whyq.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

public static void appendLog(String text,String filename)
    {       
       File dir = new File("sdcard/logger");
       if(!dir.exists())
       {
           dir.mkdirs();
       }

       File logFile = new File("sdcard/logger/"+filename+".txt");

       if (!logFile.exists())
       {
          try
          {
             logFile.createNewFile();

          } 
          catch (IOException e)
          {
             // TODO Auto-generated catch block

             e.printStackTrace();

          }
       }
       try
       {
          //BufferedWriter for performance, true to set append to file flag

          BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 

          buf.append(text);

          buf.newLine();

          buf.close();

       }
       catch (IOException e)
       {
          // TODO Auto-generated catch block

           e.printStackTrace();

       }
    }
}
