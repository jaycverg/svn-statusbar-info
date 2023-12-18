package com.jaycverg.svnstatusbarwidget;

import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.svn.SvnVcs;

import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;

public class SvnRelativePathStatusBarWidget implements StatusBarWidget, StatusBarWidget.TextPresentation {

    public static final String ID = "svn-relative-path";
    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    private StatusBar statusBar;
    private String text = "Loading...";

    @Nullable
    @Override
    public Consumer<MouseEvent> getClickConsumer() {
        return (e) -> onClick();
    }

    private void onClick() {
        statusBar.updateWidget(ID);
    }

    @NotNull
    @Override
    public String ID() {
        return ID;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        this.statusBar = statusBar;
        setRelativePath();
    }

    private void setRelativePath() {
        System.out.println("executor: " + exec);
        exec.execute(() -> {
            var project = statusBar.getProject();
            if (project == null)
                return;

            var svn = SvnVcs.getInstance(project);
            if (project.getBasePath() == null)
                return;

            var info = svn.getInfo(project.getBasePath());
            if (info == null || info.getUrl() == null || info.getRepositoryRootUrl() == null)
                return;

            text = info.getUrl().toString().replace(info.getRepositoryRootUrl().toString(), "");
            statusBar.updateWidget(ID());
        });
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation() {
        return this;
    }

    @NotNull
    @Override
    public String getText() {
        return text;
    }

    @Override
    public float getAlignment() {
        return 0;
    }

    @Nullable
    @Override
    public String getTooltipText() {
        return getText();
    }
}
