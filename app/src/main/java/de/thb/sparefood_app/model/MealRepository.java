package de.thb.sparefood_app.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.utils.URIBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import de.thb.sparefood_app.threading.ApplicationExecutors;


public class MealRepository {
    // private static final Logger logger = Logger.getLogger(MealRepository.class.getName());
    private final MutableLiveData<List<Meal>> _meals = new MutableLiveData<>();
    private static String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2xvY2FsaG9zdC9zcGFyZWZvb2QiLCJ1cG4iOiJ0cm9tcGVsbEB0aC1icmFuZGVuYnVyZy5kZSIsImV4cCI6MTY1Nzg2NDYyMywiZ3JvdXBzIjpbIlVzZXIiXSwiYmlydGhkYXRlIjoiMTk5OS0wOC0xMiIsImlhdCI6MTY1NzgwNDYyMywianRpIjoiMDE1ODk0MWYtMDk5OC00NDBmLTk1ZmEtMGE4YmI4NzQ2ZmNhIn0.P7Fdg7YV9hePAmO1L8Z0vFrKv9avAq_541bE9B1O6zMDAeL-HUkFxej6mj8sXtoXDldq8keMlFO650lMPvxtF8nejOkgVwD-vuFJtL6QT7ns0KP5ZTwbr5yefRxraJ6E5cObUx0K2JzfLqkIlhqPoKNTo0TWFOneCrgH2eHe5sRnYNa3q0tM80YdiiA72sz-WxeckF4aAPVtAywRM0iSAHg_tGsKgHHLZ4UaxtcDDtI7Tgfjotdz7Ut7CmBXLMZUrbBDX060-5C-OB6nWKZPMejnrRM6KCENBIXH1oJZpbo3yC1h_dU20728QMQ6dZuIeUPLuyYYOf9XuICIxJTigQ";
    private final ObjectMapper mapper;
    private final ApplicationExecutors executors;

    public LiveData<List<Meal>> meals = _meals;

    public MealRepository() {
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
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }

        return builder.toString();
    }

    public static void setToken(String token) {
        authToken = token;
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

                    List<Meal> mealList = mapper.readValue(sb.toString(), new TypeReference<List<Meal>>() {
                    });

                    for (Meal meal : mealList) {
                        URL url = new URL("http://10.0.2.2:8080/meals/" + meal.getId() + "/image");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestProperty("Authorization", "Bearer " + authToken);
                        con.setRequestMethod("GET");
                        con.setDoInput(true);
                        con.setDoOutput(false);

                        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader imageReader = null;
                            try {
                                Bitmap image = BitmapFactory.decodeStream(new BufferedInputStream(con.getInputStream()));
                                meal.setImage(image);
                            } catch (Exception e) {
                                Log.e("Shit", "failed", e);
                            }
                        }
                    }
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

    public boolean login(Map<String, String> params) throws IOException {
        AtomicBoolean result = new AtomicBoolean(false);

        String request = getURL("/auth/generate-token", params);
        URL loginURL = new URL(request);
        HttpURLConnection loginConnection = (HttpURLConnection) loginURL.openConnection();
        loginConnection.setRequestMethod("POST");

        executors.getBackground().execute(() -> {
            try {
                int responseCode = loginConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(loginConnection.getInputStream()));
                    authToken = reader.readLine();
                    result.set(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            loginConnection.disconnect();
        });
        return result.get();
    }

    public boolean likeMeal(int id) throws IOException {
        AtomicBoolean result = new AtomicBoolean(false);

        String request = getURL("/meals/" + id + "/reserve", null);
        URL likeURL = new URL(request);
        HttpURLConnection likeConnection = (HttpURLConnection) likeURL.openConnection();
        likeConnection.setRequestMethod("POST");
        likeConnection.setRequestProperty("Authorization", "Bearer " + authToken);

        executors.getBackground().execute(() -> {
            try {
                int responseCode = likeConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    result.set(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            likeConnection.disconnect();
        });
        return result.get();
    }

    public boolean releaseMeal(int id) throws IOException {
        AtomicBoolean result = new AtomicBoolean(false);

        String request = getURL("/meals/" + id + "/release", null);
        URL releaseURL = new URL(request);
        HttpURLConnection releaseConnection = (HttpURLConnection) releaseURL.openConnection();
        releaseConnection.setRequestMethod("POST");
        releaseConnection.setRequestProperty("Authorization", "Bearer " + authToken);

        executors.getBackground().execute(() -> {
            try {
                int responseCode = releaseConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    result.set(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            releaseConnection.disconnect();
        });
        return result.get();
    }

    public boolean postMeal(Meal meal) throws IOException {
        String data = mapper.writeValueAsString(meal);
        AtomicBoolean result = new AtomicBoolean(false);

        String request = getURL("/meals", null);
        URL postURL = new URL(request);
        HttpURLConnection postConnection = (HttpURLConnection) postURL.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setRequestProperty("Accept", "application/json");
        postConnection.setDoOutput(true);

        executors.getBackground().execute(() -> {
            try (OutputStream os = postConnection.getOutputStream()) {
                byte[] input = data.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                int responseCode = postConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    result.set(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            postConnection.disconnect();
        });
        return result.get();
    }

    public boolean postImage(int id, Bitmap bitmap) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();

        AtomicBoolean result = new AtomicBoolean(false);

        String request = getURL("/meals/" + id + "/image", null);
        URL uploadURL = new URL(request);
        HttpURLConnection uploadConnection = (HttpURLConnection) uploadURL.openConnection();
        uploadConnection.setRequestMethod("POST");
        uploadConnection.setDoOutput(true);
        uploadConnection.setDoInput(true);
        uploadConnection.setRequestProperty("Content-Type", "multipart/form-data");

        executors.getBackground().execute(() -> {
            try {
                OutputStream os = uploadConnection.getOutputStream();
                os.write(byteArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                int responseCode = uploadConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    result.set(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadConnection.disconnect();
        });

        return result.get();
    }
}
