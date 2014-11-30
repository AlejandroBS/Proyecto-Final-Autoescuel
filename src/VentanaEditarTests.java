import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentanaEditarTests extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JLabel lbl_nombreTest = new JLabel("  Nombre Test");
    private JLabel lbl_numeroPregunta = new JLabel("Número Pregunta ");
    private JLabel lbl_foto = new JLabel("Haga click para agregar una foto   ");
    private JLabel lbl_enunciado = new JLabel("Enunciado:");
    private JLabel lbl_respuestaA = new JLabel("Respuesta A: ");
    private JLabel lbl_respuestaB = new JLabel("Respuesta B: ");
    private JLabel lbl_respuestaC = new JLabel("Respuesta C: ");

    private JRadioButton rad_respuestaA = new JRadioButton("A)");
    private JRadioButton rad_respuestaB = new JRadioButton("B)");
    private JRadioButton rad_respuestaC = new JRadioButton("C)");
    private ButtonGroup grp_rad_respuestas = new ButtonGroup();

    private JTextArea txt_enunciado = new JTextArea("");
    private JTextArea txt_respuestaA = new JTextArea("");
    private JTextArea txt_respuestaB = new JTextArea("");
    private JTextArea txt_respuestaC = new JTextArea("");

    private GridBagLayout gbl_pnl_elementos = new GridBagLayout();

    private JButton btn_anterior = new JButton();
    private JButton btn_siguiente = new JButton();
    private JButton btn_reestablecerCampos = new JButton("Reestablecer Campos");
    private JButton btn_guardarPregunta = new JButton("Guardar Pregunta");
    private JButton btn_guardarTest = new JButton("GuardarTest");

    private JPanel pnl_elementos = new JPanel();
    private JPanel panel = new JPanel();
    private JPanelFoto panelFoto = new JPanelFoto(new BorderLayout());
    private JPanelFoto panel_1 = new JPanelFoto();

    //-------------------------------------------------------------------------------------\\
    private int indicePregunta = 0, numPreguntasMaximas = 0;
    private Test test;
    private final String rutaTest;
    private String rutaImgActual = "";
    private ImageIcon imgActual = null;

    public VentanaEditarTests(final String rutaTest, final String str_nombreTest, int numPregMax) {
        //System.out.println("rutatest: "+rutaTest+"--str_nombretest: "+str_nombreTest+"--numPegMax: "+numPregMax);
        this.setSize(800, 650);
        this.setLocationRelativeTo(null);
        this.rutaTest = rutaTest;
        // inserto los elementos en la ventana
        insertarElementos();

        numPreguntasMaximas = numPregMax;
        lbl_nombreTest.setText(str_nombreTest);
        lbl_numeroPregunta.setText("Pregunta: 1");

        btn_anterior.setEnabled(false);

        panelFoto.setLayout(new BorderLayout(0, 0));
        panelFoto.add(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));
        panel_1.add(lbl_foto);

        setVisible(true);
        btn_guardarPregunta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg) {
                System.out.println(rutaImgActual);

                String enun = "", resA = "", resB = "", resC = "";
                char resCorrecta = 0;

                enun = txt_enunciado.getText();
                resA = txt_respuestaA.getText();
                resB = txt_respuestaB.getText();
                resC = txt_respuestaC.getText();

                if (grp_rad_respuestas.isSelected(rad_respuestaA.getModel())) {
                    resCorrecta = 'A';
                }
                if (grp_rad_respuestas.isSelected(rad_respuestaB.getModel())) {
                    resCorrecta = 'B';
                }
                if (grp_rad_respuestas.isSelected(rad_respuestaC.getModel())) {
                    resCorrecta = 'C';
                }

                Pregunta pActual = new Pregunta(str_nombreTest, indicePregunta, enun, resA, resB, resC, resCorrecta);

                test.add(pActual, indicePregunta);

            }
        });

        btn_guardarTest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    FileOutputStream f = new FileOutputStream(rutaTest);
                    ObjectOutputStream oos = new ObjectOutputStream(f);
                    //test.aumentarVersion();
                    oos.writeInt(test.getNumPregMax());
                    oos.writeInt(test.getNumPregInsertadas());
                    oos.writeObject(test);
                    oos.close();

                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                enviarFicheroAServidor();
            }

        });

        btn_anterior.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btn_reestablecerCampos.doClick();
                if (indicePregunta >= 1) {
                    indicePregunta--;
                    lbl_numeroPregunta.setText("Pregunta: " + (indicePregunta + 1));
                    //System.out.println(test.getPregunta(indicePregunta).toString());
                    pintarPregunta();
                }
                if (indicePregunta == 0) {
                    btn_anterior.setEnabled(false);
                }
                btn_siguiente.setEnabled(true);

            }
        });

        btn_reestablecerCampos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imgActual = null;
                panel_1.pintar(lbl_foto);
                repaint();
                panel_1.setBorder(LineBorder.createGrayLineBorder());
                setVisible(false);
                setVisible(true);
                lbl_foto.setText("   Haga click para agregar una foto   ");
                panel_1.add(lbl_foto);
                txt_enunciado.setText("");
                txt_respuestaA.setText("");
                txt_respuestaB.setText("");
                txt_respuestaC.setText("");
                grp_rad_respuestas.clearSelection();
            }
        });

        btn_siguiente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                btn_reestablecerCampos.doClick();
                if (indicePregunta < numPreguntasMaximas) {
                    indicePregunta++;
                    lbl_numeroPregunta.setText("Pregunta: " + (indicePregunta + 1));
                    pintarPregunta();
                }
                btn_anterior.setEnabled(true);

                if (indicePregunta == numPreguntasMaximas - 1) {
                    btn_siguiente.setEnabled(false);
                }
            }
        });

        panel_1.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                repaint();
                JFileChooser jfc = new JFileChooser();
                FileNameExtensionFilter fnex = new FileNameExtensionFilter("Fotografias", "jpg", "png", "bmp");
                jfc.setFileFilter(fnex);
                jfc.setCurrentDirectory(new File("data"));
                int eleccion = jfc.showSaveDialog(null);
                if (eleccion == JFileChooser.APPROVE_OPTION) {
                    panel_1.remove(lbl_foto);
                    //lbl_foto.setText("");
                    panel_1.setPreferredSize(new Dimension(220, 200));
                    rutaImgActual = jfc.getSelectedFile().getPath();
                    System.out.println("getPath() -- " + jfc.getSelectedFile().getPath());
                    System.out.println("getAbsolutePath() -- " + jfc.getSelectedFile().getAbsolutePath());
                    try {
                        System.out.println("getCanonicalPath() -- " + jfc.getSelectedFile().getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    System.out.println("getName() -- " + jfc.getSelectedFile().getName());
                    panel_1.pintar(new ImageIcon(jfc.getSelectedFile().getPath()));
                    panel_1.setSize(30, 30);

                    setVisible(false);
                    setVisible(true);

                    File fo = new File(rutaImgActual);
                    File fd = new File("data" + File.separator + str_nombreTest + File.separator + "img" + File.separator + "p" + indicePregunta + ".jpg");

                    try {
                        if (fd.exists()) {
                            fd.delete();
                        }
                        fd.createNewFile();

                        Path FROM = Paths.get(rutaImgActual);
                        Path TO = Paths.get("data" + File.separator + str_nombreTest + File.separator + "img" + File.separator + "p" + indicePregunta + ".jpg");
                        CopyOption[] options;
                        options = new CopyOption[]{
                            StandardCopyOption.REPLACE_EXISTING
                        };
                        Files.copy(FROM, TO, options);

                    } catch (IOException ex) {
                        Logger.getLogger(VentanaEditarTests.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });

        cargarPreguntas();

        pintarPregunta();

    }

    public void pintarPregunta() {
        if (test.getPregunta(indicePregunta) != null) {

            panel_1.remove(lbl_foto);
            txt_enunciado.setText(test.getPregunta(indicePregunta).getEnunciado());
            txt_respuestaA.setText(test.getPregunta(indicePregunta).getRespuestaA());
            txt_respuestaB.setText(test.getPregunta(indicePregunta).getRespuestaB());
            txt_respuestaC.setText(test.getPregunta(indicePregunta).getRespuestaC());

            lbl_foto.setText("");
            panel_1.setPreferredSize(new Dimension(220, 200));
            panel_1.pintar(test.getPregunta(indicePregunta).getImagen());
            panel_1.setSize(30, 30);

            setVisible(false);
            setVisible(true);

            if (test.getPregunta(indicePregunta).getRespuestaCorrecta() == 'A') {
                grp_rad_respuestas.setSelected(rad_respuestaA.getModel(), true);
            }
            if (test.getPregunta(indicePregunta).getRespuestaCorrecta() == 'B') {
                grp_rad_respuestas.setSelected(rad_respuestaB.getModel(), true);
            }
            if (test.getPregunta(indicePregunta).getRespuestaCorrecta() == 'C') {
                grp_rad_respuestas.setSelected(rad_respuestaC.getModel(), true);
            }
        }

    }

    public void cargarPreguntas() {
        try {
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(rutaTest));
            numPreguntasMaximas = oos.readInt();
            int numPregInsertadas = oos.readInt();

            test = (Test) oos.readObject();
            oos.close();
            if (numPregInsertadas <= 0) {
                test = new Test(numPreguntasMaximas);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public boolean preguntaCompleta(Pregunta p) {
        boolean completa = true;
        if (p.getEnunciado().length() == 0) {
            completa = false;
        }
        if (p.getRespuestaA().length() == 0) {
            completa = false;
        }
        if (p.getRespuestaB().length() == 0) {
            completa = false;
        }
        if (p.getRespuestaC().length() == 0) {
            completa = false;
        }
        if (p.getRespuestaCorrecta() != 'A' && p.getRespuestaCorrecta() != 'B' && p.getRespuestaCorrecta() != 'C') {
            completa = false;
        }
        if (imgActual != null) {
            completa = false;
        }
        return completa;
    }

    public void enviarFicheroAServidor() {
        Compressor comp = new Compressor("data", lbl_nombreTest.getText() + ".zip");
        comp.compress("data" + File.separator + lbl_nombreTest.getText(), lbl_nombreTest.getText());
        comp.close();
        System.out.println("fin");
        
        int tamano = 1024;
        byte[] buffer = new byte[tamano];
        int leido = 0;
        
        try {
            Socket socket = new Socket("127.0.0.1",8440);
            ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            
            salida.writeInt(-1);
                salida.flush();  
            salida.writeInt(3);
                salida.flush();
            
            String fileName = lbl_nombreTest.getText();
            salida.writeUTF(fileName);
                salida.flush();
            File f = new File("data"+File.separator+fileName+".zip");
            FileInputStream fis = new FileInputStream(f);
            
            
            salida.writeInt(tamano);
                salida.flush();
            
            while((leido=fis.read(buffer))>0){
               
                salida.writeInt(leido);
                salida.writeObject(buffer);
                /*for(int i = 0;i<leido;i++){
                    System.out.println(i+" // "+buffer[i]);
                }*/
                entrada.readBoolean();
                buffer = new byte[tamano];
            }
            salida.writeInt(0);
                salida.flush();
           
            fis.close();
            socket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(VentanaEditarTests.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void insertarElementos() {

        gbl_pnl_elementos.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        gbl_pnl_elementos.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0};

        pnl_elementos.setLayout(gbl_pnl_elementos);

        GridBagConstraints gbc_label_3 = new GridBagConstraints();
        gbc_label_3.insets = new Insets(0, 0, 5, 5);
        gbc_label_3.gridx = 0;
        gbc_label_3.gridy = 0;
        getContentPane().setLayout(new BorderLayout(0, 0));
		//pnl_elementos.add(label_3, gbc_label_3);

        //lbl_nombreTest.setBorder(LineBorder.createGrayLineBorder());
        GridBagConstraints gbc_lbl_nombreTest_1 = new GridBagConstraints();
        gbc_lbl_nombreTest_1.gridwidth = 2;
        gbc_lbl_nombreTest_1.anchor = GridBagConstraints.WEST;
        gbc_lbl_nombreTest_1.fill = GridBagConstraints.VERTICAL;
        gbc_lbl_nombreTest_1.insets = new Insets(0, 0, 5, 5);
        gbc_lbl_nombreTest_1.gridx = 0;
        gbc_lbl_nombreTest_1.gridy = 0;
        pnl_elementos.add(lbl_nombreTest, gbc_lbl_nombreTest_1);
        //lbl_numeroPregunta.setBorder(LineBorder.createGrayLineBorder());

        GridBagConstraints gbc_lbl_numeroPregunta = new GridBagConstraints();
        gbc_lbl_numeroPregunta.gridwidth = 2;
        gbc_lbl_numeroPregunta.anchor = GridBagConstraints.WEST;
        gbc_lbl_numeroPregunta.fill = GridBagConstraints.VERTICAL;
        gbc_lbl_numeroPregunta.insets = new Insets(0, 0, 5, 5);
        gbc_lbl_numeroPregunta.gridx = 2;
        gbc_lbl_numeroPregunta.gridy = 0;
        pnl_elementos.add(lbl_numeroPregunta, gbc_lbl_numeroPregunta);

        GridBagConstraints gbc_btnGuardarPregunta = new GridBagConstraints();
        gbc_btnGuardarPregunta.fill = GridBagConstraints.BOTH;
        gbc_btnGuardarPregunta.insets = new Insets(0, 0, 5, 5);
        gbc_btnGuardarPregunta.gridx = 7;
        gbc_btnGuardarPregunta.gridy = 0;

        pnl_elementos.add(btn_guardarPregunta, gbc_btnGuardarPregunta);

        GridBagConstraints gbc_btn_guardarTest = new GridBagConstraints();
        gbc_btn_guardarTest.fill = GridBagConstraints.BOTH;
        gbc_btn_guardarTest.insets = new Insets(0, 0, 5, 5);
        gbc_btn_guardarTest.gridx = 9;
        gbc_btn_guardarTest.gridy = 0;
        pnl_elementos.add(btn_guardarTest, gbc_btn_guardarTest);
        //lbl_enunciado.setBorder(LineBorder.createGrayLineBorder());

        GridBagConstraints gbc_lbl_enunciado = new GridBagConstraints();
        gbc_lbl_enunciado.fill = GridBagConstraints.BOTH;
        gbc_lbl_enunciado.gridwidth = 6;
        gbc_lbl_enunciado.insets = new Insets(0, 0, 5, 5);
        gbc_lbl_enunciado.gridx = 3;
        gbc_lbl_enunciado.gridy = 1;
        pnl_elementos.add(lbl_enunciado, gbc_lbl_enunciado);

        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.gridheight = 5;
        gbc_panel_1.gridwidth = 3;
        gbc_panel_1.insets = new Insets(0, 0, 5, 5);
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 2;
        pnl_elementos.add(panelFoto, gbc_panel_1);

        lbl_foto.setBorder(LineBorder.createGrayLineBorder());
        txt_enunciado.setBorder(LineBorder.createGrayLineBorder());
        //txt_enunciado.setBorder(LineBorder.createGrayLineBorder());

        GridBagConstraints gbc_txt_enunciado = new GridBagConstraints();
        gbc_txt_enunciado.gridheight = 5;
        gbc_txt_enunciado.gridwidth = 8;
        gbc_txt_enunciado.insets = new Insets(0, 0, 5, 0);
        gbc_txt_enunciado.fill = GridBagConstraints.BOTH;
        gbc_txt_enunciado.gridx = 3;
        gbc_txt_enunciado.gridy = 2;
        pnl_elementos.add(txt_enunciado, gbc_txt_enunciado);
        //lbl_respuestaA.setBorder(LineBorder.createGrayLineBorder());		

        GridBagConstraints gbc_lbl_respuestaA = new GridBagConstraints();
        gbc_lbl_respuestaA.anchor = GridBagConstraints.WEST;
        gbc_lbl_respuestaA.insets = new Insets(0, 0, 5, 5);
        gbc_lbl_respuestaA.gridx = 1;
        gbc_lbl_respuestaA.gridy = 7;
        pnl_elementos.add(lbl_respuestaA, gbc_lbl_respuestaA);

        grp_rad_respuestas.add(rad_respuestaA);

        GridBagConstraints gbc_rad_respuestaA = new GridBagConstraints();
        gbc_rad_respuestaA.insets = new Insets(0, 0, 5, 5);
        gbc_rad_respuestaA.gridx = 0;
        gbc_rad_respuestaA.gridy = 8;
        pnl_elementos.add(rad_respuestaA, gbc_rad_respuestaA);
        txt_respuestaA.setBorder(LineBorder.createGrayLineBorder());

        GridBagConstraints gbc_txt_respuestaA = new GridBagConstraints();
        gbc_txt_respuestaA.gridwidth = 9;
        gbc_txt_respuestaA.insets = new Insets(0, 0, 5, 5);
        gbc_txt_respuestaA.fill = GridBagConstraints.BOTH;
        gbc_txt_respuestaA.gridx = 1;
        gbc_txt_respuestaA.gridy = 8;
        pnl_elementos.add(txt_respuestaA, gbc_txt_respuestaA);

        GridBagConstraints gbc_lbl_respuestaB = new GridBagConstraints();
        gbc_lbl_respuestaB.anchor = GridBagConstraints.WEST;
        gbc_lbl_respuestaB.insets = new Insets(0, 0, 5, 5);
        gbc_lbl_respuestaB.gridx = 1;
        gbc_lbl_respuestaB.gridy = 9;
        pnl_elementos.add(lbl_respuestaB, gbc_lbl_respuestaB);
        grp_rad_respuestas.add(rad_respuestaB);

        GridBagConstraints gbc_rad_respuestaB = new GridBagConstraints();
        gbc_rad_respuestaB.insets = new Insets(0, 0, 5, 5);
        gbc_rad_respuestaB.gridx = 0;
        gbc_rad_respuestaB.gridy = 10;
        pnl_elementos.add(rad_respuestaB, gbc_rad_respuestaB);
        //lbl_respuestaB.setBorder(LineBorder.createGrayLineBorder());
        txt_respuestaB.setBorder(LineBorder.createGrayLineBorder());

        GridBagConstraints gbc_txt_respuestaB = new GridBagConstraints();
        gbc_txt_respuestaB.gridwidth = 9;
        gbc_txt_respuestaB.insets = new Insets(0, 0, 5, 5);
        gbc_txt_respuestaB.fill = GridBagConstraints.BOTH;
        gbc_txt_respuestaB.gridx = 1;
        gbc_txt_respuestaB.gridy = 10;
        pnl_elementos.add(txt_respuestaB, gbc_txt_respuestaB);

        GridBagConstraints gbc_lbl_respuestaC = new GridBagConstraints();
        gbc_lbl_respuestaC.anchor = GridBagConstraints.WEST;
        gbc_lbl_respuestaC.insets = new Insets(0, 0, 5, 5);
        gbc_lbl_respuestaC.gridx = 1;
        gbc_lbl_respuestaC.gridy = 11;
        pnl_elementos.add(lbl_respuestaC, gbc_lbl_respuestaC);
        grp_rad_respuestas.add(rad_respuestaC);

        GridBagConstraints gbc_rad_respuestaC = new GridBagConstraints();
        gbc_rad_respuestaC.insets = new Insets(0, 0, 5, 5);
        gbc_rad_respuestaC.gridx = 0;
        gbc_rad_respuestaC.gridy = 12;
        pnl_elementos.add(rad_respuestaC, gbc_rad_respuestaC);

        getContentPane().add(pnl_elementos, BorderLayout.CENTER);
        //lbl_respuestaC.setBorder(LineBorder.createGrayLineBorder());
        txt_respuestaC.setBorder(LineBorder.createGrayLineBorder());

        GridBagConstraints gbc_txt_respuestaC = new GridBagConstraints();
        gbc_txt_respuestaC.gridwidth = 9;
        gbc_txt_respuestaC.insets = new Insets(0, 0, 5, 5);
        gbc_txt_respuestaC.fill = GridBagConstraints.BOTH;
        gbc_txt_respuestaC.gridx = 1;
        gbc_txt_respuestaC.gridy = 12;
        pnl_elementos.add(txt_respuestaC, gbc_txt_respuestaC);

        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 0, 5);
        gbc_panel.gridwidth = 10;
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 13;
        pnl_elementos.add(panel, gbc_panel);
        panel.setLayout(new GridLayout(0, 3, 0, 0));

        btn_anterior.setToolTipText("Pregunta anterior");
        btn_anterior.setIcon(new ImageIcon("resources/flechaIzq.png"));

        panel.add(btn_anterior);
        btn_reestablecerCampos.setToolTipText("Poner los campos en blanco");

        panel.add(btn_reestablecerCampos);
        btn_siguiente.setToolTipText("siguiente pregunta");
        btn_siguiente.setIcon(new ImageIcon("resources/flechaDer.png"));

        panel.add(btn_siguiente);

    }

}
