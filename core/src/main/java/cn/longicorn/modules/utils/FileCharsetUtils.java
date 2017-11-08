package cn.longicorn.modules.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileCharsetUtils {

    public static String detectCharset(String fullFileName) throws IOException {
        String type = "GBK";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fullFileName);
            byte[] head = new byte[3];
            inputStream.read(head);
            if (head[0] == -1 && head[1] == -2)
                type = "UTF-16";
            if (head[0] == -2 && head[1] == -1)
                type = "Unicode";
            if (head[0] == -17 && head[1] == -69 && head[2] == -65)
                type = "UTF-8";
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (Exception e) {
                //Do nothing
            }
        }
        return type;
    }

}