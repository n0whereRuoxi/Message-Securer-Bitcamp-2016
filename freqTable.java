package huffman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class freqTable {

    public int freqtable[] = new int[256];

    public  freqTable(String file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                int length = line.length();
                for (int i = 0; i < length; i++)
                    freqtable[(int)line.charAt(i)]++;
                freqtable[0x0A]++;
            }
        }
    }

    public boolean all_positive(int table[]) {
        for (int i = 0; i < 256; i++) {
            if (!(table[i] >= 0)) {
                return false;
            }
        }
        return true;
    }

    public boolean is_freqtable(int table[]) {
        return table[0] == 0 && all_positive(table);
    }
}
