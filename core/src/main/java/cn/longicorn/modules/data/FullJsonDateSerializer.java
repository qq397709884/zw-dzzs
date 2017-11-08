package cn.longicorn.modules.data;

import cn.longicorn.modules.annotation.NotThreadSafe;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@NotThreadSafe
public class FullJsonDateSerializer extends JsonSerializer<Date> {

    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (date != null) {
            gen.writeString(DATEFORMAT.format(date));
        } else {
            gen.writeString("");
        }
    }

}