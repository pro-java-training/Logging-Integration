package com.codve;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet(
        name = "actionServlet",
        urlPatterns = "/filter"
)
public class ActionServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            log.info("Received request with action {}.", action);
            String contents = null;
            switch (action) {
                case "readMe":
                    contents = this.readFile("../README.md", true);
                    break;
                case "readLicense":
                    contents = this.readFile("../LICENSE", false);
                    break;
                default:
                    contents = action + " is not supported.";
                    log.warn(contents);
            }
            if (contents != null) {
                response.getWriter().write(contents);
            }
        } else {
            log.error("No action specified.");
            response.getWriter().write("no action specified.");
        }
    }

    protected String readFile(String fileName, boolean delete) {
        log.entry(fileName, delete);
        try {
            byte[] data = Files.readAllBytes(new File(fileName).toPath());
            log.info("read file {} success.", fileName);
            return log.exit(new String(data));
        } catch (IOException e) {
            log.error(MarkerManager.getMarker("CODVE_CONSOLE"),
                    "Failed to read file {}.", fileName, e);
            return null;
        }
    }
}
