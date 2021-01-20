package com.example.app_client.Utils;

public class BookingCreated {
    private static final BookingCreated INSTANCE = new BookingCreated();

    public static BookingCreated instance() {
        return INSTANCE;
    }

    private BookingCreated() {
    }
}
