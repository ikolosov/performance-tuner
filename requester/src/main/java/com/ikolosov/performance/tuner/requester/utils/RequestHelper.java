package com.ikolosov.performance.tuner.requester.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * @author ikolosov
 */
public final class RequestHelper {

	public static ResultBean performRequest(CloseableHttpClient httpClient, HttpGet httpGet) {
		boolean succeeded = false;
		long duration = 0;
		String respContent = null;
		// [] start measurement
		Instant start = Instant.now();
		try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
			// [] stop measurement, determine request duration
			duration = Duration.between(start, Instant.now()).toMillis();
			respContent = EntityUtils.toString(response.getEntity());
			succeeded = (200 == response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResultBean(succeeded, duration, respContent);
	}

	public static void printStats(List<ResultBean> results) {
		System.out.println("Success rate:   " +
				(int) results.stream()
						.filter(ResultBean::isSucceeded)
						.count() + "/" + results.size());
		System.out.println("Total duration: " +
				results.stream()
						.mapToLong(ResultBean::getDuration)
						.sum() + "ms");
	}

	private RequestHelper() {
	}

	public static final class ResultBean {

		private final boolean succeeded;
		private final long duration;
		private final String respContent;

		public ResultBean(boolean succeeded, long duration, String respContent) {
			this.succeeded = succeeded;
			this.duration = duration;
			this.respContent = respContent;
		}

		public boolean isSucceeded() {
			return succeeded;
		}

		public long getDuration() {
			return duration;
		}
	}
}
