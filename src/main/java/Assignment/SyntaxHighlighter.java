package Assignment;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SyntaxHighlighter implements DocumentListener {
    private Set<String> keywords;
    private Set<String> voidwords;
    private Style keywordStyle;
    private Style normalStyle;
    private Style voidwordStyle;

    public SyntaxHighlighter(JTextPane editor) {

        keywordStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
        normalStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
        voidwordStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
        StyleConstants.setForeground(keywordStyle, Color.ORANGE);
        StyleConstants.setForeground(normalStyle, Color.BLACK);
        StyleConstants.setForeground(voidwordStyle, Color.blue);
        // prepare the keywords and void words.

        keywords = new HashSet<String>();
        keywords.add("public");
        keywords.add("protected");
        keywords.add("private");
        keywords.add("import");
        keywords.add("class");
        keywords.add("static");
        keywords.add("if");
        keywords.add("else");
        keywords.add("for");
        keywords.add("while");


        voidwords = new HashSet<String>();
        voidwords.add("void");
        voidwords.add("int");
        voidwords.add("float");
        voidwords.add("string");
        voidwords.add("Date");
        voidwords.add("throws");
        voidwords.add("double");

    }

    public void colouring(StyledDocument doc, int pos, int len) throws BadLocationException {
        int start = indexOfWordStart(doc, pos);
        int end = indexOfWordEnd(doc, pos + len);

        char ch;
        while (start < end) {
            ch = getCharAt(doc, start);
            if (Character.isLetter(ch) || ch == '_') {
                start = colouringWord(doc, start);

            } else {
                SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));
                ++start;
            }
        }
    }

    public int colouringWord(StyledDocument doc, int pos) throws BadLocationException {
        int wordEnd = indexOfWordEnd(doc, pos);
        String word = doc.getText(pos, wordEnd - pos);

        if (keywords.contains(word)) {
//        	if the word is the keyword,coloring it with orange.
            SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, keywordStyle));
        }
        else if(voidwords.contains(word)) {
//        	if the word means void something,coloring it with blue.
            SwingUtilities.invokeLater(new ColouringTask(doc,pos,wordEnd -pos,voidwordStyle));
        }
        else {
//            the other words keep black
            SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, normalStyle));
        }

        return wordEnd;
//        return the word's subscript.
    }

    public char getCharAt(Document doc, int pos) throws BadLocationException {
        return doc.getText(pos, 1).charAt(0);
    }

    public int indexOfWordStart(Document doc, int pos) throws BadLocationException {
        for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos);

        return pos;
    }

    public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {
        for (; isWordCharacter(doc, pos); ++pos);

        return pos;
    }
    public boolean isWordCharacter(Document doc, int pos) throws BadLocationException {
        char ch = getCharAt(doc, pos);
        if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') { return true; }
        return false;
    }

    public void changedUpdate(DocumentEvent e) {

    }

    public void insertUpdate(DocumentEvent e) {
        try {
            colouring((StyledDocument) e.getDocument(), e.getOffset(), e.getLength());
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    public void removeUpdate(DocumentEvent e) {
        try {
            colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    private class ColouringTask implements Runnable {
        private StyledDocument doc;
        private Style style;
        private int pos;
        private int len;

        public ColouringTask(StyledDocument doc, int pos, int len, Style style) {
            this.doc = doc;
            this.pos = pos;
            this.len = len;
            this.style = style;
        }

        public void run() {
            try {
                //coloring function
                doc.setCharacterAttributes(pos, len, style, true);
            } catch (Exception e) {}
        }
    }

}
