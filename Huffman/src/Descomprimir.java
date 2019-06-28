

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.PriorityQueue;

public class Descomprimir {

    static PriorityQueue<TREE> pq1 = new PriorityQueue<TREE>();
    static int[] freq1 = new int[300];
    static String[] ss1 = new String[300]; // INT del codigo
    static String[] btost = new String[300]; // INT TO BIN
    static String bigone; // THE BIG STRING
    static String temp; // TEMPORAL STRING
    static int exbits1; // BITS EXTRA AGREGADOS AL FINAL DE HACER EL ZIP FINAL
    // CÓDIGO MÚLTIPLE DE 8
    static int putit; //
    static int cntu; //NÚMERO DE frecuencias disponibles

    static class TREE implements Comparable<TREE> {

        TREE Lchild;
        TREE Rchild;
        public String deb;
        public int Bite;
        public int freq1nc;

        public int compareTo(TREE T) {
            if (this.freq1nc < T.freq1nc) {
                return -1;
            }
            if (this.freq1nc > T.freq1nc) {
                return 1;
            }
            return 0;
        }
    }

    static TREE Root;

//     liberar la memoria
	
    public static void initHunzipping() {
        int i;
        if (Root != null) {
            fredfs1(Root);
        }
        for (i = 0; i < 300; i++) {
            freq1[i] = 0;
        }
        for (i = 0; i < 300; i++) {
            ss1[i] = "";
        }
        pq1.clear();
        bigone = ""; // THE BIG STRING
        temp = ""; // TEMPORAL STRING
        exbits1 = 0; // BITS EXTRA AGREGADOS AL FINAL DE HACER EL CÓDIGO POSTAL FINAL
        // MULTIPLE DE 8
        putit = 0; //
        cntu = 0;
    }


//     dfs1 para liberar memoria
	
    public static void fredfs1(TREE now) {

        if (now.Lchild == null && now.Rchild == null) {
            now = null;
            return;
        }
        if (now.Lchild != null) {
            fredfs1(now.Lchild);
        }
        if (now.Rchild != null) {
            fredfs1(now.Rchild);
        }
    }

   
//      dfs1 para hacer los códigos.
	
    // public static void dfs1(TREE now,int code,String st)
    public static void dfs1(TREE now, String st) {
        now.deb = st;
        if ((now.Lchild == null) && (now.Rchild == null)) {
            ss1[now.Bite] = st;
            return;
        }
        if (now.Lchild != null) {
            dfs1(now.Lchild, st + "0");
        }
        if (now.Rchild != null) {
            dfs1(now.Rchild, st + "1");
        }
    }

//    Haciendo todos los nodos en una prioridad Q haciendo el arbol
	 
    public static void MakeNode1() {
        int i;
        cntu = 0;
        for (i = 0; i < 300; i++) {
            if (freq1[i] != 0) {

                TREE Temp = new TREE();
                Temp.Bite = i;
                Temp.freq1nc = freq1[i];
                Temp.Lchild = null;
                Temp.Rchild = null;
                pq1.add(Temp);
                cntu++;
            }

        }
        TREE Temp1, Temp2;

        if (cntu == 0) {
            return;
        } else if (cntu == 1) {
            for (i = 0; i < 300; i++) {
                if (freq1[i] != 0) {
                    ss1[i] = "0";
                    break;
                }
            }
            return;
        }

        // ¿Hay algún problema si el archivo está vacío?
        // se encuentra un error si solo hay un caracter
        while (pq1.size() != 1) {
            TREE Temp = new TREE();
            Temp1 = pq1.poll();
            Temp2 = pq1.poll();
            Temp.Lchild = Temp1;
            Temp.Rchild = Temp2;
            Temp.freq1nc = Temp1.freq1nc + Temp2.freq1nc;
            pq1.add(Temp);
        }
        Root = pq1.poll();
    }

    
//     leyendo el frecuendias de "codigos.txt" // actualizando ss
	
    public static void readfreq1(String cc) {

        File filei = new File(cc);
        int fey, i;
        Byte baital;
        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);
            cntu = data_in.readInt();

