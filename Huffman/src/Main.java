

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main extends JFrame implements ActionListener {
    // Definición de valores globales y elementos que forman parte de la GUI.

    static public File opened_file, other_file;
    static long past, future;
    static JLabel redLabel, blueLabel, redScore, blueScore;
    static JPanel buttonPanel, titlePanel, scorePanel;
    static JButton ZH, UH, EX;

    public JPanel createContentPane() {
        // Creamos un JPanel inferior para colocar todo en.
        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(null);

        titlePanel = new JPanel();
        titlePanel.setLayout(null);
        titlePanel.setLocation(90, 20);
        titlePanel.setSize(170, 70);
        totalGUI.add(titlePanel);

        redLabel = new JLabel("Tamaño de archivo seleccionado: ");
        redLabel.setLocation(43, 0);
        redLabel.setSize(150, 30);
        redLabel.setHorizontalAlignment(0);
        titlePanel.add(redLabel);

        blueLabel = new JLabel("Después de zip / "
                + "descomprimir el tamaño del archivo:");
        blueLabel.setLocation(10, 30);
        blueLabel.setSize(170, 30);
        blueLabel.setHorizontalAlignment(0);
        titlePanel.add(blueLabel);

        scorePanel = new JPanel();
        scorePanel.setLayout(null);
        scorePanel.setLocation(270, 20);
        scorePanel.setSize(120, 60);
        totalGUI.add(scorePanel);

        redScore = new JLabel("");
        redScore.setLocation(0, 0);
        redScore.setSize(200, 30);
        redScore.setHorizontalAlignment(0);
        scorePanel.add(redScore);

        blueScore = new JLabel("");
        blueScore.setLocation(0, 30);
        blueScore.setSize(100, 30);
        blueScore.setHorizontalAlignment(0);
        scorePanel.add(blueScore);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setLocation(10, 130);
        buttonPanel.setSize(5200, 150);
        totalGUI.add(buttonPanel);

        ZH = new JButton("Comprimir Huffman");
        ZH.setLocation(0, 0);
        ZH.setSize(150, 50);
        ZH.addActionListener(this);
        buttonPanel.add(ZH);

        UH = new JButton("Descomprimir Huffman");
        UH.setLocation(210, 0);
        UH.setSize(150, 50);
        UH.addActionListener(this);
        buttonPanel.add(UH);

        EX = new JButton("Salir");
        EX.setLocation(130, 70);
        EX.setSize(250, 30);
        EX.addActionListener(this);
        buttonPanel.add(EX);

        totalGUI.setOpaque(true);
        return totalGUI;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ZH) {
            Comprimir.beginHzipping(opened_file.getPath());
            JOptionPane.showMessageDialog(null, "..........................Compresion terminado..........................",
                    "Estado", JOptionPane.PLAIN_MESSAGE);
            redScore.setText(opened_file.length() + "Bytes");
            other_file = new File(opened_file.getPath() + ".michael");
            future = other_file.length();
            blueScore.setText(future + "Bytes");
        } else if (e.getSource() == UH) {
            Descomprimir.beginHunzipping(opened_file.getPath());
            JOptionPane.showMessageDialog(null, "..........................Descompresion terminada..........................", "Estado",
                    JOptionPane.PLAIN_MESSAGE);
            redScore.setText(opened_file.length() + "Bytes");
            String s = opened_file.getPath();
            s = s.substring(0, s.length() - 6);
            other_file = new File(s);
            future = other_file.length();
            blueScore.setText(future + "Bytes");

        } else if (e.getSource() == EX) {
            System.exit(0);
        }
    }

    private static void createAndShowGUI() {

        // JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("HuffmanCompresser");

        // Crea y configura el panel de contenido.
        Main demo = new Main();
        frame.setContentPane(demo.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(350, 170, 410, 300);

        frame.setVisible(true);

        JMenu fileMenu = new JMenu("Archivo");

        JMenuBar bar = new JMenuBar();
        frame.setJMenuBar(bar);
        bar.add(fileMenu);

        JMenuItem openItem = new JMenuItem("Abrir");
        fileMenu.add(openItem);
        openItem.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int result = fileChooser.showOpenDialog(null);
                opened_file = fileChooser.getSelectedFile();
                past = opened_file.length();
                redScore.setText(past + "Bytes");
                blueScore.setText("Aún no calculado");
            }
        });

        JMenuItem exitItem = new JMenuItem("Salir");
        fileMenu.add(exitItem);
        exitItem.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        JMenu helpMenu = new JMenu("Ayuda");
        frame.setJMenuBar(bar);
        bar.add(helpMenu);

        JMenuItem helpItem = new JMenuItem("Como Usar");
        helpMenu.add(helpItem);
        helpItem.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null,
                        "Se utilizan los algoritmos para la compresión archivos de huffman" + "\n"
                        + "1. Huffman Codigo" + "\n"
                        + "Para comprimir un archivo, primero abra el archivo y luego haga clic en abrir." + "\n"
                        + "Luego seleccione Comprimir Huffman para comprimir el archivo usando Huffmans.  Esto creara el archivo huffman" + "\n"
                        + "el archivo con la extensión .huffz se creará en el mismo" + "\n"
                        + "Selecione Archivo y luego selecione el archivo comprimido. Durante la descompresión, simplemente selecione" + "\n"
                        + "este archivo y haga clic en el botón Descoprimir huffman" + "\n" + "\n",
                        "How To...", JOptionPane.PLAIN_MESSAGE);
            }
        });

        JMenuItem aboutItem = new JMenuItem("Autor");
        helpMenu.add(aboutItem);

        aboutItem.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null,
                        "HuffmanCompresser es un software para comprimir y descomprimir archivos" + "\n"
                        + "Facultad de Ingenieria " + "\n"
                        + "Ingenieria de Sistemas" + "\n"
                        + "Michael Daniel Murillo López" + "\n"
                        + "Corporación Universitaria Minuto de Dios.",
                        "Sobre", JOptionPane.PLAIN_MESSAGE);
            }
        });

    }

    public static void main(String[] args) {
        // Programar un trabajo para el hilo de despacho de eventos:
        // creando y mostrando la GUI de esta aplicación.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
