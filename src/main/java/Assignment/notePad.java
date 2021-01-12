package Assignment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Calendar;
import java.util.Date;


public class notePad {
    private JFrame f;
    private JLabel label;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private FileInputStream fileInStream;
    private JMenuItem item0, item1, item2, item3, item4, editItem0, editItem1, editItem2, editItem3, editItem4, editItem5, aboutItem, viewItem0, viewItem1;
    private JMenu menu1, menu2, menu3, menu4;

    // System clipboard
    private Toolkit toolkit=Toolkit.getDefaultToolkit();
    private Clipboard clipBoard=toolkit.getSystemClipboard();

    private int boundX = 400, boundY = 300;

    public void init(){
        // TODO Auto-generated constructor stub
        f=new JFrame("Test Editor");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(boundX, boundY, 400, 200);

        Container contentPane=f.getContentPane();
        textArea=new JTextArea();
        JScrollPane scrollPane=new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 300));
        JPanel panel=new JPanel();

        //textArea.getDocument().addDocumentListener(new SyntaxHighlighter(pane));

        JMenuBar menuBar1=new JMenuBar();  //Add the menu bar component
        f.setJMenuBar(menuBar1);          //Adds the menu bar to the top-level container

        menu1=new JMenu("File");
        menu2=new JMenu("Edit");
        menu3=new JMenu("About");
        menu4=new JMenu("View");

        //Adds the menu component to the menu bar component
        menuBar1.add(menu1);
        menuBar1.add(menu2);
        menuBar1.add(menu3);
        menuBar1.add(menu4);

        //Create the File menu item component
        item0 = new JMenuItem("New");
        item1=new JMenuItem("Open");
        item2=new JMenuItem("Save");
        item3=new JMenuItem("Print");
        item4=new JMenuItem("Exit");
        menu1.add(item0);
        menu1.add(item1);
        menu1.add(item2);
        menu1.addSeparator();           //A delimited line group of items between menu items
        menu1.add(item3);
        menu1.addSeparator();
        menu1.add(item4);

        //Create the Edit menu item component
        editItem0 = new JMenuItem("Search");
        editItem1 = new JMenuItem("Select All");
        editItem2 = new JMenuItem("Copy");
        editItem3 = new JMenuItem("Past");
        editItem4 = new JMenuItem("Cut");
        editItem5 = new JMenuItem("Time And Date");
        menu2.add(editItem0);
        menu2.addSeparator();
        menu2.add(editItem1);
        menu2.add(editItem2);
        menu2.add(editItem3);
        menu2.add(editItem4);
        menu2.addSeparator();
        menu2.add(editItem5);

        //Create the About component
        aboutItem = new JMenuItem("About");
        menu3.add(aboutItem);

        // create the View menu item
        viewItem0 = new JMenuItem("Eye-shield mode");
        viewItem1 = new JMenuItem("common mode");
        menu4.add(viewItem0);
        menu4.add(viewItem1);

        //Sets the visibility of the top-level container classes
        f.setVisible(true);
        label=new JLabel("",JLabel.CENTER);
        contentPane.add(label, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(panel, BorderLayout.SOUTH);
        f.pack();
    }

    //New event listening
    public void newListen(){
        item0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int i=JOptionPane.showConfirmDialog(null, "Please confirm your file was saved before open a new",
                        "Warning",JOptionPane.YES_NO_CANCEL_OPTION);
                //The result is determined by the selection of the button in the dialog box. When the single machine is YES, the window directly disappears
                if(i==0){
                    f.dispose();
                    boundX += 100;
                    boundY += 100;
                    init();
                    eventListener();
                }

            }
        });
    }

    //Open event
    public void openListen(){
        item1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(item1)==JFileChooser.APPROVE_OPTION) {//
                    File file = chooser.getSelectedFile();
                    textArea.setText(file.getName()+":"+file.getPath()+"\n"+file.length());
                    readFile(file);
                }

            }
        });
    }

    //save event
    public void saveListen(){
        item2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter1 = new FileNameExtensionFilter("text file(*.txt)", "txt");
                FileNameExtensionFilter filter2 = new FileNameExtensionFilter("PDF file(*.pdf)", "pdf");
                chooser.setFileFilter(filter1);
                chooser.setFileFilter(filter2);

                if (chooser.showSaveDialog(item2)==JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    String fname = chooser.getName(file);

                    if(chooser.getFileFilter() == filter1){
                        file=new File(chooser.getCurrentDirectory(),fname+".txt");
                        writeFile(file.getPath());
                    }
                    else if(chooser.getFileFilter() == filter2){
                        file=new File(chooser.getCurrentDirectory(),fname+".pdf");
                        writePdf(file.getPath());
                    }

                }
            }
        });
    }

    //Handles action events for exit menu items
    public void exitListen(){
        item4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e) {
                // TODO Auto-generated method stub
                int i=JOptionPane.showConfirmDialog(null, "是否真的退出系统",
                        "退出确认对话框",JOptionPane.YES_NO_CANCEL_OPTION);
                //The result is determined by the selection of the button in the dialog box. When the single machine is YES, the window directly disappears
                if(i==0)
                    f.dispose();

            }
        });
    }

    // search event
    public void searchListen(){
        editItem0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.requestFocus();
                find();
            }
        });
    }

    // search algorithm
    public void find(){
        //create event buttons
        //Keep other windows was active when Search window was opened
        final JDialog findDialog=new JDialog(f, "Search", false);//False allows other Windows to be active at the same time (i.e., no mode)
        Container con=findDialog.getContentPane();//Returns the contentPane object of this dialog box
        con.setLayout(new FlowLayout(FlowLayout.LEFT));

        //Search text table
        JLabel findContentLabel=new JLabel("Search content：");
        final JTextField findText=new JTextField(15);

        // cearte buttons
        JButton findNextButton=new JButton("Search next：");
        final JCheckBox matchCheckBox=new JCheckBox("Match case");
        ButtonGroup bGroup=new ButtonGroup();
        final JRadioButton upButton=new JRadioButton("Up Search");
        final JRadioButton downButton=new JRadioButton("Down Search");
        downButton.setSelected(true);
        bGroup.add(upButton);
        bGroup.add(downButton);
        JButton cancel=new JButton("cancel");

        // all button event listener
        //Cancel button event handling
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findDialog.dispose();
            }
        });
        //The "Find next" button listens
        findNextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Whether the "case sensitive (C)" JCheckBox is checked
                int k;
                final String str1,str2,str3,str4,strA,strB;
                str1=textArea.getText();
                str2=findText.getText();
                str3=str1.toUpperCase();
                str4=str2.toUpperCase();
                if(matchCheckBox.isSelected()){ //Case sensitivity
                    strA=str1;
                    strB=str2;
                }
                else{ //Case insensitive, at this point, the selection is all uppercase (or lowercase) for easy lookup
                    strA=str3;
                    strB=str4;
                }
                if(upButton.isSelected()) {
                    if(textArea.getSelectedText()==null)
                        k=strA.lastIndexOf(strB,textArea.getCaretPosition()-1);
                    else
                        k=strA.lastIndexOf(strB, textArea.getCaretPosition()-findText.getText().length()-1);
                    if(k>-1) {
                        textArea.setCaretPosition(k);
                        textArea.select(k,k+strB.length());
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Can't Searched it！","Search",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else if(downButton.isSelected()) {
                    if(textArea.getSelectedText()==null)
                        k=strA.indexOf(strB,textArea.getCaretPosition()+1);
                else
                    k=strA.indexOf(strB, textArea.getCaretPosition()-findText.getText().length()+1);
                    if(k>-1) {
                        textArea.setCaretPosition(k);
                        textArea.select(k,k+strB.length());
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Can't Searched it！","Search",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        // make a search panel
        JPanel panel1=new JPanel();
        JPanel panel2=new JPanel();
        JPanel panel3=new JPanel();
        JPanel directionPanel=new JPanel();
        directionPanel.setBorder(BorderFactory.createTitledBorder("Search Direction"));
        directionPanel.add(upButton);
        directionPanel.add(downButton);
        panel1.setLayout(new GridLayout(2,1));
        panel1.add(findNextButton);
        panel1.add(cancel);
        panel2.add(findContentLabel);
        panel2.add(findText);
        panel2.add(panel1);
        panel3.add(matchCheckBox);
        panel3.add(directionPanel);
        con.add(panel2);
        con.add(panel3);
        findDialog.setSize(410,180);
        findDialog.setResizable(false);//Unresizable
        findDialog.setLocation(230,280);
        findDialog.setVisible(true);
    }

    //Select All event triggered
    public void selectAllListen(){
        editItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.selectAll();
            }
        });
    }

    // copy event
    public void copyListen(){
        editItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.requestFocus();
                String text = textArea.getSelectedText();
                StringSelection selection = new StringSelection(text);
                clipBoard.setContents(selection,null);
            }
        });
    }

    // past event
    public void pastListen(){
        editItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.requestFocus();
                Transferable contents = clipBoard.getContents(this);
                if(contents == null) return;
                String text = "";
                try
                { text=(String)contents.getTransferData(DataFlavor.stringFlavor);
                }
                catch (Exception ignored)
                {
                }
                textArea.replaceRange(text, textArea.getSelectionStart(), textArea.getSelectionEnd());
            }
        });
    }

    // cut event
    public void cutListen(){
        editItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.requestFocus();
                String text = textArea.getSelectedText();
                StringSelection selection = new StringSelection(text);
                clipBoard.setContents(selection,null);
                textArea.replaceRange("", textArea.getSelectionStart(), textArea.getSelectionEnd());
            }
        });
    }

    //time and date event
    public void timeListen(){
        editItem5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar rightNow=Calendar.getInstance();
                Date date=rightNow.getTime();
                textArea.insert(date.toString(),textArea.getCaretPosition());
            }
        });
    }

    // about event
    public void aboutListen(){
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(null, "Members: Wang Kexin, Zhao Yizhen","About us", JOptionPane.PLAIN_MESSAGE);
            }
        });
    }

    // eye-field view mode
    public void eViewListen(){
        viewItem0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = action.class.getClassLoader().getResource("config.json").getPath();
                String s = readJsonFile(path);
                JSONObject jobj = JSON.parseObject(s);
                JSONArray movies = jobj.getJSONArray("RECORDS");//构建JSONArray数组
                JSONObject key = (JSONObject)movies.get(0);
                int greenR = Integer.parseInt((String) key.get("colorGreenR"));
                int greenG = Integer.parseInt((String) key.get("colorGreenG"));
                int greenB = Integer.parseInt((String) key.get("colorGreenB"));
                textArea.setBackground(new Color(greenR, greenG, greenB));
            }
        });
    }

    // common view mode
    public void cViewMode(){
        viewItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setBackground(Color.white);
            }
        });
    }

    //4/5000
    //Event listeners
    public void eventListener(){

        newListen();

        openListen();

        saveListen();

        exitListen();

        searchListen();

        selectAllListen();

        copyListen();

        pastListen();

        cutListen();

        timeListen();

        aboutListen();

        eViewListen();

        cViewMode();
    }

    public void readFile(File file){//Read the file
        BufferedReader bReader;
        try {
            bReader=new BufferedReader(new FileReader(file));
            StringBuilder sBuffer=new StringBuilder();
            String str;
            while((str=bReader.readLine())!=null){
                sBuffer.append(str).append('\n');
                System.out.println(str);
            }
            textArea.setText(sBuffer.toString());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // write file as txt
    public void writeFile(String savepath){//Write files
        FileOutputStream fos;
        try {
            fos=new FileOutputStream(savepath);
            fos.write(textArea.getText().getBytes());
            fos.close();
            System.out.println("save successful");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        textArea.getText();
    }

    // write file as pdf
    public void writePdf(String outpath) {
        try {
            //1 创建Document
            Document document = new Document();
            //2 获取PdfWriter
            PdfWriter.getInstance(document, new FileOutputStream(outpath));
            //3 打开
            document.open();
            //4 添加内容
            document.add(new Paragraph(textArea.getText()));
            //5 关闭
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // read json file
    public String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}