package com.ddsolutions.stream.service;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.ddsolutions.stream.utility.AWSUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CloudwatchOperation {
    private static final Logger LOGGER = LogManager.getLogger(CloudwatchOperation.class);
    private static final String NAMESPACE = "RSVPLambdaProcessor";

    private AmazonCloudWatchClient cloudWatchClient;

    public CloudwatchOperation() {
        this(AWSUtil.getCloudwatchClient());
    }

    public CloudwatchOperation(AmazonCloudWatchClient cloudWatchClient) {
        this.cloudWatchClient = cloudWatchClient;
    }

    public void putMetricData(String metricName, String dimensionName, String dimensionValue) {
        PutMetricDataRequest metricDataRequest = new PutMetricDataRequest();

        List<MetricDatum> metricDatumList = new ArrayList<>();
        MetricDatum metricDatum = new MetricDatum();
        metricDatum.setValue(1d);
        metricDatum.setUnit(StandardUnit.Count);
        metricDatum.setMetricName(metricName);

        List<Dimension> dimensionList = new ArrayList<>();
        Dimension dimension = new Dimension();
        dimension.setName(dimensionName);
        dimension.setValue(dimensionValue);
        dimensionList.add(dimension);

        metricDatum.setDimensions(dimensionList);
        metricDatumList.add(metricDatum);

        metricDataRequest.setMetricData(metricDatumList);
        metricDataRequest.setNamespace(NAMESPACE);

        cloudWatchClient.putMetricData(metricDataRequest);
        LOGGER.debug("PutMetric for name space {} is successful", NAMESPACE);
    }

    public void putMetricDataWithCount(String metricName, String dimensionName, String dimensionValue, Double value) {
        PutMetricDataRequest metricDataRequest = new PutMetricDataRequest();

        List<MetricDatum> metricDatumList = new ArrayList<>();
        MetricDatum metricDatum = new MetricDatum();
        metricDatum.setValue(1d);
        metricDatum.setUnit(StandardUnit.Count);
        metricDatum.setMetricName(metricName);

        List<Dimension> dimensionList = new ArrayList<>();
        Dimension dimension = new Dimension();
        dimension.setName(dimensionName);
        dimension.setValue(dimensionValue);
        dimensionList.add(dimension);

        metricDatum.setDimensions(dimensionList);
        metricDatumList.add(metricDatum);

        metricDataRequest.setMetricData(metricDatumList);
        metricDataRequest.setNamespace(NAMESPACE);

        cloudWatchClient.putMetricData(metricDataRequest);
        LOGGER.debug("PutMetric for name space {} is successful", NAMESPACE);
    }
}
