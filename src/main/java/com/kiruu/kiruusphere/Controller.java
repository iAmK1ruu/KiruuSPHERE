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

import javax.swing.*;
import java.time.*;
import java.io.*;
import java.util.ArrayList;

//  BAD CODE-WRITING AHEAD!!!!!!!!!!!!!!!!!!!
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
        String savedLocation = RequestWeatherData.readSavedLocation()[2];
        if (savedLocation.equals("NO_SAVED_LOC")) {
            label_noloc.setVisible(true);
            pane_main.setVisible(false);
            pane_side.setVisible(false);
        }
        getWeatherData();
        showNewsArticle();


    }

    Label[] searchnames = {
            searchname, searchname1, searchname2, searchname3,
            searchname4, searchname5, searchname6, searchname7,
            searchname8, searchname9
    };

    Label[] ad1arr = {
            ad1, ad11, ad12, ad13,
            ad14, ad15, ad16, ad17,
            ad18, ad19
    };
    Label[] ad2arr = {
            ad2, ad21, ad22, ad23,
            ad24, ad25, ad26, ad27,
            ad28, ad29
    };
    Label[] ad3arr = {
            ad3, ad31, ad32, ad33,
            ad34, ad35, ad36, ad37,
            ad38, ad39
    };
    // Create Panels with ImageViews and Labels for Future Forecasts
    Pane[] panes = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
    Button[] btns = {b1, b2, b3, b4, b5, b6, b7, b8, b9, b10};

    public void searchAction(ActionEvent e) throws Exception {
        String fetched_text = field_search.getText();
        if (fetched_text.isEmpty() || fetched_text.isBlank()) {
            // INSERT ERROR DIALOG
            return;
        }

        getIndividualSearchResponse(fetched_text);
        label_noloc.setVisible(false);
        pane_main.setVisible(false);
        pane_side.setVisible(false);
        pane_search.setVisible(true);
        pane_search.setDisable(false);
        pane_bottom.setVisible(false);
    }

    public void getIndividualSearchResponse(String fetched_text) {
        Label[] searchnames = {
                searchname, searchname1, searchname2, searchname3,
                searchname4, searchname5, searchname6, searchname7,
                searchname8, searchname9
        };

        Label[] ad1arr = {
                ad1, ad11, ad12, ad13,
                ad14, ad15, ad16, ad17,
                ad18, ad19
        };
        Label[] ad2arr = {
                ad2, ad21, ad22, ad23,
                ad24, ad25, ad26, ad27,
                ad28, ad29
        };
        Label[] ad3arr = {
                ad3, ad31, ad32, ad33,
                ad34, ad35, ad36, ad37,
                ad38, ad39
        };
        Pane[] panes = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10};
        Button[] btns = {b1, b2, b3, b4, b5, b6, b7, b8, b9, b10};
        for (int i = 0; i < 10; i++) {
            panes[i].setVisible(false);
            btns[i].setVisible(false);
            ad1arr[i].setText("NA");
            ad2arr[i].setText("NA");
            ad3arr[i].setText("NA");
        }
        String[] searches = Geocode.getJSONResponse(Geocode.getPlace(fetched_text));
        label_noloc.setVisible(false);
        label_nosearch.setVisible(false);
        label_loading.setVisible(false);
        if (searches[0].contains("name")) {
            for (int i = 0; i < searches.length; i++) {
                String[] splits = searches[i].split(",");
                panes[i].setVisible(true);
                btns[i].setVisible(true);
                btns[i].setDisable(false);
                for (int j = 0; j < splits.length; j++) {
                    if (splits[j].contains("name")) {
                        searchnames[i].setText(splits[j].substring(8, splits[j].length() - 1));
                    }
                    if (splits[j].contains("admin1")) {
                        if (splits[j].contains("}")) {
                            ad1arr[i].setText(splits[j].substring(10, splits[j].length() - 3));
                        } else {
                            ad1arr[i].setText(splits[j].substring(10, splits[j].length() - 1));
                        }
                    }
                    if (splits[j].contains("admin2")) {
                        if (splits[j].contains("}")) {
                            ad2arr[i].setText(splits[j].substring(10, splits[j].length() - 3));
                        } else {
                            ad2arr[i].setText(splits[j].substring(10, splits[j].length() - 1));
                        }
                    }
                    if (splits[j].contains("admin3")) {
                        if (splits[j].contains("}")) {
                            ad3arr[i].setText(splits[j].substring(10, splits[j].length() - 3));
                        } else {
                            ad3arr[i].setText(splits[j].substring(10, splits[j].length() - 1));
                        }
                    }
                }
            }
        } else {
            label_nosearch.setVisible(true);
        }
    }

    public void selectLocation(ActionEvent e) {
        Button source = (Button) e.getSource();
        int start = source.toString().indexOf("id=") + 4, end = source.toString().indexOf(", "),
                index = Integer.parseInt(source.toString().substring(start, end)) - 1;
        String[] response = Geocode.getJSONResponse(Geocode.getPlace(field_search.getText())),
                coords = Geocode.getLatLong(response, index), split = response[index].split(",");
        fetched_search = split[1].substring(8, split[1].length() - 1);

        try {
            BufferedWriter coordswriter = new BufferedWriter(
                    new FileWriter("src/main/resources/com/kiruu/kiruusphere/data/pinned.dat"));
            coordswriter.write(coords[0]);
            coordswriter.newLine();
            coordswriter.write(coords[1]);
            coordswriter.newLine();
            coordswriter.write(fetched_search);
            coordswriter.close();
            getWeatherData();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
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
                        case 21:
                            max_temp = currentLine.substring(21, currentLine.length() - 1).split(",");
                            break;
                        case 22:
                            min_temp = currentLine.substring(21, currentLine.length() - 1).split(",");
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
            ImageView future_icons[] = {fi, fi1, fi2, fi3, fi4, fi5, fi6};
            Month current_month = Month.of(Integer.parseInt(dates.get(0).substring(5, 7)));
            for (int i = 0; i < 7; i++) {
                future_temp[i].setText(min_temp[i] + "-" + max_temp[i] + "C");
                future_icons[i].setImage(new Image("com/kiruu/kiruusphere/1x/" + returnWeatherIcon(1, Integer.parseInt(weather_codes[i]))));
                if (i == 6) {
                    future_forecasts[i].setText(current_month + " " + dates.get(i).substring(8, 10));
                } else {
                    future_forecasts[i].setText(current_month + " " + dates.get(i).substring(dates.get(i).length() - 2));
                }

            }
            img_weather.setImage(new Image("com/kiruu/kiruusphere/1x/" + returnWeatherIcon(isDay, weatherCode)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Weather codes based at https://www.jodc.go.jp/data_format/weather-code.html
    public String returnWeatherIcon(int isDay, int weatherCode) {
        String icon = "";
        String weatherCondition = ""; // New variable for the weather condition

        // Determine the weather icon and condition based on the WMO weather code
        switch (weatherCode) {
            case 0: // Clear sky
                icon = "s"; // Sun
                weatherCondition = "Clear Sky";
                break;
            case 1: // Mainly clear
            case 2: // Partly cloudy
            case 3: // Overcast
                icon = "cs"; // Cloud + Sun
                weatherCondition = "Partly Cloudy/Overcast";
                break;
            case 45: // Fog
            case 48: // Depositing rime fog
                icon = "cw"; // Cloud + Wind
                weatherCondition = "Fog";
                break;
            case 51: // Drizzle: Light
            case 53: // Drizzle: Moderate
            case 55: // Drizzle: Dense
                icon = "cr"; // Cloud + Rain
                weatherCondition = "Drizzle";
                break;
            case 56: // Freezing drizzle: Light
            case 57: // Freezing drizzle: Dense
                icon = "cr"; // Cloud + Rain
                weatherCondition = "Freezing Drizzle";
                break;
            case 61: // Rain: Slight
            case 63: // Rain: Moderate
            case 65: // Rain: Heavy
                icon = "cr"; // Cloud + Rain
                weatherCondition = "Rain";
                break;
            case 66: // Freezing Rain: Light
            case 67: // Freezing Rain: Heavy
                icon = "cr"; // Cloud + Rain
                weatherCondition = "Freezing Rain";
                break;
            case 71: // Snowfall: Slight
            case 73: // Snowfall: Moderate
            case 75: // Snowfall: Heavy
                icon = "crsn"; // Cloud + Snow
                weatherCondition = "Snowfall";
                break;
            case 77: // Snow grains
                icon = "csn"; // Cloud + Snow
                weatherCondition = "Snow Grains";
                break;
            case 80: // Rain showers: Slight
            case 81: // Rain showers: Moderate
            case 82: // Rain showers: Violent
                icon = "crs"; // Cloud + Rain + Sun
                weatherCondition = "Rain Showers";
                break;
            case 85: // Snow showers: Slight
            case 86: // Snow showers: Heavy
                icon = "crsn"; // Cloud + Snow
                weatherCondition = "Snow Showers";
                break;
            case 95: // Thunderstorm: Slight or moderate
                icon = "crl"; // Cloud + Rain + Lightning
                weatherCondition = "Thunderstorm";
                break;
            case 96: // Thunderstorm with slight hail
            case 99: // Thunderstorm with heavy hail
                icon = "crl"; // Cloud + Rain + Lightning
                weatherCondition = "Thunderstorm with Hail";
                break;
            default:
                icon = "unknown"; // Default case if no icon matches
                weatherCondition = "c";
                break;
        }
        label_conditions.setText(weatherCondition);
        return icon + ".png";
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