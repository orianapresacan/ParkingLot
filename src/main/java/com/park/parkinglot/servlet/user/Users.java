/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.park.parkinglot.servlet.user;

import com.park.parkinglot.common.UserDetails;
import com.park.parkinglot.ejb.InvoiceBean;
import com.park.parkinglot.ejb.UserBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Oriana
 */
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"AdminRole", "ClientRole"}))
@WebServlet(name = "Users", urlPatterns = {"/Users"})
public class Users extends HttpServlet {

    @Inject
    private UserBean userBean;

    @Inject
    InvoiceBean invoiceBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<UserDetails> users = userBean.getAllUsers();
        request.setAttribute("users", users);
        if (!invoiceBean.getUserIds().isEmpty()) {
            Collection<String> usernames = userBean.findUsernames(invoiceBean.getUserIds());
            request.setAttribute("invoices", usernames);
        }
        request.setAttribute("numberOfAdmins", 1);
        request.getRequestDispatcher("/WEB-INF/pages/users.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] userIdsAsString = request.getParameterValues("user_ids");
        if (userIdsAsString != null) {
            Set<Integer> userIds = new HashSet<Integer>();
            for (String userIdAsString : userIdsAsString) {
                userIds.add(Integer.parseInt(userIdAsString));
            }
            invoiceBean.getUserIds().addAll(userIds);

        }
        response.sendRedirect(request.getContextPath() + "/Users");
    }

    @Override
    public String getServletInfo() {
        return "Users";
    }

}
