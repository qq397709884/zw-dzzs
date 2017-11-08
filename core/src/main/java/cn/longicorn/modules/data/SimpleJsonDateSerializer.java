package cn.longicorn.modules.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleJsonDateSerializer extends JsonSerializer<Date> {

    private static final SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (date != null) {
            gen.writeString(DATAFORMAT.format(date));
        } else {
            gen.writeString("");
        }
    }

}