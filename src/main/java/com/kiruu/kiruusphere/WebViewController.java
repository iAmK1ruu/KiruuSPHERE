package com.kiruu.kiruusphere;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebViewController {
    @FXML
    WebView webview_main;
    @FXML
    public void initialize() {
        WebEngine webengine_main = webview_main.getEngine();
        webengine_main.load("https://google.com");
    }
}
