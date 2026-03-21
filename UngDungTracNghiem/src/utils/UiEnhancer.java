package utils;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.text.Collator;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;

public final class UiEnhancer {
    private static final String PROP_ENHANCED = "uiEnhancer.enhanced";
    private static final String PROP_CONTAINER_WATCH = "uiEnhancer.containerWatchInstalled";
    private static final String PROP_TEXT_KEYS = "uiEnhancer.textKeysInstalled";
    private static final String PROP_TEXT_POPUP = "uiEnhancer.textPopupInstalled";
    public static final String PROP_DISABLE_TEXT_COPY_PASTE = "uiEnhancer.disableTextCopyPaste";
    private static final String PROP_COMBO_HOOK = "uiEnhancer.comboSortHookInstalled";
    private static final String PROP_COMBO_SORTING = "uiEnhancer.comboSorting";
    private static final String PROP_TABLE_STYLED = "uiEnhancer.tableStyled";
    private static volatile boolean installed = false;

    private UiEnhancer() {
    }

    public static void install() {
        if (installed) {
            return;
        }
        installed = true;

        SwingUtilities.invokeLater(() -> {
            installUiDefaults();
            enhanceAllShowingWindows();
            installWindowListener();
        });
    }

    private static void installUiDefaults() {
        Font baseTableFont = UIManager.getFont("Table.font");
        if (baseTableFont != null) {
            UIManager.put("Table.font", baseTableFont.deriveFont(Math.max(14f, baseTableFont.getSize2D())));
        }
        Font baseHeaderFont = UIManager.getFont("TableHeader.font");
        if (baseHeaderFont != null) {
            UIManager.put("TableHeader.font", baseHeaderFont.deriveFont(Font.BOLD, Math.max(14f, baseHeaderFont.getSize2D())));
        }
    }

