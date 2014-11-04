package server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

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
   
   public String getCanonicalPath(String relativeFilePath) {
      try {
         return new File(root + relativeFilePath).getCanonicalPath();
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      return null;
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
   
   public Iterator<String> dfsWithStartDir(String startDir) {
      return new DfsFileIterator(startDir);
   }
   
   private final class DfsFileIterator implements Iterator<String> {
      
      private List<File> filesList = new ArrayList<>();
      
      private int nextIndex;
      
      private DfsFileIterator(String startDir) {
         File start = new File(root + startDir);
         dfs(start);
      }
      
      private void dfs(File current) {
         if (current.listFiles() == null) {
            filesList.add(current);
            return;
         }
         for (File child : current.listFiles() ) {
            dfs(child);
         }
         // add current directory
         filesList.add(current);
      }

      @Override
      public boolean hasNext() {
         return nextIndex < filesList.size() ;
      }

      @Override
      public String next() {
         if (!hasNext() )
            throw new NoSuchElementException();
         return filesList.get(nextIndex++).toString();
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }
      
   }
   
   public Iterator<String> bfsWoStartDir(String startDir) {
      return new BfsFileIterator(startDir);
   }
   
   private final class BfsFileIterator implements Iterator<String> {
      
      private Queue<File> filesQueue = new LinkedList<>();
      
      private BfsFileIterator(String startDir) {
         File start = new File(root + startDir);
         for (File child : start.listFiles()) {
            filesQueue.add(child);
         }
      }

      @Override
      public boolean hasNext() {
         return !filesQueue.isEmpty();
      }

      @Override
      public String next() {
         File next = filesQueue.poll();
         if (next.isDirectory() ) {
            for (File child : next.listFiles()) {
               filesQueue.add(child);
            }
         }
         return next.toString();
      }
      
      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }
      
   }
   
   // @Test
   public static void main(String[] args) {
      Vfs vfs = new Vfs("D:\\www\\");
      System.out.println(vfs.getAbsolutePath("h.txt") );
      System.out.println(vfs.isExist("h.txt") );
      System.out.println(vfs.isDirectory("h.txt") );
      if (vfs.isExist("h.txt") && !vfs.isDirectory("h.txt")) {
         byte[] b = vfs.getBytes("h.txt");
         for (int i = 0; i < b.length; i++)
            System.out.print((char) b[i] + " ");
         System.out.println(vfs.getUtf8Text("h.txt"));
      }
      
      vfs = new Vfs("data");
      Iterator<String> dfsIterator = vfs.dfsWithStartDir("");
      System.out.println("DFS: ");
      while (dfsIterator.hasNext() ) {
         System.out.println(dfsIterator.next() );
      }
      
      Iterator<String> bfsIterator = vfs.bfsWoStartDir("");
      System.out.println("BFS: ");
      while (bfsIterator.hasNext() ) {
         System.out.println(bfsIterator.next() );
      }
      
      vfs = new Vfs("");
      System.out.println(vfs.getAbsolutePath("../data"));
      System.out.println(vfs.getCanonicalPath("../data"));

   }

}
