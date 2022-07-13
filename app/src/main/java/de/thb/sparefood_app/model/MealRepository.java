package de.thb.sparefood_app.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.thb.sparefood_app.ui.home.Card;


public class MealRepository {
    private static final Logger logger = Logger.getLogger(MealRepository.class.getName());
    private final MutableLiveData<List<Meal>> _meals = new MutableLiveData<>();
    private String authToken;
    private ObjectMapper mapper;

    public LiveData<List<Meal>> meals = _meals;

    public MealRepository() {
        List<Meal> testMeals = new ArrayList<>();
        testMeals.add(new Meal("Nudeln", "mit ner tollen Sauce"));
        testMeals.add(new Meal("Huhn", "mit Reis und Gemüse"));
        testMeals.add(new Meal("Käsespetzle", "mit extra Käse"));
        testMeals.add(new Meal("Noch ein Gericht", "mit leckeren Zutaten"));
        testMeals.add(new Meal("Krabbenburger", "mit Liebe und nicht mit Magie"));
        testMeals.add(new Meal("Curry", "nach Thai Art mit Basmati Reis"));
        this._meals.setValue(testMeals);
        mapper = new ObjectMapper();
    }

    public String getURL(String path, Map<String, String> parameters) {
        URIBuilder builder = new URIBuilder()
                .setScheme("http")
                .setHost("localhost:8080")
                .setPath(path);
        for (Map.Entry<String, String> entry: parameters.entrySet()) {
            builder.addParameter(entry.getKey(), entry.getValue());
        }

        return builder.toString();
    }

    public void requestMeals(Map<String, String> params) throws IOException, JSONException {
        String request = getURL("/meals", params);

        URL mealsURL = new URL(request);
        HttpURLConnection mealsConnection = (HttpURLConnection) mealsURL.openConnection();
        mealsConnection.setRequestProperty("Authorization", "Bearer " + authToken);
        mealsConnection.setRequestMethod("GET");
        mealsConnection.setDoInput(true);
        mealsConnection.setDoOutput(false);

        int responseCode = mealsConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mealsConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            List<Meal> mealLIst = mapper.readValue(sb.toString(), new TypeReference<List<Meal>>() {});
            if (mealLIst != null) {
                _meals.postValue(mealLIst);
            }

        } else {
            throw new IOException("Invalid response from server: " + responseCode);
        }

        mealsConnection.disconnect();
    }
}
