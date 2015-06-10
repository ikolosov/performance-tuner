package com.ikolosov.performance.tuner.requester;

import com.ikolosov.performance.tuner.requester.utils.RequestHelper;
import com.ikolosov.performance.tuner.requester.utils.RequestHelper.ResultBean;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @author ikolosov
 */
public class AtWork {

	private static final String REGULAR_SERVLET_URN = "/web-application/regular";
	private static final String ASYNC_SERVLET_URN = "/web-application/async";

	private static final int REQUEST_COUNT = 10;

	public static void main(String[] args) {
		// [] regular servlets
		System.out.println("\n[] Regular servlet -----");
		launch(REGULAR_SERVLET_URN);
		// [] async servlets
		System.out.println("\n[] Async servlet -----");
		launch(ASYNC_SERVLET_URN);
	}

	private static void launch(String servletUrn) {
		try {
			// [] http client
			CloseableHttpClient httpClient = HttpClients.createDefault();
			// [] http request base
			URI uriBase = new URIBuilder()
					.setScheme("http")
					.setHost("localhost")
					.setPort(8080).build();
			// [] workload
			Collection<Callable<ResultBean>> tasks = new ArrayList<>();
			IntStream.range(0, REQUEST_COUNT).forEach(i -> tasks.add(
					() -> RequestHelper.performRequest(
							httpClient,
							new HttpGet(new URIBuilder(uriBase).setPath(servletUrn).build()))));
			// [] execution
			ExecutorService service = Executors.newWorkStealingPool();
			List<ResultBean> results = new CopyOnWriteArrayList<>();
			try {
				for (Future<ResultBean> aResult : service.invokeAll(tasks))
					results.add(aResult.get());
			} finally {
				service.shutdown();
			}
			// [] results
			RequestHelper.printStats(results);
		} catch (URISyntaxException |
				InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}
