package de.thb.sparefood_app.model;

import android.util.Log;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.thb.sparefood_app.threading.ApplicationExecutors;
import de.thb.sparefood_app.ui.home.Card;
import kotlin.coroutines.Continuation;


public class MealRepository {
    // private static final Logger logger = Logger.getLogger(MealRepository.class.getName());
    private final MutableLiveData<List<Meal>> _meals = new MutableLiveData<>();
    private String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdC9zcGFyZWZvb2QiLCJ1cG4iOiJ0cm9tcGVsbEB0aC1icmFuZGVuYnVyZy5kZSIsImV4cCI6MTY1Nzg2NDYyMywiZ3JvdXBzIjpbIlVzZXIiXSwiYmlydGhkYXRlIjoiMTk5OS0wOC0xMiIsImlhdCI6MTY1NzgwNDYyMywianRpIjoiMDE1ODk0MWYtMDk5OC00NDBmLTk1ZmEtMGE4YmI4NzQ2ZmNhIn0.P7Fdg7YV9hePAmO1L8Z0vFrKv9avAq_541bE9B1O6zMDAeL-HUkFxej6mj8sXtoXDldq8keMlFO650lMPvxtF8nejOkgVwD-vuFJtL6QT7ns0KP5ZTwbr5yefRxraJ6E5cObUx0K2JzfLqkIlhqPoKNTo0TWFOneCrgH2eHe5sRnYNa3q0tM80YdiiA72sz-WxeckF4aAPVtAywRM0iSAHg_tGsKgHHLZ4UaxtcDDtI7Tgfjotdz7Ut7CmBXLMZUrbBDX060-5C-OB6nWKZPMejnrRM6KCENBIXH1oJZpbo3yC1h_dU20728QMQ6dZuIeUPLuyYYOf9XuICIxJTigQ";
    private ObjectMapper mapper;
    private ApplicationExecutors executors;

    public LiveData<List<Meal>> meals = _meals;

    public MealRepository() {
//        List<Meal> testMeals = new ArrayList<>();
//        testMeals.add(new Meal("Nudeln", "mit ner tollen Sauce"));
//        testMeals.add(new Meal("Huhn", "mit Reis und Gemüse"));
//        testMeals.add(new Meal("Käsespetzle", "mit extra Käse"));
//        testMeals.add(new Meal("Noch ein Gericht", "mit leckeren Zutaten"));
//        testMeals.add(new Meal("Krabbenburger", "mit Liebe und nicht mit Magie"));
//        testMeals.add(new Meal("Curry", "nach Thai Art mit Basmati Reis"));
//        this._meals.setValue(testMeals);
        mapper = new ObjectMapper();
        executors = new ApplicationExecutors();
        HashMap<String, String> params = new HashMap<>();
        params.put("filter.radius", "5");
        params.put("longitude", "52.5");
        params.put("latitude", "13.4");
        try {
            requestMeals(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getURL(String path, Map<String, String> parameters) {
        URIBuilder builder = new URIBuilder()
                .setScheme("http")
                .setHost("10.0.2.2:8080")
                .setPath(path);
        if (parameters != null) {
            for (Map.Entry<String, String> entry: parameters.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }

        return builder.toString();
    }

    public void requestMeals(Map<String, String> params) throws IOException {
        String request = getURL("/meals", params);

        URL mealsURL = new URL(request);
        HttpURLConnection mealsConnection = (HttpURLConnection) mealsURL.openConnection();
        mealsConnection.setRequestProperty("Authorization", "Bearer " + authToken);
        mealsConnection.setRequestMethod("GET");
        mealsConnection.setDoInput(true);
        mealsConnection.setDoOutput(false);

        executors.getBackground().execute(() -> {
            int responseCode = 0;
            try {
                responseCode = mealsConnection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(mealsConnection.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    List<Meal> mealList = mapper.readValue(sb.toString(), new TypeReference<List<Meal>>() {});
                    _meals.postValue(mealList);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("HTTP", "Request failed with status code: " + responseCode);
            }

            mealsConnection.disconnect();
        });

    }
}
