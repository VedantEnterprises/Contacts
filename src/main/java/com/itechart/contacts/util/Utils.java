package com.itechart.contacts.util;

import com.google.gson.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {

    public static Gson buildGsonInstance() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {
            try {
                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                return new Date(df.parse(json.getAsString()).getTime());
            } catch (ParseException e) {
                return null;
            }
        });
        gsonBuilder.registerTypeAdapter(Timestamp.class, (JsonDeserializer<Timestamp>) (json, typeOfT, context) -> {
            try {
                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                return new Timestamp(df.parse(json.getAsString()).getTime());
            } catch (ParseException e) {
                return null;
            }
        });
        return gsonBuilder.create();
    }
}