package org.opencds.cqf;

import org.opencds.cqf.client.RequestFactory;
import org.opencds.cqf.testcase.MeasureTestScript;
import org.opencds.cqf.testcase.ResourceItems;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class TestHelper {

    public static void fetchFiles(File dir, Consumer<File> fileConsumer) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files == null) return;
            for (File file1 : files) {
                fetchFiles(file1, fileConsumer);
            }
        }
        else {
            fileConsumer.accept(dir);
        }
    }

    public static void loadMeasureData(MeasureTestScript script, String baseUrl) throws IOException {
        for (String artifact : script.getLoadArtifacts().getBundle()) {
            String bundle = new String(Files.readAllBytes(Paths.get(artifact)), StandardCharsets.UTF_8);
            RequestFactory.makeRequest(RequestFactory.RequestType.POST, baseUrl, bundle);
        }
        for (ResourceItems artifact : script.getLoadArtifacts().getResource()) {
            String resource = new String(Files.readAllBytes(Paths.get(artifact.getPath())), StandardCharsets.UTF_8);
            RequestFactory.makeRequest(RequestFactory.RequestType.PUT, baseUrl + "/" + artifact.getReference(), resource);
        }
        for (MeasureTestScript.Test test : script.getTest()) {
            for (String artifact : test.getLoadPatientData().getBundle()) {
                String bundle = new String(Files.readAllBytes(Paths.get(artifact)), StandardCharsets.UTF_8);
                RequestFactory.makeRequest(RequestFactory.RequestType.POST, baseUrl, bundle);
            }
            for (ResourceItems artifact : test.getLoadPatientData().getResource()) {
                String resource = new String(Files.readAllBytes(Paths.get(artifact.getPath())), StandardCharsets.UTF_8);
                RequestFactory.makeRequest(RequestFactory.RequestType.PUT, baseUrl + "/" + artifact.getReference(), resource);
            }
        }
    }

    public static String buildEvaluateMeasureRequest(MeasureTestScript.Test test) {
        String evaluateMeasureRequest = "/Measure/" + test.getMeasureId() + "/$evaluate-measure";
        String evaluateMeasureQuery = "";
        if (test.getPatientId() != null && !test.getPatientId().isEmpty()) {
            evaluateMeasureQuery += "?patient=" + test.getPatientId();
        }
        if (test.getReportType() != null && !test.getReportType().isEmpty()) {
            evaluateMeasureQuery += evaluateMeasureQuery.isEmpty()
                    ? "?reportType=" + test.getReportType()
                    : "&reportType=" + test.getReportType();
        }
        if (test.getPeriodStart() != null && !test.getPeriodStart().isEmpty()) {
            evaluateMeasureQuery += evaluateMeasureQuery.isEmpty()
                    ? "?periodStart=" + test.getPeriodStart()
                    : "&periodStart=" + test.getPeriodStart();
        }
        else {
            throw new RuntimeException("The $evaluate-measure operation requires a periodStart value");
        }
        if (test.getPeriodEnd() != null && !test.getPeriodEnd().isEmpty()) {
            evaluateMeasureQuery += evaluateMeasureQuery.isEmpty()
                    ? "?periodEnd=" + test.getPeriodEnd()
                    : "&periodEnd=" + test.getPeriodEnd();
        }
        else {
            throw new RuntimeException("The $evaluate-measure operation requires a periodEnd value");
        }
        if (test.getPractitioner() != null && !test.getPractitioner().isEmpty()) {
            evaluateMeasureQuery += evaluateMeasureQuery.isEmpty()
                    ? "?practitioner=" + test.getPractitioner()
                    : "&practitioner=" + test.getPractitioner();
        }
        if (test.getLastReceivedOn() != null && !test.getLastReceivedOn().isEmpty()) {
            evaluateMeasureQuery += evaluateMeasureQuery.isEmpty()
                    ? "?lastReceivedOn=" + test.getLastReceivedOn()
                    : "&lastReceivedOn=" + test.getLastReceivedOn();
        }

        return evaluateMeasureRequest + evaluateMeasureQuery;
    }
}
