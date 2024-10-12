package com.kiruu.kiruusphere;

import com.sun.net.httpserver.Request;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import jdk.jshell.spi.ExecutionControlProvider;
import java.time.*;
import java.io.*;
import java.util.ArrayList;

//  BAD CODE-WRITING AHEAD!!!!!!!!!!!!!!!!!!!
public class Controller {
    public String fetched_search;
    @FXML
    Button btn_search;
    @FXML
    Pane pane_main, pane_side, pane_search,
            p1, p2, p3, p4, p5, p6, p7, p8, p9, p10;
    @FXML
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;
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
            label_humidity, fd1, fd2, fd3, fd4, fd5, fd6, fd7;

    @FXML
    ImageView fi1, fi2, fi3, fi4, fi5, fi6, fi7;

    @FXML
    public void initialize() throws IOException {
        String savedLocation = RequestWeatherData.readSavedLocation()[2];
        if (savedLocation.equals("NO_SAVED_LOC")) {
            label_noloc.setVisible(true);
            pane_main.setVisible(false);
            pane_side.setVisible(false);
        }
        getWeatherData();
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
    Pane[] panes = { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10 };
    Button[] btns = { b1, b2, b3, b4, b5, b6, b7, b8, b9, b10 };
    Label[] future_forecasts = { fd1, fd2, fd3, fd4, fd5, fd6, fd7 };
    ImageView future_forecast_icon[] = { fi1, fi2, fi3, fi4, fi5, fi6, fi7 };

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
        Pane[] panes = { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10 };
        Button[] btns = { b1, b2, b3, b4, b5, b6, b7, b8, b9, b10 };
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
        pane_search.setVisible(false);
        pane_main.setVisible(true);
        pane_side.setVisible(true);
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
                        default: // Daily dates
                            if (i >= 8 && i < 20) {
                                dates.add(currentLine);
                            }
                            break;
                    }
                }
            }
            Label[] future_forecasts = { fd1, fd2, fd3, fd4, fd5, fd6, fd7 };
            // RECHECK LATER
            //Month current_month = Month.of(Integer.parseInt(dates.get(0).substring(6, 2)));
            for (int i = 0; i < 7; i++) {
                //future_forecasts[i].setText(current_month + " " + dates.get(i).substring(dates.get(i).length() - 2));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Weather codes based at https://www.jodc.go.jp/data_format/weather-code.html
    public String returnWeatherIcon(int isDay, int weatherCode) {
        String icon = "";

        // Determine the weather icon based on the weather code
        switch (weatherCode) {
            case 0: // Clear
                icon = "s"; // Sun
                break;
            case 1: // Partly Cloudy
                icon = "cs"; // Cloud + Sun
                break;
            case 2: // Cloudy
                icon = "c"; // Clouds
                break;
            case 3: // Overcast or Light Rain
                icon = "cr"; // Cloud + Rain
                break;
            case 4: // Thunderstorms or Heavy Rain
                icon = "crl"; // Cloud + Rain + Lightning
                break;
            case 5: // Showers
                icon = "crs"; // Cloud + Rain + Sun
                break;
            case 6: // Snow
                icon = "crsn"; // Cloud + Rain + Snow
                break;
            case 7: // Mist or Fog
                icon = "cw"; // Cloud + Wind
                break;
            case 8: // Windy
                icon = (isDay == 1) ? "sw" : "m"; // Sun + Wind or Moon if nighttime
                break;
            case 9: // Thunderstorm (day)
                icon = "cl"; // Clouds + Lightning
                break;
            case 10: // Moonlit
                icon = "m"; // Moon
                break;
            case 11: // Moonlit with Wind
                icon = "mw"; // Moon + Wind
                break;
            case 12: // Partly Cloudy Night
                icon = "cms"; // Cloud + Moon + Stars
                break;
            case 13: // Snow at Night
                icon = "csn"; // Cloud + Snow
                break;
            case 14: // Snowy Weather
                icon = "csl"; // Cloud + Snow + Lightning (if applicable)
                break;
            case 15: // Hail
                icon = "cr"; // Assuming hail can be represented with rain clouds
                break;
            case 16: // Sleet
                icon = "crs"; // Cloud + Rain + Snow
                break;
            default:
                icon = "unknown"; // Default case if no icon matches
                break;
        }

        // Append .png to the icon string to return the filename
        return icon + ".png";
    }


}