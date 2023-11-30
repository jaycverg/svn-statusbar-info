package com.jaycverg.svnstatusbarwidget;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileManagerListener;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.impl.status.TextPanel;
import com.intellij.ui.ClickListener;

public class SvnRelativePathStatusBarWidget extends TextPanel implements CustomStatusBarWidget {

    public static final String ID = "svn-relative-path";

    private String projectBasePath;

    public SvnRelativePathStatusBarWidget() {
        new ClickListener() {
            @Override
            public boolean onClick(@NotNull MouseEvent mouseEvent, int i) {
                setInfo();
                return true;
            }
        }.installOn(this);
    }

    @NotNull
    @Override
    public String ID() {
        return ID;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        VirtualFileManager.getInstance().addVirtualFileManagerListener(new VirtualFileManagerListener() {
            @Override
            public void beforeRefreshStart(boolean asynchronous) {
            }

            @Override
            public void afterRefreshFinish(boolean asynchronous) {
                setInfo();
            }
        }, this);
        projectBasePath = statusBar.getProject().getBasePath();
        setInfo();
    }

    private void setInfo() {
        var info = getSvnInfo(projectBasePath);
        setText(info);
    }

    private String getSvnInfo(String basePath) {
        try {
            var projDir = Paths.get(basePath).toFile();
            var proc = new ProcessBuilder("/opt/homebrew/bin/svn", "info")
                    .directory(projDir)
                    .start();

            try (var reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                var info = reader.lines()
                        .filter(s -> s.startsWith("Relative URL"))
                        .findFirst()
                        .orElse("")
                        .split(":");
                var path = info.length > 1 ? info[1].trim().substring(2) : "SVN Project";
                return "[" + path + "]";
            }
        } catch (IOException e) {
            System.out.println("Failed to get the SVN info. SVN cli may not be installed.");
            e.printStackTrace();
        }

        return "";
    }
}
