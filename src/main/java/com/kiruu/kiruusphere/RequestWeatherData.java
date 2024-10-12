package com.kiruu.kiruusphere;
import java.io.*;
import java.net.URL;
import javafx.scene.control.*;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;

/*
 * API URL Request (Sample Coordinates for Lingayen, Pangasinan
 * https://api.open-meteo.com/v1/forecast?latitude=16.02182&longitude=16.0218212023194&current=temperature_2m,relative_humidity_2m,is_day,rain,weather_code,wind_speed_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max&timezone=Asia%2FTokyo
 * Possible Prediction UX Design:
 * https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSwCfW6HONsxPc4npvMoHyg9vhvBpaURkKWSVob4Dl5wcS76cPc9Umn9vuM-raPHqeN6c&usqp=CAU
 * API Parameters
 * Daily Weather Variables: Weather code, Min-Max Temperature, (OPTIONAL) Precipitation Probability Max
 * Current Weather Variables: Temperature, Relative Humidity, Is Day/Night, Rain, Weather Code, Wind Speed
 *
 */
public class RequestWeatherData {
    public static String[] getResponse(String urlParam) {
        //  Recheck if there are any uses for this function to be created. If not, remove this and use getJSONResponse() instead.
        String[] noResponse = {"NO_RESPONSE"};
        return noResponse;
    }
    public static String[] readSavedLocation() {
        try {
            String[] location = new String[3];
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/kiruu/kiruusphere/data/pinned.dat"));
            location[0] = reader.readLine();
            location[1] = reader.readLine();
            location[2] = reader.readLine();
            System.out.println(location[2]);
            return location;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getJSONResponse() {
        try {
            String[] location = readSavedLocation();
            String defaultAPIUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + location[0] + "&longitude=" + location[1] + "&current=temperature_2m,relative_humidity_2m,is_day,rain,weather_code,wind_speed_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max";
            URL url = new URL(defaultAPIUrl);
            HttpsURLConnection obj = (HttpsURLConnection)url.openConnection();
            obj.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(obj.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();

        } catch (Exception e) {
            return "NO_RESPONSE";
        }
    }
    public static void splitResponse() {
        String response = getJSONResponse();
        String[] split_response = new String[2], daily, current;
        System.out.println(response);
        if (response.equals("NO_RESPONSE")) {
            split_response[0] = "NO_RESPONSE";
            split_response[1] = "NO_RESPONSE";
            JOptionPane.showMessageDialog(null, "Weather data retrieval failed. Please check your internet connection.\nLast cached data will be loaded.", "No API Response", 0);
        } else {
            split_response[0] = response.substring(response.indexOf("\"current\":{\"time\"") + 11, response.indexOf(",\"daily\":{") - 1);
            split_response[1] = response.substring(response.indexOf(",\"daily\":{") + 19, response.length() - 2);
            daily = split_response[0].split(",");
            current = split_response[1].split(",\"");
            updateWriter(daily, current);
        }
    }
    public static void updateWriter(String[] daily, String[] current) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/com/kiruu/kiruusphere/data/cache.dat"))) {
            for (int i = 0; i < daily.length; i++) {
                    if (i == 7) {
                        writer.write(daily[i].substring(daily[i].indexOf("\":") + 2, daily[i].length() - 1));
                        writer.newLine();
                    } else if (i == 8) {
                        writer.write(daily[i].substring(daily[i].indexOf("\":") + 2, daily[i].length() - 1));
                        writer.newLine();
                    } else {
                        writer.write(daily[i].substring(daily[i].indexOf("\":") + 2));
                        writer.newLine();
                    }
            }
            for (int j = 0; j < current.length; j++) {
                    if (j < 6) {
                        writer.write(current[j].substring(0, current[j].length() - 1));
                        writer.newLine();
                    } else {
                        writer.write(current[j]);
                        writer.newLine();
                    }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
