package com.productmanagementServ.merchant.dto;

import java.util.List;

public class ScanResultResponse {

    private List<ScanLine> lines;

    public ScanResultResponse(List<ScanLine> lines) { this.lines = lines; }
    public List<ScanLine> getLines() { return lines; }

    public static class ScanLine {
        private String text;
        private int height; // font size proxy — larger = bigger text
        private int y;      // vertical position on image
        private List<ScanWord> words;

        public ScanLine(String text, int height, int y, List<ScanWord> words) {
            this.text = text; this.height = height; this.y = y; this.words = words;
        }
        public String getText() { return text; }
        public int getHeight() { return height; }
        public int getY() { return y; }
        public List<ScanWord> getWords() { return words; }
    }

    public static class ScanWord {
        private String text;
        private int height;
        private int y;

        public ScanWord(String text, int height, int y) {
            this.text = text; this.height = height; this.y = y;
        }
        public String getText() { return text; }
        public int getHeight() { return height; }
        public int getY() { return y; }
    }
}
