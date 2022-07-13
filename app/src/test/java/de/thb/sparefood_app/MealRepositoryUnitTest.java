package de.thb.sparefood_app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import de.thb.sparefood_app.model.MealRepository;

public class MealRepositoryUnitTest {
    @Test
    public void URLisCorrect() {
        MealRepository repo = new MealRepository();
        Map<String, String> params = new HashMap<>();
        params.put("filter.radius", "20");
        assertEquals(repo.getURL("/meals", params), "http://localhost:8080/meals?filter.radius=20");
    }
}
