package com.kiruu.kiruusphere;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsData {
    public String key;

    public static String checkAPIKey() {
        String key;
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/kiruu/kiruusphere/apikey/key"))) {
            key = reader.readLine();
            if (key == null || key.equals("NO_API_KEY") || key.isEmpty()) {
                return "NO_API_KEY";
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "There was an error reading the API key.", "File Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
        return key;
    }

    public static String getCountry() {
        String lat, longt, locale;
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/kiruu/kiruusphere/data/pinned.dat"))) {
            lat = reader.readLine();
            longt = reader.readLine();
            locale = reader.readLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "There was an error reading the cached data.", "File Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
        String[] response = Geocode.getJSONResponse(Geocode.getPlace(locale));
        if (response != null) {
            for (String line : response) {
                if (line.contains(lat) && line.contains(longt) && line.contains(locale)) {
                    int countryStart = line.indexOf("\"country\":") + 11;
                    int countryEnd = line.indexOf("\",\"admin1\":");
                    return line.substring(countryStart, countryEnd);
                }
            }
        }
        return "Global";
    }

    public static String getJSONResponse() throws IOException {
        try {
            String country = getCountry(), key = checkAPIKey();
            URL url = new URL("https://newsapi.org/v2/everything?q=" + country + "+weather+news&apiKey=" + key);
            HttpURLConnection obj = (HttpURLConnection) url.openConnection();
            obj.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(obj.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (Exception e) {
            return "NO_RESPONSE";
        }
    }

    public static String[] newsArticles(String response) {
        return response.substring(response.indexOf("\"articles\": [") + 46, response.length() - 2).split("\\},\\{");
    }

}
