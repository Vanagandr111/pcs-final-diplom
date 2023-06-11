import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    Map<String, List<PageEntry>> word_page_list = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        var pdfs = pdfsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

        for (File pdf: pdfs) {
            var doc = new PdfDocument(new PdfReader(pdf));
            for(int p = 1; p <= doc.getNumberOfPages(); p++) {
                var page = doc.getPage(p);
                var pagetext = PdfTextExtractor.getTextFromPage(page);
                var wordsinpage = pagetext.split("\\P{IsAlphabetic}+");

                var amountOfWord = new HashMap<String, Integer>();
                for (String word: wordsinpage) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    amountOfWord.put(word.toLowerCase(), amountOfWord.getOrDefault(word, 0) + 1);
                }

                int finalP = p;
                amountOfWord.forEach((s, integer) -> {
                    var pagel =  word_page_list.getOrDefault(s, new ArrayList<>());
                    pagel.add(new PageEntry(pdf.getName(), finalP, integer));
                    word_page_list.put(s, pagel);
                });
            }
        }

        for (List<PageEntry> wordList: word_page_list.values()) {
            Collections.sort(wordList);
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return word_page_list.get(word.toLowerCase());
    }
}
