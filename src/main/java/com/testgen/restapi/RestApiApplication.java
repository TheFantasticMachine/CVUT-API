package com.testgen.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;

@SpringBootApplication
public class RestApiApplication {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        // Enforce non-headless mode before Spring Boot initializes
        System.setProperty("java.awt.headless", "false");

        context = new SpringApplicationBuilder(RestApiApplication.class)
                .headless(false)
                .run(args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void onApplicationReady() {
//        // Open the default browser
//        openBrowser("http://localhost:8080/");
//
//        // Setup the System Tray on the AWT Event Dispatch Thread
//        SwingUtilities.invokeLater(this::createTrayIcon);
//    }
//
//    private void openBrowser(String url) {
//        try {
//            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
//                Desktop.getDesktop().browse(new URI(url));
//            } else {
//                new ProcessBuilder("cmd", "/c", "start", url).start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void createTrayIcon() {
//        if (!SystemTray.isSupported()) {
//            System.err.println("SystemTray is not supported on this platform.");
//            return;
//        }
//
//        SystemTray tray = SystemTray.getSystemTray();
//
//        // Generate a 16x16 blue circle icon
//        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g = image.createGraphics();
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g.setColor(new Color(0, 101, 189));
//        g.fillOval(1, 1, 14, 14);
//        g.dispose();
//
//        PopupMenu popup = new PopupMenu();
//
//        MenuItem openItem = new MenuItem("Open in Browser");
//        openItem.addActionListener(e -> openBrowser("http://localhost:8080/"));
//        popup.add(openItem);
//
//        popup.addSeparator();
//
//        MenuItem exitItem = new MenuItem("Exit");
//        exitItem.addActionListener(e -> {
//            try {
//                if (context != null) {
//                    SpringApplication.exit(context, () -> 0);
//                }
//            } finally {
//                System.exit(0);
//            }
//        });
//        popup.add(exitItem);
//
//        TrayIcon trayIcon = new TrayIcon(image, "CVUT TestGen", popup);
//        trayIcon.setImageAutoSize(true);
//
//        try {
//            tray.add(trayIcon);
//        } catch (AWTException e) {
//            System.err.println("Unable to add tray icon: " + e.getMessage());
//        }
//    }
}