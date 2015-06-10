package com.ikolosov.performance.tuner.webapp.servlets;

import com.ikolosov.performance.tuner.webapp.executor.TaskExecutor;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ikolosov
 */
@WebServlet(urlPatterns = {"/async"}, asyncSupported = true)
public class AsyncServlet extends HttpServlet {

	@Inject
	private TaskExecutor taskExecutor;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/**
		 * []
		 * Before processing has started, context listener might be registered at async context:
		 * 		<code>
		 * 		context.addListener(new javax.servlet.AsyncListener(){...});
		 * 		</code>
		 * @see javax.servlet.AsyncListener
		 */
		taskExecutor.serveAsync(req.startAsync());
	}
}
