package server;

import com.google.gson.Gson;

public class Serializer {

    public static <T> T deserialize(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }

    public static <T> String serialize(T object) {
        return new Gson().toJson(object);
    }
}
