import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;


public class PorterStemAnalyzer extends Analyzer {
	
    File stopwordsFile = new File("npl\\common_words");

    @SuppressWarnings("deprecation")
    protected static CharArraySet loadStopwordSet(File stopwordsFile, Version matchVersion) throws IOException {
        Reader reader = null;
        try {
            reader = IOUtils.getDecodingReader(stopwordsFile, IOUtils.CHARSET_UTF_8);
            return WordlistLoader.getWordSet(reader, matchVersion);
        } finally {
            IOUtils.close(reader);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer source = new LetterTokenizer(Version.LATEST, reader);              
        TokenStream filter = new LowerCaseFilter(Version.LATEST, source);   
        CharArraySet mystopwords = null;
        try {
            mystopwords = loadStopwordSet(stopwordsFile, Version.LATEST);
        } catch (IOException ex) {
            Logger.getLogger(PorterStemAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
        filter = new StopFilter(Version.LATEST, filter, mystopwords);                  
        filter = new PorterStemFilter(filter);
        return new TokenStreamComponents(source, filter);
    }
}
