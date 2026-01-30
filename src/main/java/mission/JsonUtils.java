package mission;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static int getInt(String json, String key) {
        JSONObject obj = new JSONObject(json);
        return obj.getInt(key);
    }

    public static String getString(String json, String key) {
        JSONObject obj = new JSONObject(json);
        return obj.optString(key, null);
    }

    public static String getString(String json, String objectKey, String nestedKey) {
        JSONObject obj = new JSONObject(json);
        JSONObject nested = obj.getJSONObject(objectKey);
        return nested.getString(nestedKey);
    }

    public static int getInt(String json, String objectKey, String nestedKey) {
        JSONObject obj = new JSONObject(json);
        JSONObject nested = obj.getJSONObject(objectKey);
        return nested.getInt(nestedKey);
    }

    public static List<Integer> getIntList(String json, String arrayKey, String fieldKey) {
        JSONObject obj = new JSONObject(json);
        JSONArray arr = obj.getJSONArray(arrayKey);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject row = arr.getJSONObject(i);
            result.add(row.getInt(fieldKey));
        }
        return result;
    }
}
