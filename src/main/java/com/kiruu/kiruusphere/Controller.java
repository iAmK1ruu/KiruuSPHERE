package com.kiruu.kiruusphere;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.time.*;
import java.io.*;
import java.util.ArrayList;
public class Controller {
    public String fetched_search;
    public int articleLimit = NewsData.newsArticles(NewsData.getJSONResponse()).length - 1;
    public int articleCount = 0;
    @FXML
    Button btn_search, btn_closearticle, btn_about;
    @FXML
    Pane pane_main, pane_side, pane_search,
            p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, pane_bottom;
    @FXML
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, redirect_btn;
    @FXML
    TextField field_search;
    @FXML
    Label searchname, ad1, ad2, ad3,
            searchname1, ad11, ad21, ad31,
            searchname2, ad12, ad22, ad32,
            searchname3, ad13, ad23, ad33,
            searchname4, ad14, ad24, ad34,
            searchname5, ad15, ad25, ad35,
            searchname6, ad16, ad26, ad36,
            searchname7, ad17, ad27, ad37,
            searchname8, ad18, ad28, ad38,
            searchname9, ad19, ad29, ad39;
    @FXML
    Label label_nosearch, label_noloc, label_loading, label_temp, label_conditions, label_loc, label_wind, label_chance,
            label_humidity, fd1, fd2, fd3, fd4, fd5, fd6, fd,
            ft, ft1, ft2, ft3, ft4, ft5, ft6, news_label, news_provider_label, news_desc;
    @FXML
    WebView webview;
    @FXML
    ImageView fi, fi1, fi2, fi3, fi4, fi5, fi6;
    @FXML
    ImageView img_weather;

    public Controller() throws IOException {
    }

    @FXML
    public void initialize() throws IOException {
        if (isNoSavedLocation()) {
            handleNoSavedLocation();
        } else {
            getWeatherData();
            showNewsArticle();
        }
    }

    private boolean isNoSavedLocation() throws IOException {
        String savedLocation = RequestWeatherData.readSavedLocation()[2];
        return "NO_SAVED_LOC".equals(savedLocation);
    }

    private void handleNoSavedLocation() {
        label_noloc.setVisible(true);
        pane_main.setVisible(false);
        pane_side.setVisible(false);
    }



    public void searchAction(ActionEvent e) throws Exception {
        String fetchedText = field_search.getText();

        if (isSearchFieldEmpty(fetchedText)) {
            showErrorDialog("Search field cannot be empty.");
            return;
        }

        handleSearchAction(fetchedText);
    }

    private boolean isSearchFieldEmpty(String text) {
        return text == null || text.isBlank();
    }

    private void showErrorDialog(String message) {
        // Replace with actual JavaFX dialog code
        System.out.println("Error: " + message); // Placeholder for error dialog
    }

    private void handleSearchAction(String fetchedText) throws Exception {
        getIndividualSearchResponse(fetchedText);

        label_noloc.setVisible(false);
        pane_main.setVisible(false);
        pane_side.setVisible(false);
        pane_search.setVisible(true);
        pane_search.setDisable(false);
        pane_bottom.setVisible(false);
    }


    public void getIndividualSearchResponse(String fetchedText) {
        resetSearchUI();

        String[] searches = Geocode.getJSONResponse(Geocode.getPlace(fetchedText));
        label_noloc.setVisible(false);
        label_nosearch.setVisible(false);
        label_loading.setVisible(false);

        if (searches[0].contains("name")) {
            for (int i = 0; i < searches.length; i++) {
                updatePaneWithSearchData(i, searches[i]);
            }
        } else {
            label_nosearch.setVisible(true);
        }
    }

    private void resetSearchUI() {
        Label[] searchNames = { searchname, searchname1, searchname2, searchname3, searchname4, searchname5, searchname6, searchname7, searchname8, searchname9 };
        Label[] ad1Arr = { ad1, ad11, ad12, ad13, ad14, ad15, ad16, ad17, ad18, ad19 };
        Label[] ad2Arr = { ad2, ad21, ad22, ad23, ad24, ad25, ad26, ad27, ad28, ad29 };
        Label[] ad3Arr = { ad3, ad31, ad32, ad33, ad34, ad35, ad36, ad37, ad38, ad39 };
        Pane[] panes = { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10 };
        Button[] buttons = { b1, b2, b3, b4, b5, b6, b7, b8, b9, b10 };

        for (int i = 0; i < panes.length; i++) {
            panes[i].setVisible(false);
            buttons[i].setVisible(false);
            ad1Arr[i].setText("NA");
            ad2Arr[i].setText("NA");
            ad3Arr[i].setText("NA");
        }
    }

