package servlet;

import service.VehicleBookingService;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/deleteBooking")
public class DeleteBookingServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    // Handle POST request to delete booking
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get bookingId from the form
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        
        // Create a service object to call the delete method
        VehicleBookingService bookingService = new VehicleBookingService();
        
        // Call delete method to remove the booking from the database
        boolean isDeleted = bookingService.deleteBooking(bookingId);
        
        // Check if the deletion was successful
        if (isDeleted) {
            // Redirect to the bookings page with success message
            request.setAttribute("message", "Booking deleted successfully.");
            request.getRequestDispatcher("/viewBookings.jsp").forward(request, response);
        } else {
            // Redirect to the bookings page with an error message
            request.setAttribute("error", "Failed to delete booking. Please try again.");
            request.getRequestDispatcher("/viewBookings.jsp").forward(request, response);
        }
    }
}
