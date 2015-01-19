package com.elmsoftware.cloudwatch;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class CloudWatchService {

	private static final Logger log = LoggerFactory.getLogger(CloudWatchService.class);
	public static final String ENVIRONMENT_NAME = "environment";

	private final AmazonCloudWatch cloudWatch;
	private final String environment;
	private final String namespace;

	public CloudWatchService(
			@Nonnull final AmazonCloudWatch cloudWatch,
			@Nonnull final String environment,
			@Nonnull final String namespace
	) {
		this.cloudWatch = cloudWatch;
		this.environment = environment;
		this.namespace = namespace;
	}

	public void addMetric(
			@Nonnull final String metricName,
			@Nonnull final Double metricValue,
			@Nonnull final String metricUnit,
			@Nonnull final Dimension... dimensions
	) {
		try {

			log.trace("putting CloudWatch metric: {}/{}/{}/{}", namespace, metricName, metricUnit, metricValue);

			cloudWatch.putMetricData(
					new PutMetricDataRequest()
							.withNamespace(namespace)
							.withMetricData(
									new MetricDatum()
											.withDimensions(dimensions)
											.withDimensions(
													new Dimension()
															.withName(ENVIRONMENT_NAME)
															.withValue(environment)
											)
											.withMetricName(metricName)
											.withUnit(metricUnit)
											.withValue(metricValue)
							)
			);

		} catch (final Exception e) {
			log.warn("Error sending metric data to CloudWatch", e);
		}

	}

}
