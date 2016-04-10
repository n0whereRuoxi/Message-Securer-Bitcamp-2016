package huffman;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EncodeDriver {
    static final int MAX_ENCODE = 32;   //Max number of characters that allows to encode, in 2^32
    static final int JUMPER = 100;  
    static final boolean SECURE = false;    //True for extra layer of security
    static final String SONG = "demoWav.wav";
    static final String ENCODED_SONG = "demoWavEncode.wav";
    static final String MESSAGE_FILE = "demoWavMessage.txt"; //Message wish to encode
    static final String FREQ_FILE = "demoWavFreq.txt";  //frequency file for extra layer of security
    static final String FREQ_TABLE = "demoWavTable.txt";
    static final String JUMPER_INFO = "demoWavJumper.txt";
    
    static public void main(String[] args) {
        
         //Read file, encoding, produce the song
         Huffman h = null;
         int table[] = null; 
         
         if (SECURE) {
             h = new Huffman(FREQ_FILE);
             table = h.getTable(FREQ_FILE);
         } else {
             h = new Huffman(MESSAGE_FILE);
             table = h.getTable(MESSAGE_FILE);
         }
            
         byte b[] = new byte[JUMPER];
         int j = 0;
         
         try {
             if (SECURE) {
                 table = h.getTable(MESSAGE_FILE);
                 FileWriter r = new FileWriter(FREQ_FILE, true);
                 for (int i = 0; i < 256; i++) {
                     for (int k = 0; k < table[i]; k++) {
                         r.write((char)i);
                     }
                 }
                 
                 r.close();
             }
             String res = h.encode(h.root, MESSAGE_FILE);
             
             int encodedLength = res.length();
             int index = 0;
             String len = "";
             while (encodedLength != 0) {
                 len = (encodedLength % 2) + len;
                 encodedLength /= 2;
             }
             
             while (len.length() < MAX_ENCODE)
                 len = "0" + len;
             res = len + res;
                          
             File song = new File(SONG);
             FileInputStream file = new FileInputStream(song);
             File encodedSong = new File(ENCODED_SONG);
             FileOutputStream encodedFile = new FileOutputStream(encodedSong);
                          
             int l = res.length();
             boolean eof = false;
             while (!eof) {
                 int in = file.read(b);
                 if (in == -1)
                     eof = true;
                 else {
                     for (int i = 0; i < JUMPER - 1; i++) {
                             encodedFile.write(b[i]);
                     }
                     
                     int newIn = b[JUMPER-1];
                     
                     if ( index < l && res.charAt(index) == '0') {
                         newIn = b[JUMPER-1] & 0xFFFFFFFE;
                         index += 1;
                         j = 0;
                     }
                     else if (index < l) {
                         newIn = b[JUMPER-1] | 1;
                         index += 1;
                         j = 0;
                     }
                     encodedFile.write(newIn);
                     j++;      
                 }
             }
             file.close();
             encodedFile.close();
             
             DataOutputStream freq = new DataOutputStream(new FileOutputStream(FREQ_TABLE));
             for (int i = 0; i < table.length; i++) {
                 freq.writeInt(i );
                 freq.writeInt(table[i]);
             }
             freq.close();
             
             DataOutputStream jump = new DataOutputStream(new FileOutputStream(JUMPER_INFO));
             jump.writeInt(JUMPER);
             jump.close();
             
         } catch (IOException e) {
            System.out.println(e);
        }
    }
}
