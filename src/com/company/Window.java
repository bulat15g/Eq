package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public final class Window extends JFrame {
    public static int maxDB = 60;
    public static ArrayList<Integer> freqsArray;
    public static int minDB = -60;
    public static ArrayList<Integer> coeffsGainArray;
    private int numOfStreams = 5;
    private static Window _instance = null;
    private static String fileName;

    public Boolean isFileChoosed = false;
    public Boolean isPlaying = false;


    private JButton playOrPause = new JButton("Play or pause button");
    private JTextField statusBar = new JTextField("setTrack", 10);
    private JCheckBox buttonEcho = new JCheckBox("Echo");
    private JCheckBox buttonDistortion = new JCheckBox("Distortion");
    private ArrayList<JSlider> slidersArray = new ArrayList<>();
    private MenuBar menuBar;
    private File musicFile;
    private Timer slidersUpdateTimer;


    private Window() {
        Player.getInstance();
        setFreqs();
        setGui();
        setSliders();
        setMenu();
        setTimers();


        setBounds(200, 200, 500, 500);
        setVisible(true);
    }

    //assembl properties
    private void setGui() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        this.add(statusBar);
        this.add(playOrPause);
        playOrPause.addActionListener(new buttonListener());
        this.add(buttonEcho);
        this.add(buttonDistortion);
    }

    //setUpMenu
    private void setMenu() {
        menuBar = new java.awt.MenuBar();
        java.awt.Menu menu = new java.awt.Menu("OpenMusicFile");
        java.awt.MenuItem txtFileItem = new java.awt.MenuItem("openMusic");
        txtFileItem.addActionListener(new buttonListener());
        menu.add(txtFileItem);
        menuBar.add(menu);
        this.setMenuBar(menuBar);
    }

    //set props of Sliders
    private void setSliders() {
        for (int i = 0; i < this.numOfStreams; i++) {

            Hashtable<Integer, JLabel> labeltable = new Hashtable<>();
            labeltable.put(maxDB, new JLabel(" " + String.valueOf(maxDB) + " dB"));
            labeltable.put((maxDB + minDB) / 2, new JLabel(
                    String.valueOf(freqsArray.get(i)) + "-" + String.valueOf(freqsArray.get(i + 1)) + " Hz"
            ));
            labeltable.put(minDB, new JLabel(" " + String.valueOf(minDB) + " dB"));

            JSlider localSlider = new JSlider(JSlider.HORIZONTAL, minDB, maxDB, 0);
            localSlider.setLabelTable(labeltable);
            localSlider.addChangeListener(new SliderListener());
            localSlider.setPaintLabels(true);
            slidersArray.add(localSlider);
        }
        slidersArray.forEach(this::add);
    }

    //set array of freqs TODO
    private void setFreqs() {
        freqsArray = new ArrayList<>();
        freqsArray.add(6);
        freqsArray.add(36);
        freqsArray.add(216);
        freqsArray.add(1296);
        freqsArray.add(8145);
        freqsArray.add(944667);
    }

    private void setTimers() {
        slidersUpdateTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < numOfStreams; i++) {
                    coeffsGainArray.set(i, slidersArray.get(i).getValue());
                }
            }
        });
    }

    //refresh's slidersArray value
    class SliderListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            System.out.println(source.getValue());
        }
    }

    //actionListener
    class buttonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            switch (e.getActionCommand()) {
                case "openMusic":
                    JFileChooser fileOpener = new JFileChooser();
                    FileNameExtensionFilter fileNameExtensionFilter =
                            new FileNameExtensionFilter("wav", "wav");
                    fileOpener.setFileFilter(fileNameExtensionFilter);
                    int ret = fileOpener.showDialog(null, "Открыть файл");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        musicFile = fileOpener.getSelectedFile();
                        statusBar.setText("fileChoosed");
                        isFileChoosed = true;
                    }
                    break;
                case "Play or pause button":
                    if (isFileChoosed & !isPlaying) {
                        Player.setFile(musicFile);
                        statusBar.setText("playing");
                        Player.play();
                    }
                    if (isFileChoosed & isPlaying) {
                        //.....
                        statusBar.setText("playing");
                    }

                    break;

                default:
                    System.out.println(e.getActionCommand());
                    break;
            }
        }
    }

    public static synchronized Window getInstance() {
        if (_instance == null)
            _instance = new Window();
        return _instance;
    }

}