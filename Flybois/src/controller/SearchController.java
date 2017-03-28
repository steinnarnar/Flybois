 package controller;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Criteria;
import model.Result;
public class SearchController extends HttpServlet {
         @Override
          protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
               System.out.println("Calling doPost...");
               String fromAirport = req.getParameter("fromAirport");
               String toAirport = req.getParameter("toAirport");
               String fromDay = req.getParameter("fromDay");
               String fromMonth = req.getParameter("fromMonth");
               String fromYear = req.getParameter("fromYear");
              
               String toDay = req.getParameter("toDay");
               String toMonth = req.getParameter("toMonth");
               String toYear = req.getParameter("toYear");

               Criteria criteria = new Criteria();
               Calendar departureCalendar = Calendar.getInstance();
               departureCalendar.set(Integer.parseInt(fromYear), Integer.parseInt(fromMonth) - 1, Integer.parseInt(fromDay)); // month starts from 0 in Calendar
               Calendar returnCalendar = Calendar.getInstance();
               if (toYear.length() > 0 && toMonth.length() > 0 && toDay.length() > 0) {
                    returnCalendar.set(Integer.parseInt(toYear), Integer.parseInt(toMonth) - 1, Integer.parseInt(toDay));
               }

               criteria.setDepartureAirport(fromAirport);
               criteria.setArrivalAirport(toAirport);
               criteria.setDepartureDate(departureCalendar);
               criteria.setReturnDate(returnCalendar);
              
               System.out.println("From airport =" + fromAirport);
               System.out.println("To   airport =" + toAirport);
               Result[] resultsForward = null;
               Result[] resultsReturn = null;
              
               try {
                    resultsForward = new SearchDAO().getResultsForward(criteria);
                    System.out.println("Results Forward ["+ resultsForward +"] ");
                    resultsReturn = new SearchDAO().getResultsReturn(criteria);
                    System.out.println("Results Return ["+ resultsReturn +"] ");
               } catch (SQLException e) {
                    e.printStackTrace();
               }
              
               HttpSession session = req.getSession();
               session.setAttribute("ATTR_RESULTS_FORWARD", resultsForward);
               session.setAttribute("ATTR_RESULTS_RETURN", resultsReturn);
               RequestDispatcher dis = req.getRequestDispatcher("resultView.jsp");
               dis.forward(req, resp);
          }
}