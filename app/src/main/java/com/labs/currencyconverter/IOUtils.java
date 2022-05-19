package com.labs.currencyconverter;

import java.io.InputStream;
import java.io.Reader;

public class IOUtils {
    public static void closeQuietly(InputStream in)  {
        try {
            in.close();
        }catch (Exception e) {
        }
    }
    public static void closeQuietly(Reader reader)  {
        try {
            reader.close();
        }catch (Exception e) {
        }
    }
}
