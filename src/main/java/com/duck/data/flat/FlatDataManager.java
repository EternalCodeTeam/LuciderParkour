package com.duck.data.flat;

import com.duck.LuciderParkour;
import com.duck.utils.FileUtils;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;

@Getter
public class FlatDataManager {

    private File generalConfigurationFile;
    private File userConfigurationFile;
    private File parkourConfigurationFile;
    private File categoriesConfigurationFile;

    public void buildFileConfigurationModel(File filesDirectory) throws IOException, URISyntaxException {

        if(!filesDirectory.exists())
            Files.createDirectory(filesDirectory.toPath());

        this.generalConfigurationFile = new File(filesDirectory, "general.yml");
        this.userConfigurationFile = new File(filesDirectory, "users.yml");
        this.parkourConfigurationFile = new File(filesDirectory, "arenas.yml");
        this.categoriesConfigurationFile = new File(filesDirectory, "categories.yml");

        if(!userConfigurationFile.exists()){
            InputStream inputStream = LuciderParkour.getInstance().getResource("users.yml");

            Files.createFile(userConfigurationFile.toPath());
            FileUtils.copyFile(inputStream, userConfigurationFile);
        }

        if(!categoriesConfigurationFile.exists()){
            InputStream inputStream = LuciderParkour.getInstance().getResource("categories.yml");

            Files.createFile(categoriesConfigurationFile.toPath());
            FileUtils.copyFile(inputStream, categoriesConfigurationFile);
        }

        if(!parkourConfigurationFile.exists()){
            InputStream inputStream = LuciderParkour.getInstance().getResource("arenas.yml");

            Files.createFile(parkourConfigurationFile.toPath());
            FileUtils.copyFile(inputStream, parkourConfigurationFile);
        }
    }

}
