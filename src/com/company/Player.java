package com.company;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Player {
    private static Player _instance = null;
    static File file;
    private static AudioInputStream audioInputStream;

    private Player() {

    }

    public static void setFile(File file) {
        Player.file = file;
    }

    public static void play() {
        try {
            audioInputStream =
                    AudioSystem.getAudioInputStream(file.getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public static synchronized Player getInstance() {
        if (_instance == null)
            _instance = new Player();
        return _instance;
    }
}
