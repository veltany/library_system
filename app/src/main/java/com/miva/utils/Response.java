package com.miva.utils;

/**
 * Unified Response Object
 */

public class Response<Data> {

    public boolean success;
    public String message;
    public Data data;

    // Polymorphic constructor

    // response with data
    public Response(boolean success, String message, Data data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // response without data
    public Response(boolean success, String message) {
        this(success, message, null);
    }

    // static style
    public Response() {
        this(false, "", null);
    }

    // Getters Methods

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Data data) {
        this.data = data;
    }

    // Static Helpers

    /**
     * shorthand to return success response
     *
     * @param message
     * @param data
     * @return Response<Data>
     */

    // with data method overload
    public static <T> Response<T> success(String message, T data) {
        return new Response<T>(true, message, data);
    }

    // without data overload
    public static <T> Response<T> success(String message) {
        return new Response<T>(true, message);
    }

    /**
     * shorthand to return error response
     *
     * @param message
     * @return Response<Data>
     */
    // with data method overload
    public Response<Data> error(String message, Data data) {
        return new Response<Data>(false, message, data);
    }

    // without data overload
    public static <T> Response<T> error(String message) {
        return new Response<T>(false, message);
    }

}