            for (i = 0; i < cntu; i++) {
                baital = data_in.readByte();
                fey = data_in.readInt();
                freq1[to(baital)] = fey;
            }
            data_in.close();
            file_input.close();
        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }

        MakeNode1(); // haciendo los nodos correspondientes
        if (cntu > 1) {
            dfs1(Root, ""); // dfs1 para hacer los códigos
        }
        for (i = 0; i < 256; i++) {
            if (ss1[i] == null) {
                ss1[i] = "";
            }
        }
        filei = null;
    }

    /**
     * int a bin la creación de código de conversión de cadena
     */
    public static void createbin() {
        int i, j;
        String t;
        for (i = 0; i < 256; i++) {
            btost[i] = "";
            j = i;
            while (j != 0) {
                if (j % 2 == 1) {
                    btost[i] += "1";
                } else {
                    btost[i] += "0";
                }
                j /= 2;
            }
            t = "";
            for (j = btost[i].length() - 1; j >= 0; j--) {
                t += btost[i].charAt(j);
            }
            btost[i] = t;
            // System.out.println(btost[i]);
        }
        btost[0] = "0";
    }

    /**
     * ****************************************************************************
     * sí, significa que temp es un código válido y póngalo en el valor *
     * correspondiente del código bitd
	 *****************************************************************************
     */
    public static int got() {
        int i;

        for (i = 0; i < 256; i++) {
            if (ss1[i].compareTo(temp) == 0) {
                putit = i;
                return 1;
            }
        }
        return 0;

    }

//    conversión de byte a int
	
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

   
//    Convierte cualquier cadena en una cadena de ocho dígitos.
     
    public static String makeeight(String b) {
        String ret = "";
        int i;
        int len = b.length();
        for (i = 0; i < (8 - len); i++) {
            ret += "0";
        }
        ret += b;
        return ret;
    }

  
//    función de descompresión
	
    public static void readbin(String zip, String unz) {
        File f1 = null, f2 = null;
        int ok, bt;
        Byte b;
        int j, i;
        bigone = "";
        f1 = new File(zip);
        f2 = new File(unz);
        try {
            FileOutputStream file_output = new FileOutputStream(f2);
            DataOutputStream data_out = new DataOutputStream(file_output);
            FileInputStream file_input = new FileInputStream(f1);
            DataInputStream data_in = new DataInputStream(file_input);
            try {
                cntu = data_in.readInt();
                System.out.println(cntu);
                for (i = 0; i < cntu; i++) {
                    b = data_in.readByte();
                    j = data_in.readInt();

                    // System.out.println(ss[to(b)]);
                }
                exbits1 = data_in.readInt();
                System.out.println(exbits1);

            } catch (EOFException eof) {
                System.out.println("Fin del archivo");
            }

            while (true) {
                try {
                    b = data_in.readByte();
                    bt = to(b);
                    bigone += makeeight(btost[bt]);

                    // System.out.println(bigone);
                    while (true) {
                        ok = 1;
                        temp = "";
                        for (i = 0; i < bigone.length() - exbits1; i++) {
                            temp += bigone.charAt(i);
                            // System.out.println(temp);
                            if (got() == 1) {
                                data_out.write(putit);
                                ok = 0;
                                String s = "";
                                for (j = temp.length(); j < bigone.length(); j++) {
                                    s += bigone.charAt(j);
                                }
                                bigone = s;
                                break;
                            }
                        }

                        if (ok == 1) {
                            break;
                        }
                    }
                } catch (EOFException eof) {
                    System.out.println("Fin del archivo");
                    break;
                }
            }
            file_output.close();
            data_out.close();
            file_input.close();
            data_in.close();
        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }

        f1 = null;
        f2 = null;
    }

    // de nuevo mejora la función
    // error si solo <= un caracter en el archivo de entrada
    public static void beginHunzipping(String arg1) {
        initHunzipping();
        readfreq1(arg1);
        createbin();
        int n = arg1.length();
        String arg2 = arg1.substring(0, n - 6);
        readbin(arg1, arg2);
        initHunzipping();
    }

    /*
	 * public static void main (String arg[]) { initHunzipping(); String
	 * arg1="in.txt.michael"; int n=arg1.length(); readfreq1(arg1); createbin();
	 * String arg2=arg1.substring(0,n-6); //mame of the zipped file,name
	 * afterextracting readbin(arg1,arg2); initHunzipping(); }
     */
}
