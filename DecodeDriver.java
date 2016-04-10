package huffman;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DecodeDriver {
    static final int MAX_ENCODE = 32;
    static final boolean SECURE = false;
    static final String ENCODED_SONG = "demoWavEncode.wav";
    static final String FREQ_TABLE = "demoWavTable.txt";
    static final String JUMPER_INFO = "demoWavJumper.txt";
    static final String DECODED_FILE = "demoWavDecoded.txt";
    
    static public void main(String[] args) {
        try {
            DataInputStream jump = new DataInputStream(new FileInputStream(JUMPER_INFO));
            int jumper = jump.readInt();
            jump.close();
            
            byte b[] = new byte[jumper];
            int table[] = new int[256];
            DataInputStream freq = new DataInputStream(new FileInputStream(FREQ_TABLE));
            while(freq.available() > 0) 
                table[freq.readInt()] = freq.readInt();
            freq.close();
            
            Huffman h = new Huffman(table);
     
             FileInputStream resFile = new FileInputStream(ENCODED_SONG);
             
             String encodedString = "";
             boolean eof = false;
             int currentIndex = 0;
             int totalLength = 0;
             int exp = 1;
             String numString = "";
             
             int j = 0;
             for (int i = 0; i < MAX_ENCODE;) {
                  int in = resFile.read(b);
                  if ((b[jumper - 1] & 1) == 0) {
                      numString += "0";
                      i++;
                  }
                  else {
                      numString += "1";
                      i++;
                  }
                  j++;
             }
             
             for (int i = MAX_ENCODE - 1; i >= 0; i--) {
                  if (numString.charAt(i) == '1')
                      totalLength += exp;
                  exp *= 2;
             }
                          
             while (!eof && currentIndex < totalLength) {
                 int in = resFile.read(b);
                 if (in == -1)
                     eof = true;
                 else{
                     in = b[jumper - 1];
                     if ((in & 1) == 1) {
                         encodedString = encodedString + "1";
                         currentIndex++;
                     }
                     else {
                         encodedString = encodedString + "0";
                         currentIndex++;
                     }
                 }
             }
             DataOutputStream decoded = new DataOutputStream(new FileOutputStream(DECODED_FILE));
             decoded.writeBytes(h.decode(h.root, encodedString));
             decoded.close();
         } catch (IOException e) {
            System.out.println(e);
        }
    }
}
