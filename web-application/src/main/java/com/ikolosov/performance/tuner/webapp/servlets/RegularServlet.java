package com.ikolosov.performance.tuner.webapp.servlets;

import com.ikolosov.performance.tuner.webapp.executor.TaskExecutor;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author ikolosov
 */
@WebServlet(urlPatterns = {"/regular"})
public class RegularServlet extends HttpServlet {

	@Inject
	private TaskExecutor taskExecutor;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			taskExecutor.serveRegular(resp);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}
