package net.fakerestapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fakerestapi.utils.Log;

import java.io.InputStream;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T getTestData(String path, Class<T> type){
        try(InputStream stream = ResourceLoader.getResource(path)){
            return mapper.readValue(stream, type);
        }catch (Exception e){
            Log.error("unable to read test data " + path, e);
        }
        return null;
    }
}
