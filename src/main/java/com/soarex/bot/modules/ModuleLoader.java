package com.soarex.bot.modules;

import com.soarex.bot.SoarexBot;
import com.soarex.bot.api.IModule;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarFile;

/**
 * Created by shumaf on 04.05.16.
 */
public class ModuleLoader {

    private static final List<Class<? extends IModule>> modulesClasses = new CopyOnWriteArrayList<>();

    private static List<IModule> modules = new CopyOnWriteArrayList<>();

    static {
        File modulesDir = new File(SoarexBot.MODULE_DIR);
        if (modulesDir.exists()) {
            if (!modulesDir.isDirectory()) {
                throw new RuntimeException(SoarexBot.MODULE_DIR + " isn't a directory");
            }
        } else {
            if (!modulesDir.mkdir()) {
                throw new RuntimeException("Error creating " + modulesDir + " directory");
            }
        }

        File [] files = modulesDir.listFiles((FilenameFilter) FileFilterUtils.suffixFileFilter("jar"));
        if (files != null && files.length > 0) {
            SoarexBot.LOGGER.info("Attempting to load {} external module(s)", files.length);
            for (File file : files) loadModule(file);
        }
    }

    public static List<IModule> getModules() {
        return modules;
    }

    public static synchronized void loadModule(File file) {
        if (file.isFile() && file.getName().endsWith(".jar")) {
            try {
                List<String> classes =  new ArrayList<>();
                try (JarFile jar = new JarFile(file)) {
                    jar.stream().forEach(jarEntry -> {
                        if (!jarEntry.isDirectory() && jarEntry.getName().endsWith(".class")) {
                            String className = jarEntry.getName().replace('/', '.');
                            classes.add(className.substring(0, className.length()-6));
                        }
                    });
                }
                for (String clazz : classes) {
                    Class classInstance = Class.forName(clazz);
                    if (IModule.class.isAssignableFrom(classInstance)) {
                        initIModule(classInstance);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                SoarexBot.LOGGER.error("Unable to load module " + file.getName() + "!", e);
            }
        }
    }

    public static synchronized void loadModule(Class<? extends IModule> clazz) {
            initIModule(clazz);
    }



    public static void initIModule(Class<? extends IModule> clazz) {
        try{
            IModule module = clazz.newInstance();
            modules.add(module);
            SoarexBot.LOGGER.info("Loading module {} v{} by {}", module.getName(), module.getVersion(), module.getAuthor());
            Thread thread = new Thread(module);
            thread.start();
            modulesClasses.add(clazz);
        } catch (InstantiationException | IllegalAccessException e) {
            SoarexBot.LOGGER.error("Unable to load module : ", e);
        }
    }
}
