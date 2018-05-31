package com.brian.common.utils;

import java.io.Closeable;

public class IOUtil {

    public static void closeStream(Closeable stream) {
        if (stream == null) {
            return;
        }
        try {
            stream.close();
        } catch (Exception e) {
            LogUtil.printError(e);
        }
    }

}
