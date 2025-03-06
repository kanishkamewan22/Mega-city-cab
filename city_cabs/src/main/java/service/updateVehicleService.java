package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import model.Vehicle;
import contoller.database;

public class updateVehicleService {

    // Method to update vehicle details in the database
    public boolean updateVehicle(Vehicle vehicle) {
        String query = "UPDATE vehicles SET vehicleName = ?, vehicleModel = ?, vehicleType = ?, fuelType = ?, "
                     + "modelYear = ?, engineSize = ?, price = ?, vehiclePhoto = ?, vehiclePhototwo = ? WHERE id = ?";

        try (Connection con = database.getcon(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, vehicle.getVehicleName());
            ps.setString(2, vehicle.getVehicleModel());
            ps.setString(3, vehicle.getVehicleType());
            ps.setString(4, vehicle.getFuelType());
            ps.setInt(5, vehicle.getModelYear());
            ps.setString(6, vehicle.getEngineSize());
            ps.setDouble(7, vehicle.getPrice());

            // Handle image upload as InputStream (Main image)
            if (vehicle.getVehiclePhoto() != null) {
                ps.setBytes(8, convertInputStreamToBytes(vehicle.getVehiclePhoto())); // vehicle photo
            } else {
                ps.setNull(8, java.sql.Types.BLOB); // Set NULL if no photo
            }

            // Handle second vehicle photo (secondary image)
            if (vehicle.getvehiclePhototwo() != null) {
                ps.setBytes(9, convertInputStreamToBytes(vehicle.getvehiclePhototwo())); // second vehicle photo
            } else {
                ps.setNull(9, java.sql.Types.BLOB); // Set NULL if no second image
            }

           // ps.setInt(10, vehicle.getId()); // Set the vehicle ID to update the right record

            int rowsAffected = ps.executeUpdate(); // Execute the update query
            return rowsAffected > 0; // Return true if the update is successful

        } catch (SQLException | IOException e) {
            e.printStackTrace(); // Print full stack trace for debugging
            return false; // Return false if there was an error
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Print full stack trace for debugging
            return false;
        }
    }

    // Helper method to convert InputStream to byte[]
    private byte[] convertInputStreamToBytes(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null; // Return null if InputStream is null
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        return byteArrayOutputStream.toByteArray(); // Return the byte array
    }
}

