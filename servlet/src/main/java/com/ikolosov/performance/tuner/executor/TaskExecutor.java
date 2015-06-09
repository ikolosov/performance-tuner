package com.ikolosov.performance.tuner.executor;

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

	public void serveRegular(HttpServletResponse resp) throws InterruptedException, ExecutionException, TimeoutException {
		executorService.submit(() -> {
			perform(resp);
		}).get(10, TimeUnit.SECONDS);
	}

	public void serveAsync(AsyncContext context) {
		executorService.submit(() -> {
			perform((HttpServletResponse) context.getResponse());
			context.complete();
		});
	}

	private static void perform(HttpServletResponse resp) {
		try (PrintWriter writer = resp.getWriter()) {
			// [] dummy wait, useful efforts emulation
			Thread.sleep(1000);
			// [] http response infill
			resp.setHeader("Content-Type", "text/xml; charset=UTF-8");
			writer.print("SUCCESS");
			writer.flush();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
