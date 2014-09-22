package server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

public class Vfs {
   
   private final String root;

   public Vfs(String root) {
      this.root = root;
   }
   
   public String getRoot() {
      return root;
   }
   
   public boolean isExist(String relativeFilePath) {
      return new File(root + relativeFilePath).exists();
   }
   
   public boolean isDirectory(String relativeFilePath) {
      return new File(root + relativeFilePath).isDirectory();
   }
   
   public String getAbsolutePath(String relativeFilePath) {
      return new File(root + relativeFilePath).getAbsolutePath();
   }
   
   public byte[] getBytes(String relativeFilePath) {
      if (!isExist(relativeFilePath) )
         throw new IllegalArgumentException();
      File file = new File(root + relativeFilePath);
      byte[] bytes = new byte[(int) file.length()];
      FileInputStream fis = null;
      try {
         fis = new FileInputStream(file);
         fis.read(bytes);
      }
      catch(FileNotFoundException e) {
          e.printStackTrace();
      }
      catch(IOException e) {
         e.printStackTrace();
      }
      finally {
         if (fis != null) {
            try {
               fis.close();
            }
            catch (IOException e) {
               e.printStackTrace();
            }
         }
        
      }
      return bytes;
   }
   
   public String getUtf8Text(String relativeFilePath) {
      StringBuffer text = new StringBuffer();
      BufferedReader br = null;
      try {
         FileInputStream fis = new FileInputStream(root + relativeFilePath);
         InputStreamReader isr = new InputStreamReader(fis, "UTF8");
         br = new BufferedReader(isr);
         
         String line;
         while ((line = br.readLine()) != null) {
            text.append(line);
         }
      } 
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      finally {
         if (br != null) {
            try {
               br.close();
            }
            catch (IOException e) {
               e.printStackTrace();
            }
         }
         
      }
      return text.toString();
   }
   
   public Iterator<String> getIterator(String startDir) {
      return null;
   }
   
   public static void main(String[] args) {
      Vfs vfs = new Vfs("D:\\www\\");
      System.out.println(vfs.getAbsolutePath("h.txt") );
      System.out.println(vfs.isExist("h.txt") );
      System.out.println(vfs.isDirectory("h.txt") );
      byte[] b = vfs.getBytes("h.txt");
      for (int i = 0; i < b.length; i++)
         System.out.print( (char) b[i] + " ");
      System.out.println(vfs.getUtf8Text("h.txt") );
   }

}
