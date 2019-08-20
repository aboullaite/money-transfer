package me.aboullaite.moneytransfer.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate ) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        jsonWriter.value(localDate.format(formatter));
    }

    @Override
    public LocalDateTime read( final JsonReader jsonReader ) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString());
    }
}
