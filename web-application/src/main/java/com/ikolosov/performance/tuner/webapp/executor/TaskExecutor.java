package com.ikolosov.performance.tuner.webapp.executor;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author ikolosov
 */
@Singleton
public class TaskExecutor {

	@Resource
	private ManagedExecutorService executorService;

	/**
	 * []
	 * Blocking - blocks initial request thread.
	 * It's blocked at Future#get, while waiting for request processing completion.
	 * Thus unnecessary busy wait occurs.
	 * <p>
	 * Impacts high-load cases performance in a negative manner.
	 *
	 * @param resp servlet response
	 */
	public void serveRegular(HttpServletResponse resp) throws InterruptedException, ExecutionException, TimeoutException {
		executorService.submit(() -> {
			timeConsumingOperation(resp);
		}).get(10, TimeUnit.SECONDS);
	}

	/**
	 * []
	 * Non-blocking - does not block initial request thread.
	 * Letting it to avoid busy wait and get back to servlet request thread pool right after async processing has started.
	 * Request processing completion is signalled from a worker thread by AsyncContext#complete call.
	 * <p>
	 * Therefore, servlet async mode capability - performance improvement in high-load cases - becomes utilized.
	 *
	 * @param context async context
	 */
	public void serveAsync(AsyncContext context) {
		executorService.submit(() -> {
			timeConsumingOperation((HttpServletResponse) context.getResponse());
			context.complete();
		});
	}

	/**
	 * []
	 * Long running processing operation, that is to be delegated to ManagedExecutorService for further execution.
	 *
	 * @param resp servlet response
	 */
	private static void timeConsumingOperation(HttpServletResponse resp) {
		try (PrintWriter writer = resp.getWriter()) {
			// [] dummy wait, useful efforts emulation
			Thread.sleep(1000);
			// [] http response infill
			resp.setHeader("Content-Type", "text/html; charset=UTF-8");
			writer.print("SUCCESS");
			writer.flush();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
