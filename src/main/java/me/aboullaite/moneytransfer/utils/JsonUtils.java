package me.aboullaite.moneytransfer.utils;

import com.google.gson.Gson;

import java.time.LocalDateTime;

public final class JsonUtils {

    private JsonUtils() {}

    public static Gson make() {
        return new Gson().newBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .setPrettyPrinting().create();
    }

    public static String toJson(Exception err, int errCode) {
        final ErrorInfo info = new ErrorInfo(err, errCode);
        return make().toJson(info);
    }

    private static class ErrorInfo {

        private final int errorCode;
        private final String errorMessage;

        ErrorInfo(Exception err, int errCode) {
            this.errorCode = errCode;
            this.errorMessage = err.getLocalizedMessage();
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public String toString() {
            return "ErrorInfo{" +
                    "errorCode=" + errorCode +
                    ", errorMessage='" + errorMessage + '\'' +
                    '}';
        }
    }
}
