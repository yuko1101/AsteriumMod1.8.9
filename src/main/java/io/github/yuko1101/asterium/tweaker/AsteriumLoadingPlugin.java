package io.github.yuko1101.asterium.tweaker;

import kotlin.KotlinVersion;
import kotlin.text.StringsKt;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

@IFMLLoadingPlugin.Name("Asterium")
@IFMLLoadingPlugin.SortingIndex(69)
public class AsteriumLoadingPlugin implements IFMLLoadingPlugin {
    private final AsteriumLoadingPluginKt kotlinPlugin;

    public AsteriumLoadingPlugin() throws URISyntaxException {
        if (!checkForClass("kotlin.KotlinVersion") || !checkForClass("gg.essential.api.EssentialAPI")) {
            showMessage(missingDependency);
            exit();
        }
        if (!KotlinVersion.CURRENT.isAtLeast(1, 5, 0)) {
            final File file = new File(KotlinVersion.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File realFile = file;
            for (int i = 0; i < 5; i++) {
                if (realFile == null) {
                    realFile = file;
                    break;
                }
                if (!realFile.getName().endsWith(".jar!") && !realFile.getName().endsWith(".jar")) {
                    realFile = realFile.getParentFile();
                } else break;
            }

            String name = realFile.getName().contains(".jar") ? realFile.getName() : StringsKt.substringAfterLast(StringsKt.substringBeforeLast(file.getAbsolutePath(), ".jar", "unknown"), "/", "Unknown");

            if (name.endsWith("!")) name = name.substring(0, name.length() - 1);

            showMessage(kotlinErrorMessage + "<br>The culprit seems to be " + name + "<br>It bundles version " + KotlinVersion.CURRENT + "</p></html>");
            exit();
        }
        kotlinPlugin = new AsteriumLoadingPluginKt();
    }

    public static void exit() {
        try {
            Class<?> clazz = Class.forName("java.lang.Shutdown");
            Method m_exit = clazz.getDeclaredMethod("exit", int.class);
            m_exit.setAccessible(true);
            m_exit.invoke(null, 0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private boolean checkForClass(String className) {
        try {
            Class.forName(className, false, getClass().getClassLoader());
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private void showMessage(String errorMessage) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // This makes the JOptionPane show on taskbar and stay on top
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Icon icon = null;
        try {
            URL url = new URL("https://cdn.discordapp.com/attachments/630347921400791063/630360330240786452/fairy_soul.png");
            icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(url).getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton githubLink = new JButton("Open GitHub");
        githubLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/yuko1101/AsteriumMod"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        JButton close = new JButton("Close");
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exit();
            }
        });

        Object[] options = new Object[]{githubLink, close};
        JOptionPane.showOptionDialog(
                frame,
                errorMessage,
                "Asterium Error",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                icon,
                options,
                options[0]
        );
        exit();
    }


    public static final String missingDependency =
            "<html><p>" +
                    "Asterium has detected a possible missing dependency<br>" +
                    "The most likely reason is Essential failed to load.<br>" +
                    "Wait a bit, then restart your game.<br>" +
                    "Essential might also not work in your country.<br>" +
                    "Check the Asterium Discord for any announcements, and<br>" +
                    "if there are none, ask for support." +
                    "</p></html>";

    public static final String kotlinErrorMessage =
            "<html><p>" +
                    "Asterium has detected a mod with an older version of Kotlin.<br>" +
                    "The most common culprit is the ChatTriggers mod.<br>" +
                    "If you do have ChatTriggers, you can update to 1.3.2<br>" +
                    "or later to fix the issue. https://www.chattriggers.com/<br>" +
                    "In order to resolve this conflict you must<br>" +
                    "delete the outdated mods.<br>" +
                    "If you have already done this and are still getting this error,<br>" +
                    "or need assistance, ask for support in the Discord.";

    public static final String badMixinVersionMessage =
            "<html><p>" +
                    "Asterium has detected an older version of Mixin.<br>" +
                    "Many of my features require Mixin 0.8 or later!<br>" +
                    "In order to resolve this conflict you must remove<br>" +
                    "any mods with a Mixin version below 0.8.<br>" +
                    "You can also try to rename Asterium to be above other mods alphabetically<br>" +
                    "by changing Asterium.jar to !Asterium.jar<br>" +
                    "If you have already done this and are still getting this error,<br>" +
                    "ask for support in the Discord.";

    public static final String liteloaderUserMessage =
            "<html><p>" +
                    "Asterium has detected that you are using LiteLoader.<br>" +
                    "LiteLoader bundles an older, incompatible version of Mixin.<br>" +
                    "In order to resolve this conflict you must launch<br>" +
                    "Minecraft without LiteLoader.<br>" +
                    "If you have already done this and are still getting this error,<br>" +
                    "ask for support in the Discord." +
                    "</p></html>";


    @Override
    public String[] getASMTransformerClass() {
        return kotlinPlugin.getASMTransformerClass();
    }

    @Override
    public String getModContainerClass() {
        return kotlinPlugin.getModContainerClass();
    }

    @Override
    public String getSetupClass() {
        return kotlinPlugin.getSetupClass();
    }

    @Override
    public void injectData(Map<String, Object> data) {
        kotlinPlugin.injectData(data);
    }

    @Override
    public String getAccessTransformerClass() {
        return kotlinPlugin.getAccessTransformerClass();
    }
}