    private void updatePaneWithSearchData(int index, String searchData) {
        Pane[] panes = { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10 };
        Button[] buttons = { b1, b2, b3, b4, b5, b6, b7, b8, b9, b10 };
        Label[] searchNames = { searchname, searchname1, searchname2, searchname3, searchname4, searchname5, searchname6, searchname7, searchname8, searchname9 };
        Label[] ad1Arr = { ad1, ad11, ad12, ad13, ad14, ad15, ad16, ad17, ad18, ad19 };
        Label[] ad2Arr = { ad2, ad21, ad22, ad23, ad24, ad25, ad26, ad27, ad28, ad29 };
        Label[] ad3Arr = { ad3, ad31, ad32, ad33, ad34, ad35, ad36, ad37, ad38, ad39 };

        panes[index].setVisible(true);
        buttons[index].setVisible(true);
        buttons[index].setDisable(false);

        String[] splits = searchData.split(",");
        for (String split : splits) {
            if (split.contains("name")) {
                searchNames[index].setText(getFormattedText(split, 8));
            } else if (split.contains("admin1")) {
                ad1Arr[index].setText(getFormattedText(split, 10));
            } else if (split.contains("admin2")) {
                ad2Arr[index].setText(getFormattedText(split, 10));
            } else if (split.contains("admin3")) {
                ad3Arr[index].setText(getFormattedText(split, 10));
            }
        }
    }

    private String getFormattedText(String data, int startIndex) {
        int endIndex = data.endsWith("}") ? data.length() - 3 : data.length() - 1;
        return data.substring(startIndex, endIndex);
    }


    public void selectLocation(ActionEvent e) {
        int index = extractIndexFromButton((Button) e.getSource());
        String[] response = Geocode.getJSONResponse(Geocode.getPlace(field_search.getText()));
        String[] coords = Geocode.getLatLong(response, index);
        String fetchedSearch = extractFetchedSearch(response, index);

        writePinnedData(coords, fetchedSearch);
        getWeatherData();
    }

    private int extractIndexFromButton(Button button) {
        String buttonText = button.toString();
        int start = buttonText.indexOf("id=") + 4;
        int end = buttonText.indexOf(", ");
        return Integer.parseInt(buttonText.substring(start, end)) - 1;
    }

    private String extractFetchedSearch(String[] response, int index) {
        String[] split = response[index].split(",");
        return split[1].substring(8, split[1].length() - 1);
    }

    private void writePinnedData(String[] coords, String fetchedSearch) {
        try (BufferedWriter coordsWriter = new BufferedWriter(new FileWriter("src/main/resources/com/kiruu/kiruusphere/data/pinned.dat"))) {
            coordsWriter.write(coords[0]);
            coordsWriter.newLine();
            coordsWriter.write(coords[1]);
            coordsWriter.newLine();
            coordsWriter.write(fetchedSearch);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write pinned data", ex);
        }
    }




