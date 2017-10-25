package finder;

import java.io.File;

public class SearchEntry {
    private File file;
    private Integer lineNum;
    private String lineText;

    SearchEntry(File file, Integer lineNum, String lineText)
    {
        this.file = file;
        this.lineNum = lineNum;
        this.lineText = lineText;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Integer getLineNum() {
        return lineNum;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    public String getLineText() {
        return lineText;
    }

    public void setLineText(String lineText) {
        this.lineText = lineText;
    }
}
