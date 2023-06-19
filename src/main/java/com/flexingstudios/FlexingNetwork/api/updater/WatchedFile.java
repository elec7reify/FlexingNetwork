package com.flexingstudios.flexingnetwork.api.updater;

import com.flexingstudios.flexingnetwork.api.event.FileUpdateEvent;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class WatchedFile extends WatchedEntry {
    final File file;
    long length;
    long lastModified;

    WatchedFile(String path, File file) {
        super(path);
        this.file = file;
        this.length = file.length();
        this.lastModified = file.lastModified();
    }

    public File getUpdateFile() {
        return this.file;
    }

    public boolean hasChanges(WatchedEntry entry) {
        if (!(entry instanceof WatchedFile))
            return true;
        WatchedFile curr = (WatchedFile)entry;
        if (curr.length != this.length || curr.lastModified != this.lastModified) {
            Bukkit.getPluginManager().callEvent(new FileUpdateEvent(this, curr));
            this.length = curr.length;
            this.lastModified = curr.lastModified;
        }
        return false;
    }

    public void copyUpdate() throws IOException {
        Files.copy(this.file.toPath(), getLocalMirror().toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public String toString() {
        return "File: " + this.path + " => " + this.file.getAbsolutePath();
    }
}
