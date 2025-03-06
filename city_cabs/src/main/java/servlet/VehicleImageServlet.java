package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import contoller.database;

@WebServlet("/vehicleImage")
public class VehicleImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vehicleId = request.getParameter("id");
        String imageType = request.getParameter("imageType"); // Get imageType from query parameter (vehiclePhoto or vehiclePhototwo)

        if (vehicleId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing vehicle ID");
            return;
        }

        // Default to vehiclePhoto if imageType is not specified or is invalid
        String query = "SELECT vehiclePhoto, vehiclePhototwo FROM vehicles WHERE id = ?";
        String imageColumn = "vehiclePhoto"; // Default image column

        if (imageType != null && imageType.equals("secondary")) {
            imageColumn = "vehiclePhototwo"; // Use secondary image if imageType is specified
        }

        try (Connection con = database.getcon();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            
            pstmt.setInt(1, Integer.parseInt(vehicleId));
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                InputStream inputStream = rs.getBinaryStream(imageColumn); // Get the image based on the selected column

                if (inputStream != null) {
                    // Set the content type to image/jpeg for the image response
                    response.setContentType("image/jpeg");

                    // Output the image data to the response
                    OutputStream outputStream = response.getOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    inputStream.close();
                    outputStream.close();
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Vehicle not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing image.");
        }
    }
}
