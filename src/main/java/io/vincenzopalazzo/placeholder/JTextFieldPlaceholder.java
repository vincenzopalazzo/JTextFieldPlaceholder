package io.vincenzopalazzo.placeholder;

import mdlaf.shadows.RoundedCornerBorder;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class JTextFieldPlaceholder extends JPanel {

    protected JLabel iconContainer;
    protected JLabel placeholder;
    private JSeparator separator;
    protected JTextField textField;

    public JTextFieldPlaceholder() {
        super(new FlowLayout());
        initView();
        initStyle();
    }

    protected void initView(){
        iconContainer = new JLabel();
        this.add(iconContainer);

        placeholder = new JLabel();
        separator = new JSeparator(JSeparator.VERTICAL);
        placeholder.setBorder(BorderFactory.createEmptyBorder(0,0,0,2));
        this.add(placeholder);
        this.add(separator);

        textField = new JTextField();
        textField.setMinimumSize(new Dimension(50, 20));
        textField.setPreferredSize(new Dimension(50, 20));
        textField.setSize(new Dimension(50, 20));
        super.add(textField);

        textField.setBorder(BorderFactory.createEmptyBorder());
        iconContainer.setBorder(BorderFactory.createEmptyBorder());
        placeholder.setBorder(BorderFactory.createEmptyBorder());
        setBorder(new RoundedCornerBorder(getBackground(), 6));
    }

    protected void initStyle(){
        setBackground(textField.getBackground());
        placeholder.setBackground(getBackground());
        iconContainer.setBackground(getBackground());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.paintLine(g);
    }

    protected void paintLine(Graphics graphics){
        graphics.setColor(Color.CYAN);
        graphics.fillRect(iconContainer.getX(), this.getHeight() - this.getY(), this.getWidth() - iconContainer.getWidth(), 1);
    }

    public JTextFieldPlaceholder setIcon(Icon icon){
        if(icon == null) throw new IllegalArgumentException("icon null");
        iconContainer.setIcon(icon);
        return this;
    }

    public JTextFieldPlaceholder setText(String text){
        if(text == null || text.isEmpty()) throw new IllegalArgumentException("Invalid text");
        placeholder.setText(text);
        return this;
    }

}