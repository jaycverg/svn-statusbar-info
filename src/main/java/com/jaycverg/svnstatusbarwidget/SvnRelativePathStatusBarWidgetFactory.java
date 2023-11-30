package com.jaycverg.svnstatusbarwidget;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;

public class SvnRelativePathStatusBarWidgetFactory implements StatusBarWidgetFactory {

    @Override
    public @NotNull @NonNls String getId() {
        return SvnRelativePathStatusBarWidget.ID;
    }

    @Override
    public @NotNull @NlsContexts.ConfigurableName String getDisplayName() {
        return "SVN Relative Path";
    }

    @Override
    public @NotNull StatusBarWidget createWidget(@NotNull Project project) {
        return new SvnRelativePathStatusBarWidget();
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        var svnDir = Paths.get(project.getBasePath(), ".svn");
        return Files.exists(svnDir);
    }
}