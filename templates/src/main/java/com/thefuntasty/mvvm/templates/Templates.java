package com.thefuntasty.mvvm.templates;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileTree;
import org.gradle.api.tasks.Copy;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Templates implements Plugin<Project> {

    private static final String TASK_NAME = "copyTemplates";
    private static final String TASK_GROUP = "thefuntasty";
    private static final String TEMPLATES = "templates";
    private static final String AS_REGEX = "/[A-Za-z/]*/[Aa]ndroid[ -][Ss]tudio[ 0-9A-Za-z.()]*\\(/Contents\\)\\?";
    private static final String AS_TEMPLATES = "/plugins/android/lib/templates/other";

    @Override
    public void apply(Project project) {
        project.getTasks().create(TASK_NAME, Copy.class, task -> {
            task.setGroup(TASK_GROUP);

            copyToBuildDir(task, project);

            task.doLast(t -> {
                System.out.println("Copy templates");

                List<String> paths = getAndroidStudioVersions(project);
                List<String> templates = getRousourceFolders(project);

                if (paths.size() <= 0) {
                    System.out.println("\nThere is no Android Studio to copy templates!");
                    return;
                }

                System.out.println("\nNew templates:");
                for (String template : templates) {
                    System.out.println("\t> " + template);
                }

                System.out.println("Versions:");
                for (String path : paths) {
                    System.out.println("\t> " + path + AS_TEMPLATES);
                    removeOldTemplatesFromAndroidStudio(path, templates, project);
                    copyToAndroidStudio(path, project);
                }

                deleteTemporaryFiles(project);

                System.out.println("\nTemplates copied, restart Android Studio, please!");
            });
        });
    }

    /**
     * Copy templates from dependency jar file to build dir
     *
     * @param task    {@link Copy}
     * @param project {@link Project}
     */
    private void copyToBuildDir(Copy task, Project project) {
        String resources = getClass().getProtectionDomain().getCodeSource().getLocation().toExternalForm();
        FileTree from = project.zipTree(resources)
                .matching(p -> p.include(TEMPLATES + "/**"))
                .getAsFileTree();

        task.from(from, t -> t.include("**/"));
        task.into(project.getBuildDir());
    }

    /**
     * Get Android Studio paths from running processes
     *
     * @param project {@link Project}
     * @return path list
     */
    private List<String> getAndroidStudioVersions(Project project) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        project.exec(e -> {
            e.commandLine(
                    "bash",
                    "-c",
                    "ps -ax | grep --only-matching '" + AS_REGEX + "' | sort --unique"
            );
            e.setStandardOutput(out);
        });

        List<String> lines = new ArrayList<>(Arrays.asList(out.toString().split("[\\r\\n]+")));
        lines.removeAll(Arrays.asList("", null));

        return lines;
    }

    /**
     * Get all new templates
     *
     * @param project {@link Project}
     * @return path list
     */
    private List<String> getRousourceFolders(Project project) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        project.exec(e -> {
            e.commandLine(
                    "ls",
                    project.getBuildDir().getAbsolutePath() + "/" + TEMPLATES
            );
            e.setStandardOutput(out);
        });

        List<String> lines = new ArrayList<>(Arrays.asList(out.toString().split("[\\r\\n\\t ]+")));
        lines.removeAll(Arrays.asList("", null));

        return lines;
    }

    /**
     * Remove all old templates from Android Studio
     *
     * @param path      {@link String}
     * @param templates {@link List}
     * @param project   {@link Project}
     */
    private void removeOldTemplatesFromAndroidStudio(String path, List<String> templates, Project project) {
        for (String template : templates) {
            String deletePath = path + AS_TEMPLATES + "/" + template;
            project.exec(e -> e.commandLine(
                    "rm",
                    "-rf",
                    deletePath
            ));
        }
    }

    /**
     * Copy templates to Android Studio
     *
     * @param path    {@link String}
     * @param project {@link Project}
     */
    private void copyToAndroidStudio(String path, Project project) {
        project.exec(e -> e.commandLine(
                "cp",
                "-af",
                project.getBuildDir().getAbsolutePath() + "/" + TEMPLATES + "/.",
                path + AS_TEMPLATES + "/."
        ));
    }

    /**
     * Delete temporary files from build dir
     *
     * @param project {@link Project}
     */
    private void deleteTemporaryFiles(Project project) {
        project.exec(e -> e.commandLine(
                "rm",
                "-rf",
                project.getBuildDir().getAbsolutePath() + "/" + TEMPLATES
        ));
    }
}
