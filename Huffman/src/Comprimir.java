
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.PriorityQueue;

//Si la frecuencia de un byte es mayor que 2 ^ 32, habrá un problema.
public class Comprimir {

    static PriorityQueue<TREE> pq = new PriorityQueue<TREE>();
    static int[] freq = new int[300];
    static String[] ss = new String[300];
    static int exbits;
    static byte bt;
    static int cnt; // número de caracteres diferentes

    // Para mantener las funciones de todos los bytes.
    // clase de arbol principal
    static class TREE implements Comparable<TREE> {

        TREE Lchild;
        TREE Rchild;
        public String deb;
        public int Bite;
        public int Freqnc;

        public int compareTo(TREE T) {
            if (this.Freqnc < T.Freqnc) {
                return -1;
            }
            if (this.Freqnc > T.Freqnc) {
                return 1;
            }
            return 0;
        }
    }

    static TREE Root;

    /**
     * *****************************************************************************
     * cálculo de la frecuencia del archivo fname
	 *****************************************************************************
     */
    public static void CalFreq(String fname) {
        File file = null;
        Byte bt;

        file = new File(fname);
        try {
            FileInputStream file_input = new FileInputStream(file);
            DataInputStream data_in = new DataInputStream(file_input);
            while (true) {
                try {

                    bt = data_in.readByte();
                    freq[to(bt)]++;
                } catch (EOFException eof) {
                    System.out.println("Fin de Archivo");
                    break;
                }
            }
            file_input.close();
            data_in.close();
        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        file = null;
    }

    
//    byte a conversión binaria
	
    public static int to(Byte b) {
        int ret = b;
        if (ret < 0) {
            ret = ~b;
            ret = ret + 1;
            ret = ret ^ 255;
            ret += 1;
        }
        return ret;
    }

   
//     liberando la memoria
	
    public static void initHzipping() {
        int i;
        cnt = 0;
        if (Root != null) {
            fredfs(Root);
        }
        for (i = 0; i < 300; i++) {
            freq[i] = 0;
        }
        for (i = 0; i < 300; i++) {
            ss[i] = "";
        }
        pq.clear();
    }

  
//    dfs para liberar memoria
	 
    public static void fredfs(TREE now) {

        if (now.Lchild == null && now.Rchild == null) {
            now = null;
            return;
        }
        if (now.Lchild != null) {
            fredfs(now.Lchild);
        }
        if (now.Rchild != null) {
            fredfs(now.Rchild);
        }
    }

    /**
     dfs para hacer los codigos
	 ********************************************************************************
     */
    public static void dfs(TREE now, String st) {
        now.deb = st;
        if ((now.Lchild == null) && (now.Rchild == null)) {
            ss[now.Bite] = st;
            return;
        }
        if (now.Lchild != null) {
            dfs(now.Lchild, st + "0");
        }
        if (now.Rchild != null) {
            dfs(now.Rchild, st + "1");
        }
    }

    /**
     * *******************************************************************************
     */
    /**
     * *****************************************************************************
     * Haciendo todos los nodos en una prioridad Q haciendo el arbol
	 ******************************************************************************
     */
    public static void MakeNode() {
        int i;
        pq.clear();

        for (i = 0; i < 300; i++) {
            if (freq[i] != 0) {
                TREE Temp = new TREE();
                Temp.Bite = i;
                Temp.Freqnc = freq[i];
                Temp.Lchild = null;
                Temp.Rchild = null;
                pq.add(Temp);
                cnt++;
            }

        }
        TREE Temp1, Temp2;

        if (cnt == 0) {
            return;
        } else if (cnt == 1) {
            for (i = 0; i < 300; i++) {
                if (freq[i] != 0) {
                    ss[i] = "0";
                    break;
                }
            }
            return;
        }

        // ¿Hay algún problema si el archivo está vacío?
        // se encuentra un error si solo hay un caracter
        while (pq.size() != 1) {
            TREE Temp = new TREE();
            Temp1 = pq.poll();
            Temp2 = pq.poll();
            Temp.Lchild = Temp1;
            Temp.Rchild = Temp2;
            Temp.Freqnc = Temp1.Freqnc + Temp2.Freqnc;
            pq.add(Temp);
        }
        Root = pq.poll();
    }

    /**
     * ****************************************************************************
     */
    /**
     * *****************************************************************************
     * cifrado huffman
	 ******************************************************************************
     */
    public static void encrypt(String fname) {
        File file = null;

        file = new File(fname);
        try {
            FileInputStream file_input = new FileInputStream(file);
            DataInputStream data_in = new DataInputStream(file_input);
            while (true) {
                try {

                    bt = data_in.readByte();
                    freq[bt]++;
                } catch (EOFException eof) {
                    System.out.println("Fin de Archivo");
                    break;
                }
            }
            file_input.close();
            data_in.close();

        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        file = null;
    }

    /**
     * ****************************************************************************
     */
    /**
     * *****************************************************************************
     * Fake zip crea un archivo "fakezip. txt" donde pone los códigos binarios
     * finales del archivo comprimido real
	 ******************************************************************************
     */
    public static void fakezip(String fname) {

        File filei, fileo;
        int i;

        filei = new File(fname);
        fileo = new File("fakezipped.txt");
        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);
            PrintStream ps = new PrintStream(fileo);

            while (true) {
                try {
                    bt = data_in.readByte();
                    ps.print(ss[to(bt)]);
                } catch (EOFException eof) {
                    System.out.println("Fin de Archivo");
                    break;
                }
            }

            file_input.close();
            data_in.close();
            ps.close();

        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        filei = null;
        fileo = null;

    }

    /**
     real zip según los códigos de fakezip. txt (fname)
	
     */
    public static void realzip(String fname, String fname1) {
        File filei, fileo;
        int i, j = 10;
        Byte btt;

        filei = new File(fname);
        fileo = new File(fname1);

        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);
            FileOutputStream file_output = new FileOutputStream(fileo);
            DataOutputStream data_out = new DataOutputStream(file_output);

            data_out.writeInt(cnt);
            for (i = 0; i < 256; i++) {
                if (freq[i] != 0) {
                    btt = (byte) i;
                    data_out.write(btt);
                    data_out.writeInt(freq[i]);
                }
            }
            long texbits;
            texbits = filei.length() % 8;
            texbits = (8 - texbits) % 8;
            exbits = (int) texbits;
            data_out.writeInt(exbits);
            while (true) {
                try {
                    bt = 0;
                    byte ch;
                    for (exbits = 0; exbits < 8; exbits++) {
                        ch = data_in.readByte();
                        bt *= 2;
                        if (ch == '1') {
                            bt++;
                        }
                    }
                    data_out.write(bt);

                } catch (EOFException eof) {
                    int x;
                    if (exbits != 0) {
                        for (x = exbits; x < 8; x++) {
                            bt *= 2;
                        }
                        data_out.write(bt);
                    }

                    exbits = (int) texbits;
                    System.out.println("extrabits: " + exbits);
                    System.out.println("Fin de archivo");
                    break;
                }
            }
            data_in.close();
            data_out.close();
            file_input.close();
            file_output.close();
            System.out.println("output file's size: " + fileo.length());

        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }
        filei.delete();
        filei = null;
        fileo = null;
    }

    /**
     * ****************************************************************************
     */

    /*
	 * public static void main (String[] args) { initHzipping();
	 * CalFreq("in.txt"); // calculate the frequency of each digit MakeNode();
	 * // makeing corresponding nodes if(cnt>1) dfs(Root,""); // dfs to make the
	 * codes fakezip("in.txt"); // fake zip file which will have the binary of
	 * the input to fakezipped.txt file
	 * realzip("fakezipped.txt","in.txt"+".huffz"); // making the real zip
	 * according the fakezip.txt file initHzipping();
	 * 
	 * }
     */
    public static void beginHzipping(String arg1) {
        initHzipping();
        CalFreq(arg1); // calcular la frecuencia de cada dígito
        MakeNode(); // hacer los nodos correspondientes
        if (cnt > 1) {
            dfs(Root, ""); // DFS para que los códigos
        }
        fakezip(arg1); // archivo zip falso que tendrá el binario de la entrada al archivo fakezip. txt
        realzip("fakezipped.txt", arg1 + ".michael"); // haciendo real el archivo zip
// segun el fakezip.txt
// expediente
        initHzipping();
    }
}
