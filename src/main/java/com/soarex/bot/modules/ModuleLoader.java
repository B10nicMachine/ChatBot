package com.soarex.bot.modules;

import com.soarex.bot.SoarexBot;
import com.soarex.bot.api.IModule;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarFile;

/**
 * Created by shumaf on 03.05.16.
 */
public class ModuleLoader {

    public static final String MODULE_DIR = "modules";
    protected static final List<Class<? extends IModule>> modules = new CopyOnWriteArrayList<>();

    private List<IModule> loadedModules = new CopyOnWriteArrayList<>();

    static {
        File modulesDir = new File(MODULE_DIR);
        if (modulesDir.exists()) {
            if (!modulesDir.isDirectory()) {
                throw new RuntimeException(MODULE_DIR+" isn't a directory!");
            }
        } else {
            if (!modulesDir.mkdir()) {
                throw new RuntimeException("Error creating "+MODULE_DIR+" directory");
            }
        }

        File [] files = modulesDir.listFiles((FilenameFilter) FileFilterUtils.suffixFileFilter("jar"));
        if(files != null && files.length > 0) {
            SoarexBot.LOGGER.info("Attempting to load {} external module(s)", files.length);
            new ArrayList<>(Arrays.asList(files)).forEach(ModuleLoader::loadExternalModules);
        }
    }

    public void unloadModule(IModule module) {
        loadedModules.remove(module);
        module.disable();
    }

    public static synchronized void loadExternalModules(File file) {
        if (file.isFile() && file.getName().endsWith(".jar")) {
            try {
                URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                URL url = file.toURI().toURL();
                for (URL it : Arrays.asList(loader.getURLs())) {
                    if (it.equals(url)) {
                        return;
                    }
                }
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
                method.setAccessible(true);
                method.invoke(loader, new Object[]{url});

                List<String> classes = new ArrayList<>();
                try (JarFile jar = new JarFile(file)) {
                    jar.stream().forEach(jarEntry -> {
                        if (!jarEntry.isDirectory() && jarEntry.getName().endsWith(".class")) {
                            String className = jarEntry.getName().replace('/', '.');
                            classes.add(className.substring(0, className.length()-".class".length()));
                        }
                    });
                }
                for (String clazz : classes) {
                    Class classInstance = Class.forName(clazz);
                    if (IModule.class.isAssignableFrom(classInstance)) {
                        addModuleClass(classInstance);
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException | ClassNotFoundException e) {
                SoarexBot.LOGGER.error("Unable to load module "+file.getName()+"!", e);
            }
        }
    }

    public static void addModuleClass(Class<? extends IModule> clazz) {
        modules.add(clazz);
    }
}
