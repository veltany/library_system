module com.miva {
    
    requires java.desktop;   // Enables standard Java AWT and Swing frameworks
    requires com.google.gson; // Enables deep reflection JSON file databases

    
    opens com.miva.model to com.google.gson;
    opens com.miva.database to com.google.gson;
    

    exports com.miva;
    exports com.miva.gui;
}