    public void getWeatherData() {
        String[] min_temp = new String[7], max_temp = new String[7], weather_codes = new String[7];
        pane_search.setVisible(false);
        pane_main.setVisible(true);
        pane_side.setVisible(true);
        pane_bottom.setVisible(true);
        RequestWeatherData.splitResponse();
        label_loc.setText(RequestWeatherData.readSavedLocation()[2]);

        try (BufferedReader reader = new BufferedReader(
                new FileReader("src/main/resources/com/kiruu/kiruusphere/data/cache.dat"))) {
            String currentLine;
            ArrayList<String> dates = new ArrayList<>();
            int isDay = -1, weatherCode = -1;

            for (int i = 0; i < 24; i++) {
                currentLine = reader.readLine();
                if (currentLine != null) {
                    switch (i) {
                        case 2: // Temperature
                            label_temp.setText(currentLine);
                            break;
                        case 3:
                            label_humidity.setText(currentLine);
                            break;
                        case 4: // Is Day
                            isDay = Integer.parseInt(currentLine);
                            break;
                        case 6: // Weather Code
                            weatherCode = Integer.parseInt(currentLine);
                            break;
                        case 7: // Wind Speed
                            label_wind.setText(currentLine);
                            break;
                        case 20:
                            weather_codes = currentLine.substring(15, currentLine.length() - 1).split(",");
                            break;
                        case 21:
                            max_temp = currentLine.substring(21, currentLine.length() - 1).split(",");
                            break;
                        case 22:
                            min_temp = currentLine.substring(21, currentLine.length() - 1).split(",");
                            break;
                        default: // Daily dates
                            if (i >= 13 && i < 20) {
                                dates.add(currentLine);
                            }
                            break;
                    }
                }
            }

            Label[] future_forecasts = {fd, fd1, fd2, fd3, fd4, fd5, fd6};
            Label[] future_temp = {ft, ft1, ft2, ft3, ft4, ft5, ft6};
            ImageView[] future_icons = {fi, fi1, fi2, fi3, fi4, fi5, fi6};

            // Parse the initial date from the data
            LocalDate initialDate = LocalDate.parse(dates.get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Month currentMonth = initialDate.getMonth();

            for (int i = 0; i < 7; i++) {
                LocalDate forecastDate = initialDate.plusDays(i);

                // Check for month transition
                if (forecastDate.getMonth() != currentMonth) {
                    currentMonth = forecastDate.getMonth();
                }

                // Set the forecast data
                future_temp[i].setText(min_temp[i] + "-" + max_temp[i] + "C");
                future_icons[i].setImage(new Image("com/kiruu/kiruusphere/1x/" + returnWeatherIcon(1, Integer.parseInt(weather_codes[i]))));
                future_forecasts[i].setText(forecastDate.getMonth() + " " + forecastDate.getDayOfMonth());
            }

            img_weather.setImage(new Image("com/kiruu/kiruusphere/1x/" + returnWeatherIcon(isDay, weatherCode)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public String returnWeatherIcon(int isDay, int weatherCode) {
        WeatherInfo weatherInfo = getWeatherInfo(weatherCode);

        label_conditions.setText(weatherInfo.condition);
        return weatherInfo.icon + ".png";
    }

    private WeatherInfo getWeatherInfo(int weatherCode) {
        switch (weatherCode) {
            case 0:
                return new WeatherInfo("s", "Clear Sky");
            case 1:
            case 2:
            case 3:
                return new WeatherInfo("cs", "Partly Cloudy/Overcast");
            case 45:
            case 48:
                return new WeatherInfo("cw", "Fog");
            case 51:
            case 53:
            case 55:
                return new WeatherInfo("cr", "Drizzle");
            case 56:
            case 57:
                return new WeatherInfo("cr", "Freezing Drizzle");
            case 61:
            case 63:
            case 65:
                return new WeatherInfo("cr", "Rain");
            case 66:
            case 67:
                return new WeatherInfo("cr", "Freezing Rain");
            case 71:
            case 73:
            case 75:
                return new WeatherInfo("crsn", "Snowfall");
            case 77:
                return new WeatherInfo("csn", "Snow Grains");
            case 80:
            case 81:
            case 82:
                return new WeatherInfo("crs", "Rain Showers");
            case 85:
            case 86:
                return new WeatherInfo("crsn", "Snow Showers");
            case 95:
                return new WeatherInfo("crl", "Thunderstorm");
            case 96:
            case 99:
                return new WeatherInfo("crl", "Thunderstorm with Hail");
            default:
                return new WeatherInfo("unknown", "Unknown Weather");
        }
    }

    private static class WeatherInfo {
        String icon;
        String condition;

        WeatherInfo(String icon, String condition) {
            this.icon = icon;
            this.condition = condition;
        }
    }

    public void aboutMe(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "About KiruuSPHERE\n" +
                "KiruuSphere is a simple and intuitive weather application built using JavaFX.\n" +
                "It leverages the Open-Meteo Weather API to provide real-time weather updates and conditions for any location around the globe." +
                "\nBy: github.com/iAmK1ruu");
    }

    public void showNewsArticle() throws IOException {
        String[] articles = NewsData.newsArticles(NewsData.getJSONResponse());
        String articleJson = articles[articleCount];
        String name = articleJson.substring(
                articleJson.indexOf("\"name\":\"") + 8,
                articleJson.indexOf("\"},\"author\"")
        );
        if (name.equals("[Removed]")) {
            articleLimit--;
            System.out.println("CONDITION PASSED");
        } else {
            String title = articleJson.substring(
                    articleJson.indexOf("\"title\":\"") + 9,
                    articleJson.indexOf("\",\"description\"")
            );
            news_label.setText(title);
            news_provider_label.setText(name);
            String content = articleJson.substring(
                    articleJson.indexOf("\"content\":\"") + 11,
                    articleJson.indexOf("… [") + 2
            ).replace("\\r", ".").replace("\\n", " ").replace("\"", " ");
            news_desc.setText(content);
        }
    }

    public void backBtn(ActionEvent e) throws IOException {
        if (articleCount > 0) {
            articleCount--;
        }
        showNewsArticle();
    }

    public void nextBtn(ActionEvent e) throws IOException {
        if (articleCount == articleLimit) {
            showNewsArticle();
        } else {
            articleCount++;
            showNewsArticle();
        }
    }

    public void redirect() throws IOException {
        pane_main.setVisible(false);
        pane_side.setVisible(false);
        pane_bottom.setVisible(false);
        btn_closearticle.setVisible(true);
        webview.setVisible(true);
        field_search.setVisible(false);
        btn_search.setVisible(false);
        btn_about.setVisible(false);
        String[] articles = NewsData.newsArticles(NewsData.getJSONResponse());
        WebEngine webengine_main = webview.getEngine();
        webengine_main.load(articles[articleCount].substring(articles[articleCount].indexOf("\"url\":\"") + 7, articles[articleCount].indexOf("\",\"urlToImage\"")));
    }

    public void closeArticle(ActionEvent e) {
        pane_main.setVisible(true);
        pane_side.setVisible(true);
        pane_bottom.setVisible(true);
        btn_closearticle.setVisible(false);
        webview.setVisible(false);
        field_search.setVisible(true);
        btn_search.setVisible(true);
        btn_about.setVisible(true);
    }


}
