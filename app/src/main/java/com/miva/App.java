package com.miva;

import com.miva.gui.MainWindow;
import javax.swing.SwingUtilities;

/**
 * Swing Application Entry Point
 * Implements MIVA Open University layout requirements
 */
public class App {

    public static void main(String[] args) {
        // Enforce safe multi-threaded UI boot execution loop sequence on the EDT
        SwingUtilities.invokeLater(() -> {
            try {
                // Set native operating system Look and Feel for cleaner UI edges on Windows
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                System.err.println("Notice: Native OS Look & Feel fallback decoration failed.");
            }
            
            // Spin up your main container frame window layout system
            new MainWindow();
        });
    }
}
