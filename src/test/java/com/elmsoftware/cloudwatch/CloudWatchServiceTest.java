package com.elmsoftware.cloudwatch;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class CloudWatchServiceTest {

	@Mock
	private AmazonCloudWatch cloudWatch;
	private final String environment = "env-value";
	private final String namespace = "ns-value";

	private CloudWatchService cloudWatchService;

	@Captor
	private ArgumentCaptor<PutMetricDataRequest> requestCaptor;

	@Before
	public void beforeCloudWatchServiceTest() {
		initMocks(this);
		cloudWatchService = new CloudWatchService(cloudWatch, environment, namespace);
	}

	@Test
	public void should_add_metric() {

		// setup test
		final String testDimName = "dim-name";
		final Dimension dimension = new Dimension().withName(testDimName).withValue("dim-value");

		// run test
		cloudWatchService.addMetric("metric-name", 23.4, "metric-unit", dimension);

		// verify outcome
		verify(cloudWatch).putMetricData(requestCaptor.capture());
		verifyNoMoreInteractions(cloudWatch);

		final PutMetricDataRequest actualRequest = requestCaptor.getValue();
		assertEquals(namespace, actualRequest.getNamespace());
		final MetricDatum metricDatum = actualRequest.getMetricData().get(0);
		final List<Dimension> dimensionList = metricDatum.getDimensions();
		assertEquals(2, dimensionList.size());
		Dimension environmentDim = null;
		Dimension testDim = null;
		for(final Dimension dim : dimensionList){
			if(dim.getName().equals(testDimName)){
				testDim = dim;
			}
			if(dim.getName().equals(CloudWatchService.ENVIRONMENT_NAME)){
				environmentDim = dim;
			}
		}
		assertNotNull(environmentDim);
		assertNotNull(testDim);

	}

	@Test
	public void should_not_explode_on_cloudwatch_failure() {

		// setup test
		doThrow(new RuntimeException("test"))
				.when(cloudWatch).putMetricData(any(PutMetricDataRequest.class));
		final Dimension dimension = new Dimension().withName("dim-name").withValue("dim-value");

		// run test
		cloudWatchService.addMetric("metric-name", 23.4, "metric-unit", dimension);

		// verify outcome - getting here is a win, but we'll make sure we passed the correct data
		// in anyway
		verify(cloudWatch).putMetricData(requestCaptor.capture());
		verifyNoMoreInteractions(cloudWatch);

	}

}
