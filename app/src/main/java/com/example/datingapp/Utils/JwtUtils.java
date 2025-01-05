package com.example.datingapp.Utils;

import android.util.Base64;
import org.json.JSONObject;

public class JwtUtils {
    public static class JwtPayload {
        private String userGuid;
        private String name;
        private String email;

        // Constructor
        public JwtPayload(String userGuid, String name, String email) {
            this.userGuid = userGuid;
            this.name = name;
            this.email = email;
        }

        // Getters
        public String getUserGuid() {
            return userGuid;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    // Function to decode the JWT
    public static JwtPayload decodeJwt(String token) {
        try {
            // Split the token into parts
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid token");
            }

            // Decode the payload (2nd part)
            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));

            // Convert the payload to JSON
            JSONObject payloadJson = new JSONObject(payload);

            // Get the information from the payload
            String userGuid = payloadJson.getString("sub");
            String name = payloadJson.getString("name");
            String email = payloadJson.getString("email");

            // Return the extracted information from the JWT
            return new JwtPayload(userGuid, name, email);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error decoding JWT: " + e.getMessage());
            return null; // Return null in case of error
        }
    }
}