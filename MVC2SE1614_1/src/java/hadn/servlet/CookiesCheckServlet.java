/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hadn.servlet;

import hadn.registration.RegistrationDAO;
import hadn.registration.RegistrationDTO;
import hadn.utils.MyApplicationConstants;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ACER
 */
@WebServlet(name = "CookiesCheckServlet", urlPatterns = {"/CookiesCheckServlet"})
public class CookiesCheckServlet extends HttpServlet {

//    private final String LOGIN_PAGE = "login.html";
//    private final String SEARCH_PAGE = "search.jsp";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        Properties siteMaps = (Properties)context.getAttribute("SITEMAPS");
        response.setContentType("text/html;charset=UTF-8");
//        String url = LOGIN_PAGE;
        String url = siteMaps.getProperty(MyApplicationConstants.DispatchFeatures.LOGIN_PAGE);
        try {
            //1. Check if cookies existed
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                //2. Get last cookie
                Cookie lastCookie = cookies[cookies.length - 1];
                //3. get username and password
                String username = lastCookie.getName();
                String password = lastCookie.getValue();
                //4. Check login
                RegistrationDAO dao = new RegistrationDAO();
                //boolean result = dao.checkLogin(username, password);
                
                dao.checkLogin(username, password);
                List<RegistrationDTO> result = dao.getAccounts();
                
                if (result != null) {
//                    url = SEARCH_PAGE;
                    String lastname = result.get(0).getFullName();
                    url = siteMaps.getProperty(MyApplicationConstants.LoginFeature.SEARCH_PAGE);
                    //send cookies to store in client   
                    HttpSession session = request.getSession();
                    session.setAttribute("LASTNAME", lastname);
                }
            }

        } catch (SQLException ex) {
            log("CookiesCheckServlet _ SQL" + ex.getMessage());
        } catch (NamingException ex) {
            log("CookiesCheckServlet _ SQL" + ex.getMessage());

        } finally {
            //response.sendRedirect(url);
            
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
        }
//        String url = LOGIN_PAGE;
//        if (request.getSession().getAttribute("username") == null) {
//                response.sendRedirect(url);
//            }
//        else {
//            url = SEARCH_PAGE;
//            RequestDispatcher rd = request.getRequestDispatcher(url);
//            rd.forward(request, response);
//        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
