package io.vincenzopalazzo.placeholder;

import io.swingsnackbar.SnackBar;
import io.swingsnackbar.action.AbstractSnackBarAction;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;
import mdlaf.utils.MaterialColors;
import mdlaf.utils.MaterialFontFactory;
import mdlaf.utils.MaterialImageFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public class DemoFrame extends JFrame {

    static {
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel(new JMarsDarkTheme()));
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private JFrame frame = this;
    private JPanel container;
    private JTextFieldPlaceholder textFieldPlaceholder;

    public void initView() {
        initComponent();

        super.setContentPane(container);
        super.setSize(new Dimension(400, 400));
        super.setTitle("New Swing component from @vincenzopalazzo");
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
    }

    public void initComponent() {
        container = new JPanel();
        //Init component
        textFieldPlaceholder = new JTextFieldPlaceholder(new JTextField("ALIBABA"));

        //configure component
        textFieldPlaceholder.setIcon(MaterialImageFactory.getInstance().getImage(
                GoogleMaterialDesignIcons.BOOKMARK,
                MaterialColors.COSMO_DARK_GRAY
        )).setPlaceholderText("Lan/Lon")
                .setPlaceholderTextColor(MaterialColors.COSMO_DARK_GRAY)
                .setVisible(true);

        container.add(textFieldPlaceholder);
        JButton button = new JButton(MaterialImageFactory.getInstance().getImage(
                GoogleMaterialDesignIcons.SEND,
                MaterialColors.WHITE
        ));

        button.addActionListener(new AbstractAction() {
            private SnackBar snackBar;
            @Override
            public void actionPerformed(ActionEvent e) {
                snackBar = SnackBar.make(frame, "Lan/Lon: " + textFieldPlaceholder.getText(), "CLOSE")
                        .setGap(80)
                        .setIconTextStyle(MaterialFontFactory.getInstance().getFont(MaterialFontFactory.BOLD))
                        .setIconTextColor(MaterialColors.COSMO_RED)
                        .setDuration(SnackBar.LENGTH_LONG)
                        .setAction(new AbstractSnackBarAction() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                snackBar.dismiss();
                            }
                        })
                        .run();
            }
        });

        container.add(button);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DemoFrame demoFrame = new DemoFrame();
                demoFrame.initView();
            }
        });
    }
}
