package cn.jxust.supermarket.client;

import cn.jxust.supermarket.server.ApiResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientRequestHandler {
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public ClientRequestHandler(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    public Object sendRequest(String requestType, Object requestData) throws RuntimeException {
        try {
            // Send request to the server
            objectOutputStream.writeObject(requestType);
            objectOutputStream.writeObject(requestData);
            objectOutputStream.flush();

            // Receive response from the server
            ApiResponse response = (ApiResponse) objectInputStream.readObject();
            if (response.success())
                return response.data();
            else
                throw response.exception();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during request handling: " + e.getMessage());
            return null;
        }
    }
}
