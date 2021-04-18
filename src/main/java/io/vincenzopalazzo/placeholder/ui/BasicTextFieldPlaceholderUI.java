package io.vincenzopalazzo.placeholder.ui;

import io.vincenzopalazzo.placeholder.JTextFieldPlaceholder;
import io.vincenzopalazzo.placeholder.listener.AbstractFocusComponent;
import io.vincenzopalazzo.placeholder.util.ComponentUtil;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

public class BasicTextFieldPlaceholderUI extends BasicPanelUI {

  protected static final String PREFIX = "TextFieldPlaceholder";
  protected static final String PREFIX_FATHER = "TextField";

  protected static String UNSELECTED_ICON_PATH = "/icons/unselected.png";
  protected static String SELECTED_ICON_PATH = "/icons/selected.png";

  protected Color background;
  protected Color foreground;
  protected Color disabledBackground;
  protected Color disabledForeground;
  protected Color colorFocusLine;
  protected Color colorUnfocusLine;
  protected Color separatorColor;
  protected Color placeholderColor;
  protected JTextFieldPlaceholder textFieldPlaceholder;
  protected JTextField textField;
  protected JLabel placeholder;
  protected TextFieldPlaceholderFocusListener focusListener;
  protected PropertyChangeListener changeListener;

  @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
  public static ComponentUI createUI(JComponent c) {
    return new BasicTextFieldPlaceholderUI();
  }

  public BasicTextFieldPlaceholderUI() {
    this.focusListener = new TextFieldPlaceholderFocusListener();
    this.changeListener = new ObserveValues();
  }

  @Override
  public void installUI(JComponent c) {
    super.installUI(c);
    textFieldPlaceholder = (JTextFieldPlaceholder) c;
    JToggleButton iconContainer = textFieldPlaceholder.getIconContainer();
    Icon selectedIcon = UIManager.getIcon(this.getPrefix() + ".selectedIcon");
    Icon icon = UIManager.getIcon(this.getPrefix() + ".icon");
    if (icon == null || selectedIcon == null) {
      selectedIcon = ComponentUtil.loadImage(SELECTED_ICON_PATH);
      icon = ComponentUtil.loadImage(UNSELECTED_ICON_PATH);
    }
    if (iconContainer != null) {
      iconContainer.setIcon(icon);
      iconContainer.setSelectedIcon(selectedIcon);
      // iconContainer.setOpaque(false);
      iconContainer.setBackground(this.background);
    }

    this.placeholder = textFieldPlaceholder.getPlaceholderComponent();
    if (placeholder != null) {
      // placeholder.setOpaque(false);
      placeholder.setForeground(this.placeholderColor);
      placeholder.setBackground(this.background);
    }
    int gapIconToPlaceholder =
        UIManager.getInt(this.getPrefix() + ".gapIconToPlaceholder") == 0
            ? 10
            : UIManager.getInt(this.getPrefix() + ".gapIconToPlaceholder");
    this.textFieldPlaceholder.setGapIconToPlaceholder(gapIconToPlaceholder);
    int gapSeparatorToPlaceholder =
        UIManager.getInt(this.getPrefix() + ".gapSeparatorToPlaceholder") == 0
            ? 10
            : UIManager.getInt(this.getPrefix() + ".gapSeparatorToPlaceholder");
    this.textFieldPlaceholder.setGapSeparatorToPlaceholder(gapSeparatorToPlaceholder);
    int gapTextToSeparator =
        UIManager.getInt(this.getPrefix() + ".gapTextToSeparator") == 0
            ? 10
            : UIManager.getInt(this.getPrefix() + ".gapTextToSeparator");
    this.textFieldPlaceholder.setGapTextToSeparator(gapTextToSeparator);

    /*
            if(textField != null){
                textField = textFieldPlaceholder.getTextFiled();
                textField.setForeground(this.foreground);
                textField.setBackground(this.background);
            }
    */
    this.textField = this.textFieldPlaceholder.getTextFiled();

    JSeparator separator = textFieldPlaceholder.getSeparator();
    if (separator != null) {

      separator.setBackground(this.background);
      separator.setForeground(this.separatorColor);
    }
    Color tmp = UIManager.getColor(this.getPrefix() + ".disabledBackground");
    this.disabledBackground = tmp == null ? Color.CYAN : tmp;
    tmp = UIManager.getColor(this.getPrefix() + ".disabledForeground");
    this.disabledForeground = tmp == null ? Color.CYAN : tmp;

    this.installListener();
  }

