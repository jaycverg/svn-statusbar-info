package com.jaycverg.svnstatusbarwidget;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.svn.SvnVcs;

import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;

public class SvnRelativePathStatusBarWidget implements StatusBarWidget, StatusBarWidget.TextPresentation {

    public static final String ID = "svn-relative-path";

    private StatusBar statusBar;

    public SvnRelativePathStatusBarWidget() {
    }

    @NotNull
    @Override
    public String ID() {
        return ID;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        this.statusBar = statusBar;
        statusBar.updateWidget(ID());
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation() {
        return this;
    }

    @NotNull
    @Override
    public String getText() {
        if (statusBar != null) {
            var project = statusBar.getProject();
            var svn = SvnVcs.getInstance(project);
            var info = svn.getInfo(project.getBasePath());
            var path = info.getUrl().toString().replace(info.getRepositoryRootUrl().toString(), "");
            return "[" + path + "]";
        }
        return "[Fetching...]";
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
