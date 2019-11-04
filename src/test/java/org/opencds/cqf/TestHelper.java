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
        for (String artifact : script.getTest().getLoadPatientData().getBundle()) {
            String bundle = new String(Files.readAllBytes(Paths.get(artifact)), StandardCharsets.UTF_8);
            RequestFactory.makeRequest(RequestFactory.RequestType.POST, baseUrl, bundle);
        }
        for (ResourceItems artifact : script.getTest().getLoadPatientData().getResource()) {
            String resource = new String(Files.readAllBytes(Paths.get(artifact.getPath())), StandardCharsets.UTF_8);
            RequestFactory.makeRequest(RequestFactory.RequestType.PUT, baseUrl + "/" + artifact.getReference(), resource);
        }
    }

    public static String buildEvaluateMeasureRequest(MeasureTestScript script) {
        String evaluateMeasureRequest = "/Measure/" + script.getTest().getMeasureId() + "/$evaluate-measure";
        String evaluateMeasureQuery = "";
        if (script.getTest().getPatientId() != null && !script.getTest().getPatientId().isEmpty()) {
            evaluateMeasureQuery += evaluateMeasureQuery.isEmpty()
                    ? "?patient=" + script.getTest().getPatientId()
                    : "&patient=" + script.getTest().getPatientId();
        }
        if (script.getTest().getReportType() != null && !script.getTest().getReportType().isEmpty()) {
            evaluateMeasureQuery += evaluateMeasureQuery.isEmpty()
                    ? "?reportType=" + script.getTest().getReportType()
                    : "&reportType=" + script.getTest().getReportType();
        }
        if (script.getTest().getPeriodStart() != null && !script.getTest().getPeriodStart().isEmpty()) {
            evaluateMeasureQuery += evaluateMeasureQuery.isEmpty()
                    ? "?periodStart=" + script.getTest().getPeriodStart()
                    : "&periodStart=" + script.getTest().getPeriodStart();
        }
        else {
            throw new RuntimeException("The $evaluate-measure operation requires a periodStart value");
        }
        if (script.getTest().getPeriodEnd() != null && !script.getTest().getPeriodEnd().isEmpty()) {
            evaluateMeasureQuery += evaluateMeasureQuery.isEmpty()
                    ? "?periodEnd=" + script.getTest().getPeriodEnd()
                    : "&periodEnd=" + script.getTest().getPeriodEnd();
        }
        else {
            throw new RuntimeException("The $evaluate-measure operation requires a periodEnd value");
        }
        if (script.getTest().getPractitioner() != null && !script.getTest().getPractitioner().isEmpty()) {
            evaluateMeasureQuery += evaluateMeasureQuery.isEmpty()
                    ? "?practitioner=" + script.getTest().getPractitioner()
                    : "&practitioner=" + script.getTest().getPractitioner();
        }
        if (script.getTest().getLastReceivedOn() != null && !script.getTest().getLastReceivedOn().isEmpty()) {
            evaluateMeasureQuery += evaluateMeasureQuery.isEmpty()
                    ? "?lastReceivedOn=" + script.getTest().getLastReceivedOn()
                    : "&lastReceivedOn=" + script.getTest().getLastReceivedOn();
        }

        return evaluateMeasureRequest + evaluateMeasureQuery;
    }
}