  @Override
  protected void installDefaults(JPanel p) {
    super.installDefaults(p);

    if (UIManager.getColor(this.getPrefix() + ".background") == null
        && UIManager.getColor(this.getPrefix() + ".foreground") == null) {
      LookAndFeel.installColorsAndFont(
          p, PREFIX_FATHER + ".background", PREFIX_FATHER + ".foreground", ".font");
      LookAndFeel.installBorder(p, PREFIX_FATHER + ".border");
    } else {
      LookAndFeel.installColorsAndFont(
          p, this.getPrefix() + ".background", this.getPrefix() + ".foreground", ".font");
      LookAndFeel.installBorder(p, this.getPrefix() + ".border");
    }
    this.background = p.getBackground();
    this.foreground = p.getForeground();
    this.colorFocusLine =
        UIManager.getColor(this.getPrefix() + "[Line].activeColor") == null
            ? Color.CYAN
            : this.colorFocusLine;
    this.colorUnfocusLine =
        UIManager.getColor(this.getPrefix() + "[Line].inactiveColor") == null
            ? Color.GRAY
            : this.colorUnfocusLine;
    Color color = UIManager.getColor(this.getPrefix().concat(".placeholderColor"));
    this.placeholderColor = color == null ? Color.DARK_GRAY : color;
    this.separatorColor =
        UIManager.getColor(this.getPrefix() + ".separatorColor") == null
            ? this.placeholderColor
            : UIManager.getColor(this.getPrefix() + ".separatorColor");
  }

  @Override
  public void paint(Graphics g, JComponent c) {
    if (!this.textField.isEnabled()) {
      this.textFieldPlaceholder.setBackground(this.disabledBackground);
      this.placeholder.setBackground(this.disabledBackground);
      this.placeholder.setForeground(this.disabledForeground);
      return;
    }
    this.textFieldPlaceholder.setBackground(this.background);
    this.placeholder.setBackground(this.background);
    this.placeholder.setForeground(this.foreground);
    this.paintLine(g);
  }

  @Override
  public void update(Graphics g, JComponent c) {
    super.update(g, c);
    this.paintLine(g);
  }

  @Override
  public void uninstallUI(JComponent c) {
    super.uninstallUI(c);
    this.uninstallListener();
    this.textField = null;
    this.focusListener = null;
    this.textFieldPlaceholder = null;
    this.colorUnfocusLine = null;
    this.colorFocusLine = null;
  }

  protected void installListener() {
    this.textFieldPlaceholder.addMouseListener(focusListener);
    if (this.placeholder != null) this.placeholder.addPropertyChangeListener(changeListener);
  }

  protected void uninstallListener() {
    this.textFieldPlaceholder.removeMouseListener(focusListener);
    if (this.placeholder != null) this.placeholder.removePropertyChangeListener(changeListener);
  }

  protected void paintLine(Graphics graphics) {
    if (colorFocusLine == null || colorUnfocusLine == null) {
      if (textField.isFocusOwner()) {
        this.colorFocusLine = UIManager.getColor("TextFieldPlaceholder[Line].activeColor");
      } else {
        this.colorUnfocusLine = UIManager.getColor("TextFieldPlaceholder[Line].inactiveColor");
      }
    }
    Color colorLine;
    if (this.textField.isFocusOwner()) {
      colorLine = this.colorFocusLine;
    } else {
      colorLine = this.colorUnfocusLine;
    }
    graphics.setColor(colorLine);
    graphics.fillRect(
        0, textFieldPlaceholder.getHeight() - 3, this.textFieldPlaceholder.getWidth() - 5, 1);
  }
  // getter
  public String getPrefix() {
    return PREFIX;
  }

  protected class TextFieldPlaceholderFocusListener extends AbstractFocusComponent {

    @Override
    public void mouseClicked(MouseEvent e) {
      if (textField != null) {
        textField.requestFocusInWindow();
      }
    }
  }

  // TODO make a UI for placeholder text
  protected class ObserveValues implements PropertyChangeListener {

    protected static final String FOREGROUND = "foreground";

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (FOREGROUND.equals(evt.getPropertyName())) {
        placeholderColor = (Color) evt.getNewValue();
      }
    }
  }
}
