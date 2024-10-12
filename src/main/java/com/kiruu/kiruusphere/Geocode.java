
package com.kiruu.kiruusphere;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
public class Geocode {
    public static String[] getJSONResponse(String fetched_search) {
        try {
            String provider = "https://geocoding-api.open-meteo.com/v1/search?name=" + fetched_search + "&count=10&language=en&format=json";
            URL url = new URL(provider);
            HttpsURLConnection obj = (HttpsURLConnection)url.openConnection();
            obj.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(obj.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return returnStringArrays(response.toString());
        } catch (Exception e) {
            System.out.println("There was an error requesting the weather data.");
        }
        String[] nores = {"NO_RESPONSE"};
        return nores;
    }
    public static String[] returnStringArrays(String JSONResponse) {
        ArrayList<String> responses = new ArrayList<>();
        JSONResponse = JSONResponse.substring(13, JSONResponse.length() - 1);
        String[] jsonObjects = JSONResponse.split("\\},\\{");
        return jsonObjects;
    }

    public static String getPlace(String input) {
        String[] words = input.split(" ");
        String fetchedInput = "";
        for (int i = 0; i < words.length; i++) {
            fetchedInput += words[i];
            if (i < words.length - 1)
                fetchedInput += "+";
        }
        return fetchedInput;
    }

    public static String[] getLatLong(String[] JSONResponse, int index) {
        try {
            String[] latLong = new String[2];
            latLong[0] = JSONResponse[index].substring(JSONResponse[index].indexOf("\"latitude\"") + 11, JSONResponse[index].indexOf("\"longitude\"") - 1);
            latLong[1] = JSONResponse[index].substring(JSONResponse[index].indexOf("\"longitude\"") + 12, JSONResponse[index].indexOf("\"elevation\"") - 1);
            return latLong;
        } catch (Exception e) {
            return new String[]{"NO_RESPONSE"};
        }
    }
    /*
     * USAGE:
     * String[] coords = getLatLong(getJSONResponse(getPlace("Baguio City")), <index>);
     * After the user selects the correct place, it will be passed as a 2nd parameter to getLatLong()
     */
}
