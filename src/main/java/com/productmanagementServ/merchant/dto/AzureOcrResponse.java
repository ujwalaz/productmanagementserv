package com.productmanagementServ.merchant.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureOcrResponse {

    private List<Region> regions;

    public List<Region> getRegions() { return regions; }
    public void setRegions(List<Region> regions) { this.regions = regions; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Region {
        private List<Line> lines;
        public List<Line> getLines() { return lines; }
        public void setLines(List<Line> lines) { this.lines = lines; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Line {
        private String boundingBox; // "x,y,width,height"
        private List<Word> words;
        public String getBoundingBox() { return boundingBox; }
        public void setBoundingBox(String boundingBox) { this.boundingBox = boundingBox; }
        public List<Word> getWords() { return words; }
        public void setWords(List<Word> words) { this.words = words; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Word {
        private String boundingBox; // "x,y,width,height"
        private String text;
        public String getBoundingBox() { return boundingBox; }
        public void setBoundingBox(String boundingBox) { this.boundingBox = boundingBox; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}
