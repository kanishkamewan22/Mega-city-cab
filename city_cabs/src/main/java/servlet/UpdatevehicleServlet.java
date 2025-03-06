package servlet;

import service.VehicleService;
import service.updateVehicleService;
import model.Vehicle;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/updateVehicle")
public class UpdatevehicleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String vehicleIdStr = request.getParameter("vehicleid");

        if (vehicleIdStr != null && !vehicleIdStr.isEmpty()) {
            try {
                int vehicleId = Integer.parseInt(vehicleIdStr);

                VehicleService vehicleService = new VehicleService();
                Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

                if (vehicle != null) {
                    request.setAttribute("vehicle", vehicle);
                    request.getRequestDispatcher("/up.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Vehicle not found.");
                    request.getRequestDispatcher("/up.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid vehicle ID format.");
                request.getRequestDispatcher("/up.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Vehicle ID is missing.");
            request.getRequestDispatcher("/up.jsp").forward(request, response);
        }
    }

   



        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            try {
                // Getting vehicle ID from the form (hidden field)
                String vehicleIdStr = request.getParameter("vehicleid");

                // Debugging log: Check the value of vehicleIdStr
                System.out.println("Vehicle ID received from form: " + vehicleIdStr);

                if (vehicleIdStr == null || vehicleIdStr.isEmpty()) {
                    // If vehicleId is missing, throw an error
                    throw new IllegalArgumentException("Vehicle ID is required.");
                }

                // Convert the vehicleId to an integer
                int vehicleId = Integer.parseInt(vehicleIdStr);  

                // Retrieve other form data
                String vehicleName = request.getParameter("vehicleName");
                String vehicleModel = request.getParameter("vehicleModel");
                String vehicleType = request.getParameter("vehicleType");
                String fuelType = request.getParameter("fuelType");
                int modelYear = Integer.parseInt(request.getParameter("modelYear"));
                String engineSize = request.getParameter("engineSize");
                double price = Double.parseDouble(request.getParameter("price"));

                // Handle file uploads for vehicle photos
                InputStream vehiclePhoto = request.getPart("photo") != null ? request.getPart("photo").getInputStream() : null;
                InputStream vehiclePhototwo = request.getPart("phototwo") != null ? request.getPart("phototwo").getInputStream() : null;

                // Create Vehicle object
                Vehicle vehicle = new Vehicle(vehicleId, vehicleName, vehicleModel, vehicleType, fuelType,
                                              modelYear, engineSize, price, vehiclePhoto, vehiclePhototwo);

                // Call service to update the vehicle in the database
                VehicleService service = new VehicleService();
                boolean isUpdated = service.updateVehicle(vehicle);

                // Handle the result
                if (isUpdated) {
                    request.setAttribute("message", "Vehicle updated successfully!");
                    request.getRequestDispatcher("/update.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Failed to update vehicle.");
                    request.getRequestDispatcher("/update.jsp").forward(request, response);
                }
            } catch (IllegalArgumentException e) {
                // Handle invalid input, e.g. missing or incorrect vehicle ID
                request.setAttribute("error", "Invalid input: " + e.getMessage());
                request.getRequestDispatcher("/update.jsp").forward(request, response);
            } catch (Exception e) {
                // Handle other unexpected errors
                e.printStackTrace();
                
                // Log additional information
                System.out.println("An unexpected error occurred while updating the vehicle.");
                System.out.println("Vehicle ID: " + request.getParameter("vehicleid"));
                System.out.println("Vehicle Name: " + request.getParameter("vehicleName"));
                System.out.println("Vehicle Model: " + request.getParameter("vehicleModel"));

                // Set a user-friendly error message
                request.setAttribute("error", "An error occurred while updating the vehicle.");
                request.getRequestDispatcher("/update.jsp").forward(request, response);
            }
        }
    
}