    private static void installWindowListener() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (!(event instanceof WindowEvent we)) {
                    return;
                }
                int id = we.getID();
                if (id != WindowEvent.WINDOW_OPENED && id != WindowEvent.WINDOW_ACTIVATED) {
                    return;
                }
                if (we.getWindow() == null) {
                    return;
                }
                SwingUtilities.invokeLater(() -> enhanceComponentTree(we.getWindow()));
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
    }

    private static void enhanceAllShowingWindows() {
        for (java.awt.Window w : java.awt.Window.getWindows()) {
            if (w != null && w.isShowing()) {
                enhanceComponentTree(w);
            }
        }
    }

    public static void enhanceComponentTree(Component root) {
        if (root == null) {
            return;
        }

        Deque<Component> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Component c = stack.pop();
            enhanceComponent(c);
            if (c instanceof Container container) {
                installContainerWatcher(container);
                for (Component child : container.getComponents()) {
                    stack.push(child);
                }
                if (c instanceof JScrollPane sp) {
                    Component view = sp.getViewport() != null ? sp.getViewport().getView() : null;
                    if (view != null) {
                        stack.push(view);
                    }
                } else if (c instanceof JViewport vp) {
                    Component view = vp.getView();
                    if (view != null) {
                        stack.push(view);
                    }
                }
            }
        }
    }

    private static void enhanceComponent(Component c) {
        if (c instanceof JComponent jc) {
            Object enhanced = jc.getClientProperty(PROP_ENHANCED);
            if (Boolean.TRUE.equals(enhanced)) {
                // still allow per-type enhancements that may depend on late-populated models
            } else {
                jc.putClientProperty(PROP_ENHANCED, Boolean.TRUE);
            }
        }

        if (c instanceof JTable table) {
            styleTable(table);
        } else if (c instanceof JComboBox<?> combo) {
            installComboSorting(combo);
        } else if (c instanceof JTextComponent tc) {
            installTextCopyPaste(tc);
        }
    }

    private static void installContainerWatcher(Container container) {
        if (!(container instanceof JComponent jc)) {
            return;
        }
        if (Boolean.TRUE.equals(jc.getClientProperty(PROP_CONTAINER_WATCH))) {
            return;
        }
        jc.putClientProperty(PROP_CONTAINER_WATCH, Boolean.TRUE);
        container.addContainerListener(new ContainerListener() {
            @Override
            public void componentAdded(ContainerEvent e) {
                Component child = e.getChild();
                if (child == null) {
                    return;
                }
                SwingUtilities.invokeLater(() -> enhanceComponentTree(child));
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
            }
        });
    }

    private static void styleTable(JTable table) {
        if (!(table instanceof JComponent jc)) {
            return;
        }
        if (Boolean.TRUE.equals(jc.getClientProperty(PROP_TABLE_STYLED))) {
            return;
        }
        jc.putClientProperty(PROP_TABLE_STYLED, Boolean.TRUE);

        Font base = table.getFont();
        if (base != null) {
            table.setFont(base.deriveFont(Math.max(14f, base.getSize2D())));
        }

        int minRowHeight = table.getFontMetrics(table.getFont()).getHeight() + 10;
        if (table.getRowHeight() < minRowHeight) {
            table.setRowHeight(minRowHeight);
        }

        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setRowMargin(0);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        if (header != null) {
            Font headerFont = header.getFont();
            if (headerFont != null) {
                header.setFont(headerFont.deriveFont(Font.BOLD, Math.max(14f, headerFont.getSize2D())));
            }
            header.setOpaque(true);
            header.setBackground(new Color(245, 245, 245));
            header.setForeground(UIManager.getColor("TableHeader.foreground") != null ? UIManager.getColor("TableHeader.foreground") : Color.DARK_GRAY);
            header.setReorderingAllowed(false);

            TableCellRenderer baseHeaderRenderer = header.getDefaultRenderer();
            header.setDefaultRenderer((tbl, value, isSelected, hasFocus, row, column) -> {
                Component comp = baseHeaderRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                if (comp instanceof JComponent h) {
                    h.setOpaque(true);
                    h.setBackground(header.getBackground());
                    h.setForeground(header.getForeground());
                }
                return comp;
            });
        }

        TableCellRenderer baseRenderer = table.getDefaultRenderer(Object.class);
        Color even = new Color(250, 250, 250);
        Color odd = Color.WHITE;
        table.setDefaultRenderer(Object.class, (tbl, value, isSelected, hasFocus, row, column) -> {
            Component comp = baseRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
            if (!isSelected && comp != null) {
                comp.setBackground((row % 2 == 0) ? even : odd);
                if (comp instanceof JComponent jcComp) {
                    jcComp.setOpaque(true);
                }
            }
            return comp;
        });
    }

    private static void installComboSorting(JComboBox<?> combo) {
        if (!(combo instanceof JComponent jc)) {
            return;
        }
        if (Boolean.TRUE.equals(jc.getClientProperty(PROP_COMBO_HOOK))) {
            return;
        }
        jc.putClientProperty(PROP_COMBO_HOOK, Boolean.TRUE);

        combo.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> sortComboBoxItems(combo));
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        combo.addPropertyChangeListener("model", evt -> SwingUtilities.invokeLater(() -> sortComboBoxItems(combo)));

        sortComboBoxItems(combo);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void sortComboBoxItems(JComboBox<?> combo) {
        if (!(combo instanceof JComponent jc)) {
            return;
        }
        if (Boolean.TRUE.equals(jc.getClientProperty(PROP_COMBO_SORTING))) {
            return;
        }
        jc.putClientProperty(PROP_COMBO_SORTING, Boolean.TRUE);
        try {
            ComboBoxModel<?> model = combo.getModel();
            if (model == null) {
                return;
            }
            int size = model.getSize();
            if (size <= 1) {
                return;
            }

            Object selected = model.getSelectedItem();
            List<Object> pinned = new ArrayList<>();
            List<Object> items = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Object el = model.getElementAt(i);
                if (isPinnedComboItem(el)) {
                    pinned.add(el);
                } else {
                    items.add(el);
                }
            }

            Collator collator = Collator.getInstance(Locale.forLanguageTag("vi-VN"));
            collator.setStrength(Collator.PRIMARY);
            items.sort((a, b) -> collator.compare(stringValue(a), stringValue(b)));

            List<Object> merged = new ArrayList<>(pinned.size() + items.size());
            merged.addAll(pinned);
            merged.addAll(items);

            if (model instanceof DefaultComboBoxModel) {
                DefaultComboBoxModel dcm = (DefaultComboBoxModel) model;
                dcm.removeAllElements();
                for (Object el : merged) {
                    dcm.addElement(el);
                }
            } else {
                DefaultComboBoxModel newModel = new DefaultComboBoxModel(merged.toArray());
                ((JComboBox) combo).setModel(newModel);
            }
            combo.setSelectedItem(selected);
        } finally {
            jc.putClientProperty(PROP_COMBO_SORTING, Boolean.FALSE);
        }
    }

    private static boolean isPinnedComboItem(Object el) {
        if (el == null) {
            return true;
        }
        String s = stringValue(el).trim();
        if (s.isEmpty()) {
            return true;
        }
        String lower = s.toLowerCase(Locale.ROOT);
        if (lower.startsWith("--") || lower.startsWith("---") || lower.startsWith("==")) {
            return true;
        }
        return lower.startsWith("chọn")
                || lower.startsWith("chon")
                || lower.startsWith("tất cả")
                || lower.startsWith("tat ca")
                || lower.equals("all")
                || lower.startsWith("select");
    }

    private static String stringValue(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    private static void installTextCopyPaste(JTextComponent tc) {
        if (!(tc instanceof JComponent jc)) {
            return;
        }
        if (Boolean.TRUE.equals(jc.getClientProperty(PROP_DISABLE_TEXT_COPY_PASTE))) {
            return;
        }

        if (!Boolean.TRUE.equals(jc.getClientProperty(PROP_TEXT_KEYS))) {
            jc.putClientProperty(PROP_TEXT_KEYS, Boolean.TRUE);
            installTextKeyBindings(tc);
        }
        if (!Boolean.TRUE.equals(jc.getClientProperty(PROP_TEXT_POPUP))) {
            jc.putClientProperty(PROP_TEXT_POPUP, Boolean.TRUE);
            installTextPopupMenu(tc);
        }
    }

    private static void installTextKeyBindings(JTextComponent tc) {
        int menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        InputMap im = tc.getInputMap();
        javax.swing.ActionMap am = tc.getActionMap();

        ensureTextActions(am);

        im.put(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_C, menuMask), DefaultEditorKit.copyAction);
        im.put(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_V, menuMask), DefaultEditorKit.pasteAction);
        im.put(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_X, menuMask), DefaultEditorKit.cutAction);
        im.put(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_A, menuMask), DefaultEditorKit.selectAllAction);
    }

    private static void ensureTextActions(javax.swing.ActionMap am) {
        if (am.get(DefaultEditorKit.copyAction) == null) {
            am.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        }
        if (am.get(DefaultEditorKit.pasteAction) == null) {
            am.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        }
        if (am.get(DefaultEditorKit.cutAction) == null) {
            am.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        }
        if (am.get(DefaultEditorKit.selectAllAction) == null) {
            am.put(DefaultEditorKit.selectAllAction, new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Object src = e.getSource();
                    if (src instanceof JTextComponent tc) {
                        tc.selectAll();
                    }
                }
            });
        }
    }

    private static void installTextPopupMenu(JTextComponent tc) {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                maybeShow(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShow(e);
            }

            private void maybeShow(MouseEvent e) {
                if (!e.isPopupTrigger()) {
                    return;
                }
                if (!(e.getComponent() instanceof JTextComponent target)) {
                    return;
                }
                JPopupMenu menu = buildTextPopupMenu(target);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        };
        tc.addMouseListener(adapter);
    }

    private static JPopupMenu buildTextPopupMenu(JTextComponent tc) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem cut = new JMenuItem("Cắt");
        cut.setAction(tc.getActionMap().get(DefaultEditorKit.cutAction));
        cut.setText("Cắt");

        JMenuItem copy = new JMenuItem("Sao chép");
        copy.setAction(tc.getActionMap().get(DefaultEditorKit.copyAction));
        copy.setText("Sao chép");

        JMenuItem paste = new JMenuItem("Dán");
        paste.setAction(tc.getActionMap().get(DefaultEditorKit.pasteAction));
        paste.setText("Dán");

        JMenuItem selectAll = new JMenuItem("Chọn tất cả");
        selectAll.setAction(tc.getActionMap().get(DefaultEditorKit.selectAllAction));
        selectAll.setText("Chọn tất cả");

        menu.add(cut);
        menu.add(copy);
        menu.add(paste);
        menu.addSeparator();
        menu.add(selectAll);

        menu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                boolean hasSelection = tc.getSelectionStart() != tc.getSelectionEnd();
                boolean editable = tc.isEditable() && tc.isEnabled();
                boolean enabled = tc.isEnabled();

                boolean isPassword = tc instanceof javax.swing.JPasswordField;
                cut.setEnabled(enabled && editable && hasSelection);
                copy.setEnabled(enabled && hasSelection && !isPassword);
                paste.setEnabled(enabled && editable);
                selectAll.setEnabled(enabled && tc.getDocument() != null && tc.getDocument().getLength() > 0);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });

        return menu;
    }
}
