package net.softwarepage.facharbeit.normalgame.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.stage.FileChooser;
import net.softwarepage.facharbeit.normalgame.logic.NormalGame;

public class FileManager {

    public static File getCorrectedFile(File file) {
        String newName = file.getAbsolutePath();
        if (file.getName().contains(".")) {
            newName = file.getAbsolutePath().split("\\.")[0];
        }
        newName += ".game";
        return new File(newName);
    }

    public static void saveGame(File file, NormalGame game) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(game);
    }

    public static NormalGame loadGame(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        NormalGame game = (NormalGame) ois.readObject();
        return game;
    }

    public static FileChooser createSavingFileChooser() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.setTitle("Spiel speichern");
        fc.setInitialFileName("spiel.game");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Spiele", "*.game");
        fc.setSelectedExtensionFilter(extFilter);
        return fc;
    }
    
    public static FileChooser createLoadingFileChooser() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.setTitle("Spiel laden");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Spiele", "*.game");
        fc.getExtensionFilters().add(extFilter);
        return fc;
    }
}
