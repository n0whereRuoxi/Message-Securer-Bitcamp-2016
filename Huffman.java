package huffman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Huffman{    
    private class htree_node {
        private htree_node left;
        private htree_node right;
        private char value;
        private int frequency;
        
        private  htree_node(htree_node left, htree_node right, char value, int frequency) {
            this.left = left;
            this.right = right;
            this.value = value;
            this.frequency = frequency;
        }
    }
    
    public class node_comparator implements Comparator {
        @Override
        public int compare(Object n1, Object n2) {
            if (((htree_node) n1).frequency < ((htree_node) n2).frequency)
                return -1;
            else if (((htree_node) n1).frequency > ((htree_node) n2).frequency)
                return 1;
            return 0;
        }
    }
    
    public htree_node root;
    
    public boolean is_htree0(htree_node H) {
        if (H == null) return false;
        if (H.value == '\0' || H.frequency <= 0) return false;
        if (H.left != null || H.right != null) return false;
        
        return true;
    }
    
    public boolean is_htree2(htree_node H) {
        if (H == null) return false;
        if (H.value != '\0') return false;
        if (H.left == null) return false;
        if (!is_htree2(H.left) && !is_htree0(H.left)) return false;
        if (H.right == null) return false;
        if (!is_htree2(H.right) && !is_htree0(H.right)) return false;
        if (H.frequency != H.left.frequency + H.right.frequency) return false;
        
        return true;
    }
    
    public boolean is_htree(htree_node H) {
        return (H != null && (is_htree2(H) || is_htree0(H)));
    }
    
    public int[] getTable(String file) {
        try {
            return (new freqTable(file)).freqtable;
        } catch (IOException e) {
            System.out.println(e);
        }
        
        return null;
    }
 
    public Huffman(String file) {
        try {
            freqTable table = new freqTable(file);
            
            int total = 0;
            Comparator<htree_node> comparator = new node_comparator();
            PriorityQueue<htree_node> pq = new PriorityQueue<htree_node>(256, comparator);
        
            for (int i = 0; i < 256; i++) {
                if (table.freqtable[i] > 0) {
                    char value = (char) i;
                    htree_node tmp = new htree_node(null, null, value, table.freqtable[i]);
                    total++;
                    pq.add(tmp);
                }
            }
            
            
            
            if (total < 2) {System.out.println("Too less elements");}
            
            root = build_tree(pq);
            
         } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public Huffman(int table[]) {

            int total = 0;
            Comparator<htree_node> comparator = new node_comparator();
            PriorityQueue<htree_node> pq = new PriorityQueue<htree_node>(256, comparator);
        
            for (int i = 0; i < 256; i++) {
                if (table[i] > 0) {
                    char value = (char) i;
                    htree_node tmp = new htree_node(null, null, value, table[i]);
                    total++;
                    pq.add(tmp);
                }
            }
            
            
            
            if (total < 2) {System.out.println("Too less elements");}
            
            root = build_tree(pq);
            
         
    }
        
    public htree_node build_tree(PriorityQueue<htree_node> pq) {
        while (!pq.isEmpty()) {
            htree_node left = pq.remove();
        
            if (pq.isEmpty()) return left;
            
            htree_node right = pq.remove();
            int total_freq = left.frequency + right.frequency;
            //System.out.println(total_freq);
            htree_node parent = new htree_node(left, right, '\0', total_freq);
            
            pq.add(parent);
        }
        
        return null;
    }    
    
    public boolean is_bitstring(String s) {
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            
            if (c != '0' && c != '1') return false;
        }
        
        return true;
    }
    
    public boolean is_codetable(String c[]) {
        for (int i = 0; i < 256; i++) 
            if (!is_bitstring(c[i])) return false;
        
        return true;
     }
    
    public void find_code(String c[], htree_node H, String code) {
        int value = -1;
        if (H.value != '\0')
            value = (int)H.value;
        
        if (0 <= value && value < 256) {
            c[value] = code;
            return;
        }
        
        find_code(c, H.left, code.concat("0"));
        find_code(c, H.right, code.concat("1"));
    }
    
    public String[] htree_to_codetable(htree_node H) {
        String c[] = new String[256];
        String code = "";
        find_code(c, H, code);
        
//        for (int i = 0; i < 128; i++) {
//            if (c[i] != null) System.out.println((char)(i) + " " + c[i]);
//        }
        
        return c;
    }
    
    public String encode(htree_node H, String file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String input = "";
            String encoded = "";
            while ((input = br.readLine()) != null) {
                int length = input.length();

                String table[] = htree_to_codetable(H);

                for (int i = 0; i < length; i++) {
                    char tmp = input.charAt(i);
                    int ascii_value = (int) tmp;
                    if (ascii_value > 255) {
                        System.out.println("text cannot be encoded");
                    }
                    if (table[ascii_value].equals("")) {
                        System.out.println("text cannot be encoded");
                    }

                    encoded = encoded.concat(table[ascii_value]);
                }

            }
            return encoded;
        }
    }
    
    public String encode(htree_node H, String file, String table[]) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String input = "";
            String encoded = "";
            while ((input = br.readLine()) != null) {
                int length = input.length();

                for (int i = 0; i < length; i++) {
                    char tmp = input.charAt(i);
                    int ascii_value = (int) tmp;
                    if (ascii_value > 255) {
                        System.out.println("text cannot be encoded");
                    }
                    if (table[ascii_value].equals("")) {
                        System.out.println("text cannot be encoded");
                    }

                    encoded = encoded.concat(table[ascii_value]);
                }

            }
            return encoded;
        }
    }
    
    public htree_node reset(htree_node H) {
        return (new htree_node(root.left, root.right, root.value, root.frequency));
    }
    
    public String decode(htree_node H, String bits) {
        if (!is_bitstring(bits)) {System.out.println("Can not be decoded");}
        
        String decoded = "";
        int length = bits.length();
        htree_node point = reset(root);
        
        for (int i = 0; i < length; i++) {
            if (point.value == '\0') {
                if (bits.charAt(i) == '0') 
                    point = point.left;
                else
                    point = point.right;
            } else {
                decoded = decoded.concat(point.value + "");
                point = root;
                i = i - 1;
            }
            
        //    System.out.println(point.value + " " + bits.charAt(i));
        }
        
        decoded = decoded.concat(point.value + "");
        point = reset(root);
        if (point.frequency != H.frequency) {System.out.println("Can not be decoded");}
        
        return decoded;
    }
    
    public String decode(int table[], String bits) {
        if (!is_bitstring(bits)) {System.out.println("Can not be decoded");}
        
        String decoded = "";
        int length = bits.length();
        htree_node point = reset(root);
        
        for (int i = 0; i < length; i++) {
            if (point.value == '\0') {
                if (bits.charAt(i) == '0') 
                    point = point.left;
                else
                    point = point.right;
            } else {
                decoded = decoded.concat(point.value + "");
                point = root;
                i = i - 1;
            }
            
        //    System.out.println(point.value + " " + bits.charAt(i));
        }
        
        decoded = decoded.concat(point.value + "");
        point = reset(root);
        if (point.frequency != root.frequency) {System.out.println("Can not be decoded");}
        
        return decoded;
    }
}
