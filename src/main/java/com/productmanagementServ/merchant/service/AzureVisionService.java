package com.productmanagementServ.merchant.service;

import com.productmanagementServ.merchant.dto.AzureOcrResponse;
import com.productmanagementServ.merchant.dto.ScanResultResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AzureVisionService {

    @Value("${azure.vision.endpoint}")
    private String endpoint;

    @Value("${azure.vision.key}")
    private String key;

    private final RestTemplate restTemplate;

    public AzureVisionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ScanResultResponse extractText(byte[] imageBytes) {
        String url = endpoint.replaceAll("/+$", "") + "/vision/v3.2/ocr?language=unk&detectOrientation=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Ocp-Apim-Subscription-Key", key);

        HttpEntity<byte[]> request = new HttpEntity<>(imageBytes, headers);
        AzureOcrResponse ocrResponse = restTemplate.postForObject(url, request, AzureOcrResponse.class);

        return mapToScanResult(ocrResponse);
    }

    private ScanResultResponse mapToScanResult(AzureOcrResponse ocr) {
        List<ScanResultResponse.ScanLine> lines = new ArrayList<>();
        if (ocr == null || ocr.getRegions() == null) return new ScanResultResponse(lines);

        for (AzureOcrResponse.Region region : ocr.getRegions()) {
            if (region.getLines() == null) continue;
            for (AzureOcrResponse.Line line : region.getLines()) {
                if (line.getWords() == null || line.getWords().isEmpty()) continue;

                int[] lineBbox = parseBbox(line.getBoundingBox());
                List<ScanResultResponse.ScanWord> words = new ArrayList<>();
                StringBuilder sb = new StringBuilder();

                for (AzureOcrResponse.Word word : line.getWords()) {
                    int[] wb = parseBbox(word.getBoundingBox());
                    words.add(new ScanResultResponse.ScanWord(word.getText(), wb[3], wb[1]));
                    if (sb.length() > 0) sb.append(' ');
                    sb.append(word.getText());
                }

                lines.add(new ScanResultResponse.ScanLine(sb.toString(), lineBbox[3], lineBbox[1], words));
            }
        }
        return new ScanResultResponse(lines);
    }

    // Azure OCR bounding box format: "x,y,width,height"
    private int[] parseBbox(String bbox) {
        if (bbox == null) return new int[]{0, 0, 0, 0};
        String[] parts = bbox.split(",");
        if (parts.length < 4) return new int[]{0, 0, 0, 0};
        return new int[]{
            Integer.parseInt(parts[0].trim()),
            Integer.parseInt(parts[1].trim()),
            Integer.parseInt(parts[2].trim()),
            Integer.parseInt(parts[3].trim())
        };
    }
}
