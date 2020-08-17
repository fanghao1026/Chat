

import java.awt.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.lang.annotation.Documented;

public class ChatWindow {

    private JFrame frame;
    private JTextField textField;
    private JTextField textField_1;
    private PrintStream ps;
    public JTextPane textPane;
    private StyledDocument doc;
    private int start;
    private int length;


    public void setPs(PrintStream ps) {
        this.ps = ps;
    }

    public void ChatWindow() {
        initialize();
    }

    public void setTitle(String user){
        frame.setTitle("Hello! "+user);
    }
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(700, 200, 653, 564);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);


        textField = new JTextField();
        textField.setFont(new Font("微软雅黑", Font.PLAIN, 30));
        textField.setDisabledTextColor(Color.black);
        textField.setBounds(14, 442, 431, 56);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setBounds(14, 405, 86, 24);
        frame.getContentPane().add(textField_1);
        textField_1.setColumns(10);

        textField_1.setVisible(false);

        JButton btnNewButton = new JButton("\u53D1\u9001");
        btnNewButton.setBounds(480, 449, 122, 42);
        frame.getContentPane().add(btnNewButton);

        textPane = new JTextPane();
        textPane.setFont(new Font("微软雅黑", Font.PLAIN, 30));
        //textPane.setDisabledTextColor(Color.black);
        textPane.setEnabled(false);
        textPane.setBounds(0, 13, 635, 365);
        frame.getContentPane().add(textPane);


        JRadioButton rdbtnNewRadioButton = new JRadioButton("\u7FA4\u53D1");
        rdbtnNewRadioButton.setBounds(124, 406, 157, 27);
        frame.getContentPane().add(rdbtnNewRadioButton);
        rdbtnNewRadioButton.setSelected(true);

        JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("\u5355\u53D1");
        rdbtnNewRadioButton_1.setBounds(425, 406, 157, 27);
        frame.getContentPane().add(rdbtnNewRadioButton_1);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rdbtnNewRadioButton);
        buttonGroup.add(rdbtnNewRadioButton_1);

        rdbtnNewRadioButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField_1.setVisible(false);
            }
        });


        rdbtnNewRadioButton_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField_1.setVisible(true);

            }
        });



        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if("".equals(textField.getText()))
                {
                    JOptionPane.showMessageDialog(frame, "消息不能为空！");
                }else {

                    if(rdbtnNewRadioButton_1.isSelected()){

                        insertDocument(textPane,"\n"+"您对"+textField_1.getText()+"说："+textField.getText(),Color.black);
                        //textPane.setText(textPane.getText()+"\n"+"您对"+textField_1.getText()+"说："+textField.getText());
                       ps.println(ChatProtocol.PRI_MSG+textField_1.getText()+ChatProtocol.SPLIT_SIGN+textField.getText()+ChatProtocol.PRI_MSG);
                    }else if(rdbtnNewRadioButton.isSelected()){
                        insertDocument(textPane,"\n"+"您对所有人说："+textField.getText(),Color.black);
                        //textPane.setText(textPane.getText()+"\n"+"您对所有人说："+textField.getText());
                       ps.println(ChatProtocol.PUB_MSG+frame.getTitle().substring(7)+ChatProtocol.SPLIT_SIGN+textField.getText()+ChatProtocol.PUB_MSG);
                    }

                    textField.setText("");
                }

            }
        });
        doc = textPane.getStyledDocument();
        doc.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                start = e.getOffset();
                length = e.getLength();
                doc.setCharacterAttributes(start,length,textPane.getStyle("Red"),true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        frame.setVisible(true);
    }
    public void insertDocument(JTextPane textPane,String text , Color textColor){

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textColor);
        StyleConstants.setBackground(set,textColor);//设置文字颜色
        StyleConstants.setFontSize(set, 25);//设置字体大小
        try
        {
            doc.insertString(doc.getLength(), text,set);//插入文字

        }catch (Exception e){

        }

    }
}
