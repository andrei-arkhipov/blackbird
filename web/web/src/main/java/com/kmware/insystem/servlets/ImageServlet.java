package com.kmware.insystem.servlets;

import com.kmware.insystem.dao.CardHolderDAO;
import com.kmware.insystem.model.CardHolder;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * Author: kelevra
 */
@WebServlet(value="/image_download/*", name="image-servlet")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 2116478039724502827L;

    @Inject
    CardHolderDAO dao;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        CardHolder entity = dao.getCardHolder(id);

        resp.setContentType(entity.getImageType());
        OutputStream out = resp.getOutputStream();
        out.write(entity.getImage());
    }
}
